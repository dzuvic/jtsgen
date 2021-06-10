/*
 * Copyright (c) 2017 Dragan Zuvic
 *
 * This file is part of jtsgen.
 *
 * jtsgen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jtsgen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jtsgen.  If not, see http://www.gnu.org/licenses/
 *
 */

package dz.jtsgen.processor.jtp.conv;

import dz.jtsgen.annotations.*;
import dz.jtsgen.processor.jtp.conv.visitors.JavaTypeConverter;
import dz.jtsgen.processor.jtp.info.TSProcessingInfo;
import dz.jtsgen.processor.model.TSConstant;
import dz.jtsgen.processor.model.TSMethod;
import dz.jtsgen.processor.model.*;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * processes the Elements of an java interface or class, members and variables.
 * <p>
 * <p>
 * because of beans this Visitor is stateful: return type is void.
 */
class JavaTypeElementExtractingVisitor extends SimpleElementVisitor8<Void, Void> {

    private static Logger LOG = Logger.getLogger(JavaTypeElementExtractingVisitor.class.getName());


    private final Map<String, TSMember> members = new HashMap<>();
    private final Map<String, TSConstant> constants = new HashMap<>();

    private final List<TSMethod> methods = new ArrayList<>();

    // list of members to sort out setters only
    private final Set<String> extractableMembers = new HashSet<>();

    // the current Java Type
    private final TypeElement typeElementToConvert;

    // the environment
    private TSProcessingInfo tsProcessingInfo;

    // the converter for unknown types
    private JavaTypeConverter javaTypeConverter;



    JavaTypeElementExtractingVisitor(TypeElement typeElementToConvert, TSProcessingInfo visitorParam, JavaTypeConverter javaTypeConverter) {
        super();
        if (typeElementToConvert == null || visitorParam == null) throw new IllegalArgumentException();
        this.typeElementToConvert = typeElementToConvert;
        this.tsProcessingInfo = visitorParam;
        this.javaTypeConverter = javaTypeConverter;

    }

    @Override
    public Void visitType(TypeElement e, Void notcalled) {
        LOG.warning(() -> String.format("JTExV visiting type %s not used, but called", e.toString()));
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, Void notcalled) {
        final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
        final String name = nameOfConstant(e);
        final boolean isIgnored = isIgnored(e);
        final boolean isReadOnlyAnnotation = readOnlyAnnotation(e) || readOnlyAnnotation(this.typeElementToConvert);
        LOG.log(Level.FINEST, () -> String.format("JTExV visiting variable %s%s", name, isIgnored?" (ignored)":""));


        if(!isIgnored && e.getConstantValue() != null && e.getAnnotation(dz.jtsgen.annotations.TSConstant.class) != null) {
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorOfMemberToTsType(e, tsProcessingInfo);
            final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
            TSConstantBuilder constant = TSConstantBuilder
                    .of(name, tsTypeOfExecutable, e.getConstantValue())
                    .withComment(comment);

            constants.put(name, constant);
        }
        else if (isPublic && !members.containsKey(name)) {
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorOfMemberToTsType(e, tsProcessingInfo);
            final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
            members.put(name,
                    TSRegularMemberBuilder
                            .of(
                                    name,
                                    tsTypeOfExecutable,
                                    isReadOnlyAnnotation)
                            .withComment(comment).withOptional(isOptional(e)));
            if (! isIgnored) extractableMembers.add(name);
        }
        return null;
    }


    @Override
    public Void visitExecutable(ExecutableElement e, Void notcalled) {
        LOG.fine(() -> String.format("JTExV visiting executable %s", e.toString()));

        if(e.getKind() == ElementKind.CONSTRUCTOR) {
            handleConstructor(e);
            return null;
        }

        dz.jtsgen.annotations.TSMethod tsMethodAnnotation = e.getAnnotation(dz.jtsgen.annotations.TSMethod.class);

        if(tsMethodAnnotation != null) {
            final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorToTsType(e, tsProcessingInfo).withNullable(isNullable(e)).withOptional(isOptional(e));
            Map<String, TSTargetType> params = new LinkedHashMap<>();
            e.getParameters().forEach(variableElement ->
                    {
                        TSTargetType tsTargetType = convertTypeMirrorOfMemberToTsType(variableElement, tsProcessingInfo);
                        tsTargetType = tsTargetType.withNullable(isNullable(variableElement));
                        tsTargetType = tsTargetType.withOptional(isOptional(variableElement));
                        params.put(variableElement.getSimpleName().toString(), tsTargetType);
                    }
            );

            String name;
            if(tsMethodAnnotation.name().isEmpty()) {
                name = e.getSimpleName().toString();
            }
            else {
                name = tsMethodAnnotation.name();
            }


            List<TSTypeVariable> typeParams = e.getTypeParameters().stream()
                    .map(x -> TSTypeVariableBuilder.builder()
                            .name(x.getSimpleName().toString())
                            .addAllBounds(javaTypeConverter.convertBounds(x))
                            .build())
                    .collect(Collectors.toList());
            LOG.fine(() -> "DJTC Method Element has type params: " + typeParams);

            methods.add(TSMethodMemberBuilder
                    .of(name, tsTypeOfExecutable, params)
                    .withTypeParams(typeParams)
                    .withComment(comment));
        }
        else if (isGetterOrSetter(e)) {
            final String rawName = nameOfMethod(e).orElse("");
            final String name = mappedName(rawName);
            final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
            final boolean isIgnored = isIgnored(e);
            final boolean isReadOnly = readOnlyAnnotation(e) || readOnlyAnnotation(this.typeElementToConvert);
            final boolean isNullable = isNullable(e) || isNullable(this.typeElementToConvert);
            final boolean isOptional = isOptional(e) || isOptional(this.typeElementToConvert);
            if (isGetter(e) && ( !isPublic ||  isIgnored )) return null; // return early for not converting private types
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorToTsType(e, tsProcessingInfo);
            LOG.fine(() -> "is getter or setter: " + (isPublic ? "public " : " ") + e.getSimpleName() + " -> " + name +"(" + rawName+ ")" + ":" + tsTypeOfExecutable + " " +(isIgnored?"(ignored)":""));
            if (members.containsKey(name)) {
                // can't be read only anymore
                final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
                members.put(name, TSRegularMemberBuilder
                        .of(
                                name,
                                isGetter(e) ? tsTypeOfExecutable : members.get(name).getType(),
                                isReadOnly)
                        .withComment(comment)
                        .withNullable(isNullable || members.get(name).getNullable())
                        .withOptional(isOptional)
                );
            } else {
                final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
                members.put(name, TSRegularMemberBuilder
                        .of(name, tsTypeOfExecutable, isReadOnly)
                        .withComment(comment)
                        .withNullable(isNullable)
                        .withOptional(isOptional)
                );
            }
            if (isGetter(e)) extractableMembers.add(name);
        }
        return null;
    }

    private boolean isNullable(AnnotatedConstruct element) {
        return element.getAnnotationMirrors().stream().anyMatch(annotation ->
            annotation.getAnnotationType().asElement().getSimpleName().contentEquals("Nullable")
        );
    }

    private void handleConstructor(ExecutableElement e) {
        if(e.getAnnotation(TSConstructor.class) == null) {
            return;
        }
        final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
        final TSTargetType tsTypeOfExecutable = new MirrorTypeToTSConverterVisitor(e, tsProcessingInfo, javaTypeConverter).visit(typeElementToConvert.asType());

        TSInterfaceBuilder.of(e.getEnclosingElement());
        Map<String, TSTargetType> params = new LinkedHashMap<>();
        e.getParameters().forEach(variableElement ->
                params.put(variableElement.getSimpleName().toString(), convertTypeMirrorOfMemberToTsType(variableElement, tsProcessingInfo))
        );

        methods.add(TSMethodMemberBuilder
                .of("new", tsTypeOfExecutable, params)
                .withComment(comment));
    }

    private String mappedName(String rawName) {
        return this.tsProcessingInfo.nameMapper().mapMemberName(rawName);
    }

    private boolean isGetter(ExecutableElement e) {
        return this.tsProcessingInfo.executableHelper().isGetter(e);
    }

    private Optional<String> nameOfMethod(ExecutableElement e) {
        TSProperty property = e.getAnnotation(TSProperty.class);
        String methodName;
        if(property != null && !property.name().isEmpty()) {
            methodName = property.name();
        }
        else {
            methodName = e.getSimpleName().toString();
        }
        return this.tsProcessingInfo.executableHelper().extractRawMemberName(methodName);
    }

    private String nameOfConstant(VariableElement e) {
        dz.jtsgen.annotations.TSConstant constant = e.getAnnotation(dz.jtsgen.annotations.TSConstant.class);
        String methodName;
        if(constant != null && !constant.name().isEmpty()) {
            methodName = constant.name();
        }
        else {
            methodName = e.getSimpleName().toString();
        }
        return methodName;
    }

    private boolean isGetterOrSetter(ExecutableElement e) {
        return this.tsProcessingInfo.executableHelper().isGetterOrSetter(e);
    }

    private boolean readOnlyAnnotation(Element e) {
        final TypeElement annoTationElement = this.tsProcessingInfo.elementCache().typeElementByCanonicalName(TSReadOnly.class.getCanonicalName());
        return e.getAnnotationMirrors().stream().anyMatch( (x) ->
                x.getAnnotationType().asElement().equals(annoTationElement)
        );
    }

    private boolean isOptional(AnnotatedConstruct e) {
        final TypeElement annoTationElement = this.tsProcessingInfo.elementCache().typeElementByCanonicalName(TSOptional.class.getCanonicalName());
        return e.getAnnotationMirrors().stream().anyMatch( (x) ->
                x.getAnnotationType().asElement().equals(annoTationElement)
        );
    }

    private boolean isIgnored(Element e) {
        final TypeElement annoTationElement = this.tsProcessingInfo.elementCache().typeElementByCanonicalName(TSIgnore.class.getCanonicalName());
        return e.getAnnotationMirrors().stream().anyMatch( (x) ->
                x.getAnnotationType().asElement().equals(annoTationElement));
    }


    private TSTargetType convertTypeMirrorToTsType(ExecutableElement theElement, TSProcessingInfo tsProcessingInfo) {
        return new MirrorTypeToTSConverterVisitor(theElement, tsProcessingInfo, javaTypeConverter).visit(theElement.getReturnType());
    }

    private TSTargetType convertTypeMirrorOfMemberToTsType(VariableElement theElement, TSProcessingInfo TSProcessingInfo) {
        return new MirrorTypeToTSConverterVisitor(theElement, TSProcessingInfo, javaTypeConverter).visit(theElement.asType());
    }

    List<TSMember> getMembers() {
        return members.values().stream().
                filter((x) -> extractableMembers.contains(x.getName()))
                .collect(Collectors.toList());
    }

    List<TSConstant> getConstants() {
        return new ArrayList<>(constants.values());
    }

    List<TSMethod> getMethods() {
        return methods;
    }
}
