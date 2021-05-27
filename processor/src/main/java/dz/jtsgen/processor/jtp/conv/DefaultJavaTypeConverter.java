/*
 * Copyright (c) 2018 Dragan Zuvic
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

import dz.jtsgen.annotations.TSConstant;
import dz.jtsgen.annotations.TSMethod;
import dz.jtsgen.annotations.*;
import dz.jtsgen.processor.helper.DeclTypeHelper;
import dz.jtsgen.processor.jtp.conv.visitors.JavaTypeConverter;
import dz.jtsgen.processor.jtp.info.TSProcessingInfo;
import dz.jtsgen.processor.model.*;
import dz.jtsgen.processor.util.Either;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.jtp.conv.ConversionByDsl.directConversionType;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;

/**
 * this represents the default java type type converter. It converts a TypeElement to single TypeScript Target (TSType)
 * It does NOT convert a Java type, a subclass of TypeMirror.
 */
public class DefaultJavaTypeConverter implements JavaTypeConverter {

    private final TypeElement javaLangObjectElement;
    private final TypeElement javaLangEnumElement;
    private final TSProcessingInfo processingInfo;


    private static Logger LOG = Logger.getLogger(TypeScriptAnnotationProcessor.class.getName());

    DefaultJavaTypeConverter(TSProcessingInfo processingInfo) {
        this.processingInfo = processingInfo;
        this.javaLangObjectElement = this.processingInfo.getpEnv().getElementUtils().getTypeElement("java.lang.Object");
        this.javaLangEnumElement = this.processingInfo.getpEnv().getElementUtils().getTypeElement("java.lang.Enum");
    }


    @Override
    public Optional<TSType> convertJavaType(TypeElement e) {
        LOG.log(Level.FINEST, () -> String.format("DJTC converting java type %s", e == null ? "null" : e.toString()));
        return handleJavaType(e);
    }

//    private TSTargetType convertTypeMirrorToTsType(TypeElement theElement, TSProcessingInfo tsProcessingInfo) {
//        return new MirrotTypeToTSConverterVisitor(theElement, tsProcessingInfo, this).visit(theElement.asType());
//    }

    private Optional<TSType> handleJavaType(TypeElement element) {
        if (element == null) return Optional.empty();

        if (checkExclusion(element)) {
            LOG.info(() -> "DJTC Excluding " + element);
            return Optional.empty();
        }

        List<TSType> supertypes = convertSuperTypes(element);
        TSType result = null;

        List<TSTypeVariable> typeParams = element.getTypeParameters().stream()
                .map(x -> TSTypeVariableBuilder.builder()
                        .name(x.getSimpleName().toString())
                        .addAllBounds(convertBounds(x))
                        .build())
                .collect(Collectors.toList());
        LOG.fine(() -> "DJTC Element has type params: " + typeParams);

        TypeScript annotation = element.getAnnotation(TypeScript.class);

        switch (element.getKind()) {
            case CLASS:
            case INTERFACE: {
                JavaTypeElementExtractingVisitor visitor = new JavaTypeElementExtractingVisitor(element, processingInfo, this);
                collectMembers(element, visitor);

                if(isValidInterfaceType(element, visitor.getMethods().size())) {
                    result = TSFunctionBuilder.of(element)
                            .withName(getName(element, annotation))
                            .withMethods(visitor.getMethods())
                            .withConstants(visitor.getConstants())
                            .withSuperTypes(supertypes)
                            .withTypeParams(typeParams)
                            .withDocumentString(commentFrom(element));
                }
                else {
                    result = TSInterfaceBuilder.of(element)
                            .withName(getName(element, annotation))
                            .withMembers(visitor.getMembers())
                            .withConstants(visitor.getConstants())
                            .withMethods(visitor.getMethods())
                            .withSuperTypes(supertypes)
                            .withTypeParams(typeParams)
                            .withDocumentString(commentFrom(element));
                }

                break;
            }
            case ENUM: {
                result = TSEnumBuilder.of(element)
                        .withMembers(findEnumMembers(element))
                        .withTypeParams(new ArrayList<>())
                        .withDocumentString(commentFrom(element));
                break;
            }
            default:
                break;
        }
        return Optional.ofNullable(result);
    }

    private boolean isValidInterfaceType(TypeElement element, int methodCount) {
        boolean valid = element.getAnnotation(FunctionalInterface.class) != null;

        if(!valid) {
            return false;
        }

        if(methodCount <= 0) {
            processingInfo.getpEnv().getMessager().printMessage(Diagnostic.Kind.WARNING, "Invalid function type! Did you forget the " + TSMethod.class.getSimpleName() + " annotation? Generating type instead.", element);
            valid = false;
        }
        else if(methodCount > 1) {
            processingInfo.getpEnv().getMessager().printMessage(Diagnostic.Kind.WARNING, "Invalid function type! More than one function was found. Generating type instead.", element);
            valid = false;
        }

        return valid;
    }

    private String getName(TypeElement element, TypeScript annotation) {
        String name;

        if(annotation != null && !annotation.name().isEmpty()) {
            name = annotation.name();
        }
        else {
            name = element.getSimpleName().toString();
        }
        return name;
    }


    private Optional<String> commentFrom(TypeElement element) {
        return Optional.ofNullable(this.processingInfo.getpEnv().getElementUtils().getDocComment(element));
    }

    private List<TSType> convertSuperTypes(TypeElement element) {
        final List<? extends TypeMirror> superTypes = this.processingInfo.getpEnv().getTypeUtils().directSupertypes(element.asType());
        LOG.finest(() -> "DJTC direct supertypes of " + element + " are " + superTypes);
        List<TypeElement> filteredSuperTypes = superTypes
                .stream().map(DeclTypeHelper::declaredTypeToTypeElement)
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());

        List<TSType> result = filteredSuperTypes.stream()
                .filter(x -> !isMarkerInterface(x))
                .filter(x -> !isTopType(x))
                .filter(x -> !checkExclusion(x))
                .map(x -> {
                    LOG.info("DJTC converting supertype " + x);
                    return handleJavaType(x);
                })
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());

        this.processingInfo.getTsModel().addTSTypes(result);

        return result;
    }


    private List<Either<TSTargetType, TSType>> convertBounds(TypeParameterElement element) {
        if (element.getBounds().isEmpty()) return new ArrayList<>();

        List<Either<TSTargetType, TSType>> result = element.getBounds().stream()
                .map(this.processingInfo.getpEnv().getTypeUtils()::asElement)
                .filter(x -> x instanceof TypeElement)
                .map(x -> (TypeElement) x)
                .filter(x -> !isMarkerInterface(x))
                .filter(x -> !isTopType(x))
                .filter(x -> !checkExclusion(x))
                .map(x -> {
                    LOG.info("DJTC converting Bound " + x);
                    Optional<TSTargetType> tsTargetType = convertedByDSL(x);
                    return tsTargetType
                            .map(Either::<TSTargetType, Optional<TSType>>left)
                            .orElseGet(() -> Either.right(handleJavaType(x)));
                })
                .filter(x -> x.isLeft() || (x.toOptional().flatMap(y -> Optional.of(y.isPresent())).orElse(false)))
                .map(
                        x -> (x.isLeft()) ? Either.<TSTargetType, TSType>left(x.leftValue())
                                : Either.<TSTargetType, TSType>right(x.value().get())
                )
                .collect(Collectors.toList());

        this.processingInfo.getTsModel().addTSTypes(
                result.stream().filter(Either::isRight).map(Either::value).collect(Collectors.toList())
        );

        return result;
    }


    private boolean isTopType(TypeElement typeElement) {
        boolean r =
                typeElement.getQualifiedName().equals(this.javaLangObjectElement.getQualifiedName())
                        || typeElement.getQualifiedName().contentEquals(this.javaLangEnumElement.getQualifiedName().toString());
        if (r) LOG.fine(() -> "DJTC " + typeElement + " is top type");
        return r;
    }

    private boolean isMarkerInterface(TypeElement typeElement) {
        boolean isMarker = typeElement.getKind() == ElementKind.INTERFACE
                && typeElement.getEnclosedElements().size() == 0;
        if (isMarker) LOG.fine(() -> "DJTC " + typeElement + " is marker interface");
        return isMarker;
    }

    private boolean checkExclusion(TypeElement element) {
        final String typeName = element.toString();
        boolean r = this.processingInfo.getTsModel().getModuleInfo().getExcludes().stream().anyMatch(
                x -> x.matcher(typeName).find()
        );
        if (r) LOG.fine(() -> "DJTC exclusion " + element);
        return r;
    }

    private Collection<? extends TSMember> findEnumMembers(TypeElement element) {
        return element.getEnclosedElements().stream()
                .filter(x -> x.getKind() == ENUM_CONSTANT)
                .map(this::convertEnumMemberByStrategy
                ).collect(Collectors.toList());
    }

    /**
     * convert  the enum mamber by conversion strategy
     * @param x the element
     * @return the TSEnumMember model
     */
    private TSEnumMember convertEnumMemberByStrategy(Element x) {
        final String enumName;
        TSConstant constant = x.getAnnotation(TSConstant.class);

        if (constant == null || constant.name().isEmpty()) {
            enumName = x.getSimpleName().toString();
        }
        else {
            enumName = constant.name();
        }

        TSEnumConstant enumConstant = x.getAnnotation(TSEnumConstant.class);
        final String value;

        if(enumConstant == null || enumConstant.value().isEmpty()) {
            value = x.getSimpleName().toString();
        }
        else {
            value = enumConstant.value();
        }


        final Optional<String> comment = Optional.ofNullable(
                this.processingInfo.getpEnv().getElementUtils().getDocComment(x)
        );
        EnumExportStrategy enumExportStrategy = this.processingInfo.getTsModel().getModuleInfo().enumExportStrategy();
        final TSEnumMember result;
        if (enumExportStrategy == EnumExportStrategy.STRING) {
            result = TSEnumMemberBuilder.of(enumName)
                    .withExportStrategyStringRepresentation(value)
                    .withComment(comment);
        } else {
            assert enumExportStrategy == EnumExportStrategy.NUMERIC;
            result = TSEnumMemberBuilder.of(enumName)
                    .withComment(comment);
        }
        return result;
    }

    private void collectMembers(TypeElement e, JavaTypeElementExtractingVisitor visitor) {
        LOG.fine(() -> "DJTC find members and methods in java type " + e);
        e.getEnclosedElements().stream()
                .filter(x -> x.getKind() == ElementKind.FIELD || x.getKind() == ElementKind.METHOD || x.getKind() == ElementKind.CONSTRUCTOR)
                .forEach(visitor::visit);
    }

    /**
     * Check if a type elemnet can be directly converted from a DSL expression
     * this is especially needed for types in bounds. see #46
     *
     * @param element the java type element
     * @return a converted TSType if possible
     */
    private Optional<TSTargetType> convertedByDSL(TypeElement element) {
        return directConversionType(element.asType(), this.processingInfo.declaredTypeConversions(), this.processingInfo.getpEnv());
    }
}


