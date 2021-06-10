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
import dz.jtsgen.processor.model.TSEnumMember;
import dz.jtsgen.processor.model.TSMember;
import dz.jtsgen.processor.model.TSMethodMember;
import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import dz.jtsgen.processor.model.rendering.TSMethodVisitor;

import java.io.PrintWriter;
import java.util.stream.Collectors;


public class DefaultTSMemberVisitor extends OutputVisitor implements TSMemberVisitor, TSMethodVisitor {


    DefaultTSMemberVisitor(PrintWriter out, boolean printFullName) {
        super(out, printFullName);
    }

    @Override
    public void visit(TSMember x, int ident) {
        x.getComment().ifPresent( comment -> tsComment(comment,ident));
        getOut().print(IdentHelper.identPrefix(ident));
        if (x.getReadOnly()) {
            getOut().print("readonly ");
        }
        getOut().print(x.getName());
        if(x.getOptional()) {
            getOut().print("?");
        }
        getOut().print(": ");
        getOut().print(x.getType());
        if(x.getNullable()) {
            getOut().print(" | null");
        }
        getOut().println(";");
    }

    @Override
    public void visit(TSEnumMember x, int ident) {
        x.getComment().ifPresent( comment -> tsComment(comment,ident));
        getOut().print(IdentHelper.identPrefix(ident));
        if ( x.getExportStrategyStringRepresentation().isPresent()) {
            getOut().print(x.getName());
            getOut().print(" = '");
            getOut().print(x.getExportStrategyStringRepresentation().get());
            getOut().print("'");
        } else {
            getOut().print(x.getName());
        }
    }

    @Override
    public void visit(TSMethodMember x, int ident) {
        x.getComment().ifPresent( comment -> tsComment(comment,ident));
        getOut().print(IdentHelper.identPrefix(ident));
        getOut().print(x.getName());
        if (!x.getTypeParams().isEmpty()) {
            getOut().print("<");
            getOut().print(x.getTypeParams().stream().map(TSTypeVisitorDefault::nameWithBounds).collect(Collectors.joining(", ")));
            getOut().print(">");
        }
        getOut().print("(");
        String args = x.getArguments()
                .entrySet()
                .stream()
                .map((entry) -> {
                    String arg;

                    if(entry.getValue().isOptional()) {
                        arg = entry.getKey() + "?: " + entry.getValue().toString();
                    }
                    else {
                        arg = entry.getKey() + ": " + entry.getValue().toString();
                    }
                    if(entry.getValue().isNullable()) {
                        arg += " | null";
                    }
                    return arg;
                })
                .collect(Collectors.joining(", "));
        getOut().print(args);
        getOut().print("): ");
        getOut().print(x.getType());
        if(x.getType().isNullable()) {
            getOut().print(" | null");
        }
        getOut().println(";");
    }
}
