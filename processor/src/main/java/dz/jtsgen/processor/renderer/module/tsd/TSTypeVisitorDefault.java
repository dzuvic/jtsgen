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

package dz.jtsgen.processor.renderer.module.tsd;

import dz.jtsgen.processor.helper.IdentHelper;
import dz.jtsgen.processor.model.*;
import dz.jtsgen.processor.model.rendering.TSConstantVisitor;
import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import dz.jtsgen.processor.model.rendering.TSMethodVisitor;
import dz.jtsgen.processor.model.rendering.TSTypeVisitor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * default renderer for TSTypes
 */
class TSTypeVisitorDefault extends OutputVisitor implements TSTypeVisitor {

    private final TSMemberVisitor tsMemberVisitor;
    private final TSMethodVisitor tsMethodVisitor;
    private final TSConstantVisitor tsConstantVisitor;

    TSTypeVisitorDefault(PrintWriter out, TSMemberVisitor visitor, TSMethodVisitor visitor2, TSConstantVisitor visitor3, boolean printFullName){
        super(out, printFullName);
        this.tsMemberVisitor = visitor;
        this.tsMethodVisitor = visitor2;
        this.tsConstantVisitor = visitor3;
    }

    TSTypeVisitorDefault(PrintWriter out, boolean printFullName) {
        this(out, new DefaultTSMemberVisitor(out, printFullName), new DefaultTSMemberVisitor(out, printFullName), new DefaultTSConstantVisitor(out, printFullName), printFullName);
    }



    @Override
    public void visit(TSInterface x, int ident) {
        List<TSConstant> constants = new ArrayList<>(x.getConstants());
        constants.sort(Comparator.comparing(TSConstant::getName));
        constants.forEach(y -> y.accept(getTsConstantVisitor(), ident));

        tsComment(x.getDocumentString().orElse(null), ident, x.getElement());
        typePrefix(x, ident);

        List<TSMember> members = new ArrayList<>(x.getMembers());
        members.sort(Comparator.comparing(TSMember::getName));
        members.forEach(y -> y.accept(getTsMemberVisitor(), ident + 1));

        List<TSMethod> methods = new ArrayList<>(x.getMethods());
        methods.sort(Comparator.comparing(TSMethod::getName));
        methods.forEach(y -> y.accept(getTsMethodVisitor(), ident + 1));

        typePostFix(ident);
    }

    @Override
    public void visit(TSEnum x, int ident) {
        tsComment(x.getDocumentString().orElse(null), ident, x.getElement());
        typePrefix(x, ident);
        final boolean[] isFirst = {true};
        x.getMembers().forEach(y -> {
            if (isFirst[0]) {
                isFirst[0] = false;
            } else {
                getOut().println(",");
            }
            y.accept(getTsMemberVisitor(), ident + 1);
        });
        getOut().println("");
        typePostFix(ident);

    }

    @Override
    public void visit(TSFunction x, int ident) {
        TSMethod method = x.getMethods().get(0);
        String comment = "";

        if(x.getDocumentString().isPresent()) { // get the docu of the functional interface
            comment += x.getDocumentString().get();
        }
        if(method.getComment().isPresent()) { // get the docu of the only method of the functional interface.
            comment += "\n";
            comment += method.getComment().get();
        }

        tsComment(comment, ident, x.getElement());

        getOut().print(IdentHelper.identPrefix(ident));
        getOut().print("export ");
        getOut().print(x.getKeyword());
        getOut().print(" ");
        getOut().print(x.getName());
        if (!x.getTypeParams().isEmpty()) {
            getOut().print("<");
            getOut().print(x.getTypeParams().stream().map(this::nameWithBounds).collect(Collectors.joining(", ")));
            getOut().print("> ");
        }
        getOut().print(extendsSuperTypes(x));

        getOut().print(" = (");
        String args = method.getArguments()
                .entrySet()
                .stream()
                .map((entry) -> {
                    String result;

                    if(entry.getValue().isOptional()) {
                        result = entry.getKey() + "?: " + entry.getValue().toString();
                    }
                    else {
                        result = entry.getKey() + ": " + entry.getValue().toString();
                    }

                    if(entry.getValue().isNullable()) {
                        result += " | null";
                    }
                    return result;
                })
                .collect(Collectors.joining(", "));
        getOut().print(args);
        getOut().print(") => ");
        getOut().print(method.getType());
        if(method.getType().isNullable()) {
            getOut().append(" | null");
        }

        getOut().println(";");
    }


    private void typePostFix(int ident) {
        getOut().print(IdentHelper.identPrefix(ident));
        getOut().println("}");
        getOut().println("");
    }

    private void typePrefix(TSType x, int ident) {
        getOut().print(IdentHelper.identPrefix(ident));
        getOut().print("export ");
        getOut().print(x.getKeyword());
        getOut().print(" ");
        getOut().print(x.getName());
        if (!x.getTypeParams().isEmpty()) {
            getOut().print("<");
            getOut().print(x.getTypeParams().stream().map(this::nameWithBounds).collect(Collectors.joining(", ")));
            getOut().print("> ");
        }
        getOut().print(extendsSuperTypes(x));
        getOut().println(" {");
    }

    private String nameWithBounds(TSTypeVariable z) {
        return z.getName() +
                (z.getBounds().isEmpty() ? "" :
                    " extends " + z.getBounds().stream().map( x ->
                            x.fold( TSTargetType::toString,
                                    TSType::getName
                            )
                    ).collect(Collectors.joining(" & "))
                );
    }

    private String extendsSuperTypes(TSType x) {
        if (x.getSuperTypes().size() == 0) return "";
        StringBuilder s = new StringBuilder();
        return s.append(" extends ").append( x.getSuperTypes().stream().map(TSType::getName).collect(Collectors.joining(",")))
                .toString();

    }

    private TSMemberVisitor getTsMemberVisitor() {
        return tsMemberVisitor;
    }

    private TSMethodVisitor getTsMethodVisitor() {
        return tsMethodVisitor;
    }

    private TSConstantVisitor getTsConstantVisitor() {
        return tsConstantVisitor;
    }
}
