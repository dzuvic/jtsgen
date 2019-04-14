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
import dz.jtsgen.processor.model.TSInterface;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;

import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * default renderer for TSTypes
 */
class TSTypeVisitorTypeGuards extends TSTypeVisitorDefault {

    TSTypeVisitorTypeGuards(PrintWriter out, TypeScriptRenderModel model) {
        super(out, new DefaultTSMemberVisitor(out, model), model);
    }



    @Override
    public void visit(TSInterface x, int ident) {
        super.visit(x, ident);

        guardPrefix(x, ident);
        final String ident2 = IdentHelper.identPrefix(ident + 2);
        Stream<String> memberChecks = x.getMembers().stream()
                .map (y -> ident2 + "x." + y.getName() + " !== undefined");

        Stream<String> superTypeChecks = x.getSuperTypes().stream()
                .map (y -> ident2 + "instanceOf" + y.getName() + "(x) === true");

        final String joinedArgs = Stream.concat(superTypeChecks, memberChecks)
                .collect(Collectors.joining(" &&" + System.lineSeparator()));
        getOut().println(joinedArgs + ";");
        guardPostFix(ident);

    }

    private void guardPrefix(TSInterface x, int ident) {
        getOut().println("");
        getOut().print(IdentHelper.identPrefix(ident));
        getOut().println("export function instanceOf" + x.getName() + "(x: any): x is " + x.getName() + " {");
        getOut().print(IdentHelper.identPrefix(ident + 1));
        getOut().println("return x" + ( hasMembersOrSuperTyes(x) ? " &&" : " && true") );

    }

    private boolean hasMembersOrSuperTyes(TSInterface x) {
        return x.getMembers().size() > 0 || (! x.getSuperTypes().isEmpty());
    }

    private void guardPostFix(int ident) {
        getOut().print(IdentHelper.identPrefix(ident));
        getOut().println("}");
        getOut().println("");
    }

}
