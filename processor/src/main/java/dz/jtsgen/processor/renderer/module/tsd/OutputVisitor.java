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

import javax.lang.model.element.Element;
import javax.lang.model.element.QualifiedNameable;
import java.io.PrintWriter;
import java.util.stream.Stream;

/**
 * This class handles mostly anything regarding the commnt and the output stream
 *
 * @author Dragan Zuvic
 */
public abstract class OutputVisitor {
    private final PrintWriter out;
    private final boolean printFullName;

    OutputVisitor(PrintWriter out, boolean printFullName) {
        this.out = out;
        this.printFullName = printFullName;
    }

    void tsComment(String comment, int ident) {
        tsComment(comment, ident, null);
    }

    void tsComment(String comment, int ident, Element element) {
        if (comment == null && !printFullName) return;

        if(comment == null) {
            comment = "";
        }

        if(printFullName && element instanceof QualifiedNameable) {

            comment += "\n@see " + ((QualifiedNameable)element).getQualifiedName().toString() + " <i>Original file</i>";
            comment = comment.trim();
        }

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
