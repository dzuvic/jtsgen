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

import dz.jtsgen.annotations.TSIgnore;
import dz.jtsgen.annotations.TSReadOnly;
import dz.jtsgen.processor.model.TSMember;
import dz.jtsgen.processor.model.TSRegularMemberBuilder;
import dz.jtsgen.processor.model.TSTargetType;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.helper.ExecutableElementHelper.*;

/**
 * processes the Elements of an java interface or class.
 * <p>
 * <p>
 * because of beans this Visitor is stateful: return type is void.
 */
class JavaTypeElementExtractingVisitor extends SimpleElementVisitor8<Void, Void> {

    private static Logger LOG = Logger.getLogger(JavaTypeElementExtractingVisitor.class.getName());


    private final Map<String, TSMember> members = new HashMap<>();

    // list of members to sort out setters only
    private final Set<String> extractableMembers = new HashSet<>();

    // the current Java Type
    private final TypeElement typeElementToConvert;

    // the environment
    private TSProcessingInfo tsProcessingInfo;



    JavaTypeElementExtractingVisitor(TypeElement typeElementToConvert, TSProcessingInfo visitorParam) {
        assert typeElementToConvert != null && visitorParam != null;
        this.typeElementToConvert = typeElementToConvert;
        this.tsProcessingInfo = visitorParam;

    }

    @Override
    public Void visitType(TypeElement e, Void notcalled) {
        LOG.warning(() -> String.format("JTExV visiting type %s not used, but called", e.toString()));
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, Void notcalled) {
        final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
        final String name = e.getSimpleName().toString();
        final boolean isIgnored = isIgnored(e);
        final boolean  isReadOnlyAnnotation = readOnlyAnnotation(e) || readOnlyAnnotation(this.typeElementToConvert);
        LOG.log(Level.FINEST, () -> String.format("JTExV visiting variable %s%s", name, isIgnored?" (ignored)":""));
        if (isPublic && !members.containsKey(name)) {
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorOfMemberToTsType(e, tsProcessingInfo);
            members.put(name, TSRegularMemberBuilder.of(name,tsTypeOfExecutable, isReadOnlyAnnotation));
            if (! isIgnored) extractableMembers.add(name);
        }
        return null;
    }


    @Override
    public Void visitExecutable(ExecutableElement e, Void notcalled) {
        LOG.fine(() -> String.format("JTExV visiting executable %s", e.toString()));
        if (isGetterOrSetter(e)) {
            final String name = nameFromMethod(e.getSimpleName().toString());
            final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
            final boolean isIgnored = isIgnored(e);
            final boolean isReadOnly = readOnlyAnnotation(e) || readOnlyAnnotation(this.typeElementToConvert);
            if (isGetter(e) && ( !isPublic ||  isIgnored )) return null; // return early for not converting private types
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorToTsType(e, tsProcessingInfo);
            LOG.fine(() -> "is getter or setter: " + (isPublic ? "public " : " ") + e.getSimpleName() + " -> " + name + ":" + tsTypeOfExecutable + " " +(isIgnored?"(ignored)":""));
            if (members.containsKey(name)) {
                // can't be read only anymore
                members.put(name, TSRegularMemberBuilder.of(name, isGetter(e) ? tsTypeOfExecutable : members.get(name).getType(), isReadOnly));
            } else {
                members.put(name, TSRegularMemberBuilder.of(name, tsTypeOfExecutable, isReadOnly));
            }
            if (isGetter(e)) extractableMembers.add(name);
        }
        return null;
    }

    private boolean readOnlyAnnotation(Element e) {
        final TypeElement annoTationElement = this.tsProcessingInfo.elementCache().typeElementByCanonicalName(TSReadOnly.class.getCanonicalName());
        return e.getAnnotationMirrors().stream().anyMatch( (x) ->
                x.getAnnotationType().asElement().equals(annoTationElement)
        );
    }

    private boolean isIgnored(Element e) {
        final TypeElement annoTationElement = this.tsProcessingInfo.elementCache().typeElementByCanonicalName(TSIgnore.class.getCanonicalName());
        return e.getAnnotationMirrors().stream().anyMatch( (x) ->
                x.getAnnotationType().asElement().equals(annoTationElement));
    }


    private TSTargetType convertTypeMirrorToTsType(ExecutableElement theElement, TSProcessingInfo TSProcessingInfo) {
        return new MirrotTypeToTSConverterVisitor(theElement, TSProcessingInfo).visit(theElement.getReturnType());
    }

    private TSTargetType convertTypeMirrorOfMemberToTsType(VariableElement theElement, TSProcessingInfo TSProcessingInfo) {
        return new MirrotTypeToTSConverterVisitor(theElement, TSProcessingInfo).visit(theElement.asType());
    }

    List<TSMember> getMembers() {
        return members.values().stream().
                filter((x) -> extractableMembers.contains(x.getName()))
                .collect(Collectors.toList());
    }
}
