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

import dz.jtsgen.annotations.EnumExportStrategy;
import dz.jtsgen.processor.model.TSEnumMember;
import dz.jtsgen.processor.model.TSMember;
import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;

import java.io.PrintWriter;


public class DefaultTSMemberVisitor implements TSMemberVisitor {

    private final PrintWriter out;

    private final TypeScriptRenderModel model;

    DefaultTSMemberVisitor(PrintWriter out, TypeScriptRenderModel model) {
        this.out=out;
        this.model=model;
    }

    @Override
    public void visit(TSMember x) {
        if (x.getReadOnly()) out.print("readonly ");
        out.print(x.getName());
        out.print(": ");
        out.print(x.getType());
        out.println(";");
    }

    @Override
    public void visit(TSEnumMember x) {
        out.print(x.getName());
        if (EnumExportStrategy.STRING.equals(model.getEnumExportStrategy())) {
            out.print(" = '");
            out.print(x.getName());
            out.print("'");
        }
    }

}
