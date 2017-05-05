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
import dz.jtsgen.processor.model.TSTargetType;
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
class JavaTypeElementExtractingVisitor extends SimpleElementVisitor8<Void, Void> {

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
    public Void visitType(TypeElement e, Void notcalled) {
        LOG.log(Level.FINEST, () -> String.format("JTExV visiting type %s", e.toString()));
        Optional<TSMember> member = Optional.ofNullable(tsMemberVisitor.visit(e.asType(), this.tsaVisitorParam));
        member.ifPresent(x->this.members.put(x.getName(), x));
        if (!member.isPresent()) {
            LOG.info( () -> "could not the type '" + e + "' to a TSMember");
        }
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, Void notcalled) {
        final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
        final String name = e.getSimpleName().toString();
        final boolean isIgnored = isIgnored(e);
        LOG.log(Level.FINEST, () -> String.format("JTExV visiting variable %s%s", name, isIgnored?" (ignored)":""));
        if (isPublic && !members.containsKey(name)) {
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorOfMemberToTsType(e, tsaVisitorParam);
            members.put(name, new TSMember(name, tsTypeOfExecutable, false));
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
            if (isGetter(e) && ( !isPublic ||  isIgnored )) return null; // return early for not converting private types
            final TSTargetType tsTypeOfExecutable = convertTypeMirrorToTsType(e, tsaVisitorParam);
            LOG.fine(() -> "is getter or setter: " + (isPublic ? "public " : " ") + e.getSimpleName() + " -> " + name + ":" + tsTypeOfExecutable + " " +(isIgnored?"(ignored)":""));
            if (members.containsKey(name)) {
                // can't be read only anymore
                members.put(name, new TSMember(name, isGetter(e) ? tsTypeOfExecutable : members.get(name).getType(), false));
            } else {
                members.put(name, new TSMember(name, tsTypeOfExecutable, isGetter(e)));
            }
            if (isGetter(e)) extractableMembers.add(name);
        }
        return null;
    }

    private boolean isIgnored(Element e) {
        return e.getAnnotationMirrors().stream().anyMatch( (x) -> TSIgnore.class.getName().equals(x.getAnnotationType().toString()));
    }


    private TSTargetType convertTypeMirrorToTsType(ExecutableElement theElement, TSAVisitorParam tsaVisitorParam) {
        return new MirrotTypeToTSConverterVisitor(theElement,tsaVisitorParam).visit(theElement.getReturnType());
    }

    private TSTargetType convertTypeMirrorOfMemberToTsType(VariableElement theElement, TSAVisitorParam tsaVisitorParam) {
        return new MirrotTypeToTSConverterVisitor(theElement, tsaVisitorParam).visit(theElement.asType());
    }

    List<TSMember> getMembers() {
        return members.values().stream().
                filter((x) -> extractableMembers.contains(x.getName()))
                .collect(Collectors.toList());
    }
}
