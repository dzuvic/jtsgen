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
import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import dz.jtsgen.processor.model.rendering.TSTypeVisitor;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;

import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * default renderer for TSTypes
 */
class TSTypeVisitorDefault implements TSTypeVisitor {
    private final PrintWriter out;
    private final TSMemberVisitor tsMemberVisitor;
    private final TypeScriptRenderModel model;

    TSTypeVisitorDefault(PrintWriter out, TSMemberVisitor visitor, TypeScriptRenderModel model){
        this.out = out;
        this.tsMemberVisitor = visitor;
        this.model = model;
    }

    TSTypeVisitorDefault(PrintWriter out, TypeScriptRenderModel model) {
        this(out,new DefaultTSMemberVisitor(out, model), model);
    }



    @Override
    public void visit(TSInterface x, int ident) {
        tsComment(x, ident);
        typePrefix(x, ident);
        x.getMembers().forEach(y -> {
            getOut().print(IdentHelper.identPrefix(ident + 1));
            y.accept(getTsMemberVisitor());
        });
        typePostFix(ident);
    }

    @Override
    public void visit(TSEnum x, int ident) {
        typePrefix(x, ident);
        getOut().print(IdentHelper.identPrefix(ident + 1));
        final boolean[] isFirst = {true};
        x.getMembers().forEach(y -> {
            if (isFirst[0]) {
                isFirst[0] = false;
            } else {
                getOut().print(", ");
            }
            y.accept(getTsMemberVisitor());
        });
        getOut().println("");
        typePostFix(ident);

    }

    private void tsComment(TSType tsType, int ident) {
        tsType.getDocumentString().ifPresent(x -> {

            getOut().print(IdentHelper.identPrefix(ident));
            getOut().println("/**");

            getOut().print(IdentHelper.identPrefix(ident));
            getOut().print(" * ");
            getOut().println(x);


            getOut().print(IdentHelper.identPrefix(ident));
            getOut().println(" */");
        });

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

    PrintWriter getOut() {
        return out;
    }

    private TSMemberVisitor getTsMemberVisitor() {
        return tsMemberVisitor;
    }
}
