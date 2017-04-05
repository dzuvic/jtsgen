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

import dz.jtsgen.processor.model.TSMember;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.helper.ExecutableElementHelper.isGetter;
import static dz.jtsgen.processor.helper.ExecutableElementHelper.isGetterOrSetter;

/**
 * processes the Elements of an java interface or class.
 * <p>
 * because of beans this Visitor is stateful: return type is void.
 */
class TSElementVisitor extends SimpleElementVisitor8<Void, TSAVisitorParam> {

    private static Logger LOG = Logger.getLogger(TSElementVisitor.class.getName());

    private final TSMemberVisitor tsMemberVisitor;

    private final MirrotTypeToTSConverterVisitor mirrotTypeToTSKonverterVisitor;

    private final Map<String, TSMember> members = new HashMap<>();

    // list of members to sort out setters only
    private final Set<String> extractableMembers = new HashSet<>();


    public TSElementVisitor() {
        this.tsMemberVisitor = new TSMemberVisitor();
        this.mirrotTypeToTSKonverterVisitor = new MirrotTypeToTSConverterVisitor();
    }

    @Override
    public Void visitType(TypeElement e, TSAVisitorParam tsaVisitorParam) {
        LOG.log(Level.FINEST, () -> String.format("TSEV visiting type %s", e.toString()));
        TSMember member = tsMemberVisitor.visit(e.asType(), tsaVisitorParam);
        this.members.put(member.getName(), member);
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, TSAVisitorParam tsaVisitorParam) {
        LOG.log(Level.FINEST, () -> String.format("TSEV visiting variable %s", e.toString()));
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableElement e, TSAVisitorParam tsaVisitorParam) {
        LOG.fine( () -> String.format("TSEV visiting executable %s", e.toString()));
        if (isGetterOrSetter(e)) {
            LOG.finest(() -> "is getter or setter: " + e.getSimpleName());
            final String name = nameFromMethod(e.getSimpleName().toString());
            final String tsType =  convertTypeMirrorToTsType(e.getReturnType(), tsaVisitorParam);
            if (members.containsKey(name)) {
                // can't be read only anymore
                members.put(name, new TSMember(name, tsType, false));
            } else {
                members.put(name, new TSMember(name, tsType, isGetter(e)));
            }
            if (isGetter(e)) extractableMembers.add(name);
        }
        return null;
    }

    private String nameFromMethod(String s) {
        assert isGetterOrSetter(s);
        String nameWithoutGetSet = s.contains("get")?s.replaceFirst("get",""):s.replaceFirst("set","");
        return Character.toLowerCase(nameWithoutGetSet.charAt(0)) + nameWithoutGetSet.substring(1);
    }

    private String convertTypeMirrorToTsType(TypeMirror typeMirror, TSAVisitorParam tsaVisitorParam) {
        return typeMirror.accept(this.mirrotTypeToTSKonverterVisitor, tsaVisitorParam);

    }

    List<TSMember> getMembers() {
        return members.values().stream().
                filter((x) -> extractableMembers.contains(x.getName()))
                .collect(Collectors.toList());
    }
}
