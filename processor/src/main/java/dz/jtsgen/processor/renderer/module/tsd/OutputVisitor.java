/*
 * Copyright (c) 2019 Dragan Zuvic
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

import java.io.PrintWriter;
import java.util.stream.Stream;

/**
 * This class handles mostly anything regarding the commnt and the output stream
 *
 * @author Dragan Zuvic
 */
public abstract class OutputVisitor {
    private final PrintWriter out;

    OutputVisitor(PrintWriter out) {
        this.out = out;
    }

    void tsComment(String comment, int ident) {
        if (comment == null) return;


        getOut().print(IdentHelper.identPrefix(ident));
        getOut().println("/**");

        Stream.of(comment.split("\\r?\\n")).forEach(y -> {
            getOut().print(IdentHelper.identPrefix(ident));
            getOut().print(" * ");
            getOut().print(y);
            getOut().println("");
        });

        getOut().print(IdentHelper.identPrefix(ident));
        getOut().println(" */");

    }

    protected PrintWriter getOut() {
        return out;
    }
}
