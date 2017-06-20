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

package dz.jtsgen.processor.jtp.visitors;

import dz.jtsgen.processor.model.TSType;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Simple visitor that calls a JavaTypeConverter */
public class TSAVisitor extends SimpleElementVisitor8<Optional<TSType>, JavaTypeConverter> {

    private static Logger LOG = Logger.getLogger(TSAVisitor.class.getName());

    public TSAVisitor() {
        super(Optional.empty());
    }

    @Override
    public Optional<TSType> visitType(TypeElement e, JavaTypeConverter param) {
        LOG.log(Level.FINER,() -> String.format("visitType %s", e.toString() ));
        return param.convertJavaType(e);
    }
}
