/*
 * Copyright (c) 2021 Dominik Scholl
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
import dz.jtsgen.processor.model.TSConstant;
import dz.jtsgen.processor.model.rendering.TSConstantVisitor;

import java.io.PrintWriter;

public final class DefaultTSConstantVisitor extends OutputVisitor implements TSConstantVisitor {

    DefaultTSConstantVisitor(PrintWriter out, boolean printFullName) {
        super(out, printFullName);
    }

    @Override
    public void visit(TSConstant x, int ident) {
        x.getComment().ifPresent( comment -> tsComment(comment, ident));

        Object constantValue = x.getConstantValue();

        if(constantValue instanceof String) {
            constantValue = "\"" + constantValue + "\"";
        }

        PrintWriter out = getOut();

        out.print(IdentHelper.identPrefix(ident));
        out.print("export const ");
        out.print(x.getName());
        out.print(":");
        out.print(x.getType());
        out.print(" = ");
        out.print(constantValue);
        out.println(";");

    }
}
