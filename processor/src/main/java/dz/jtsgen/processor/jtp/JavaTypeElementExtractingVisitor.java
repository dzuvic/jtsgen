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

package dz.jtsgen.processor.jtp;

import dz.jtsgen.annotations.TSIgnore;
import dz.jtsgen.processor.model.TSMember;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
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
class JavaTypeElementExtractingVisitor extends SimpleElementVisitor8<Void, TSAVisitorParam> {

    private static Logger LOG = Logger.getLogger(JavaTypeElementExtractingVisitor.class.getName());

    private final TSMemberVisitor tsMemberVisitor;

    private final Map<String, TSMember> members = new HashMap<>();

    // list of members to sort out setters only
    private final Set<String> extractableMembers = new HashSet<>();

    // the current Java Type
    private final TypeElement element;

    // the environment
    private TSAVisitorParam tsaVisitorParam;


    JavaTypeElementExtractingVisitor(TypeElement element, TSAVisitorParam visitorParam) {
        assert element != null && visitorParam != null;
        this.element = element;
        this.tsaVisitorParam = visitorParam;
        this.tsMemberVisitor = new TSMemberVisitor();
    }

    @Override
    public Void visitType(TypeElement e, TSAVisitorParam tsaVisitorParam) {
        LOG.log(Level.FINEST, () -> String.format("JTExV visiting type %s", e.toString()));
        TSMember member = tsMemberVisitor.visit(e.asType(), tsaVisitorParam);
        this.members.put(member.getName(), member);
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, TSAVisitorParam tsaVisitorParam) {
        final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
        final String name = e.getSimpleName().toString();
        final boolean isIgnored = isIgnored(e);
        LOG.log(Level.FINEST, () -> String.format("JTExV visiting variable %s%s", name, isIgnored?" (ignored)":""));
        if (isPublic && !members.containsKey(name)) {
            final String tsTypeOfExecutable = convertTypeMirrorOfMemberToTsType(e, tsaVisitorParam);
            members.put(name, new TSMember(name, tsTypeOfExecutable, false));
            if (! isIgnored) extractableMembers.add(name);
        }
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableElement e, TSAVisitorParam tsaVisitorParam) {
        LOG.fine(() -> String.format("JTExV visiting executable %s", e.toString()));
        if (isGetterOrSetter(e)) {
            final String name = nameFromMethod(e.getSimpleName().toString());
            final String tsTypeOfExecutable = convertTypeMirrorToTsType(e, tsaVisitorParam);
            final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
            final boolean isIgnored = isIgnored(e);
            LOG.finest(() -> "is getter or setter: " + (isPublic ? "public " : " ") + e.getSimpleName() + " -> " + name + ":" + tsTypeOfExecutable + " " +(isIgnored?"(ignored)":""));
            if (members.containsKey(name)) {
                // can't be read only anymore
                members.put(name, new TSMember(name, isGetter(e) ? tsTypeOfExecutable : members.get(name).getType(), false));
            } else {
                members.put(name, new TSMember(name, tsTypeOfExecutable, isGetter(e)));
            }
            if (isGetter(e) && isPublic && ! isIgnored) extractableMembers.add(name);
        }
        return null;
    }

    private boolean isIgnored(Element e) {
        return e.getAnnotationMirrors().stream().anyMatch( (x) -> TSIgnore.class.getName().equals(x.getAnnotationType().toString()));
    }


    private String convertTypeMirrorToTsType(ExecutableElement theElement, TSAVisitorParam tsaVisitorParam) {
        TypeMirror typeMirror = theElement.getReturnType();
        return typeMirror.accept(new MirrotTypeToTSConverterVisitor(theElement), tsaVisitorParam);
    }

    private String convertTypeMirrorOfMemberToTsType(VariableElement theElement, TSAVisitorParam tsaVisitorParam) {
        TypeMirror typeMirror = theElement.asType();
        return typeMirror.accept(new MirrotTypeToTSConverterVisitor(theElement), tsaVisitorParam);
    }

    List<TSMember> getMembers() {
        return members.values().stream().
                filter((x) -> extractableMembers.contains(x.getName()))
                .collect(Collectors.toList());
    }
}
