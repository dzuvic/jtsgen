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
import dz.jtsgen.processor.model.TSEnum;
import dz.jtsgen.processor.model.TSInterface;
import dz.jtsgen.processor.model.TSType;
import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import dz.jtsgen.processor.model.rendering.TSTypeVisitor;

import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * default renderer for TSTypes
 */
class DefaultTSTypeVisitor implements TSTypeVisitor {
    private final PrintWriter out;
    private final TSMemberVisitor tsMemberVisitor;

    DefaultTSTypeVisitor(PrintWriter out) {
        this.out = out;
        this.tsMemberVisitor = new DefaultTSMemberVisitor(out);
    }



    @Override
    public void visit(TSInterface x, int ident) {
        typePrefix(x, ident);
        x.getMembers().forEach(y -> {
            out.print(IdentHelper.identPrefix(ident + 1));
            y.accept(tsMemberVisitor);
        });
        typePostFix(ident);
    }

    @Override
    public void visit(TSEnum x, int ident) {
        typePrefix(x, ident);
        out.print(IdentHelper.identPrefix(ident + 1));
        final boolean[] isFirst = {true};
        x.getMembers().forEach(y -> {
            if (isFirst[0]) {
                isFirst[0] = false;
            } else {
                out.print(", ");
            }
            y.accept(tsMemberVisitor);
        });
        out.println("");
        typePostFix(ident);

    }

    private void typePostFix(int ident) {
        out.print(IdentHelper.identPrefix(ident));
        out.println("}");
        out.println("");
    }

    private void typePrefix(TSType x, int ident) {
        out.print(IdentHelper.identPrefix(ident));
        out.print("export ");
        out.print(x.getKeyword());
        out.print(" ");
        out.print(x.getName());
        out.print(extendsSuperTypes(x));
        out.println(" {");
    }

    private String extendsSuperTypes(TSType x) {
        if (x.getSuperTypes().size() == 0) return "";
        StringBuilder s = new StringBuilder();
        return s.append(" extends ").append( x.getSuperTypes().stream().map(TSType::getName).collect(Collectors.joining(",")))
                .toString();

    }
}
