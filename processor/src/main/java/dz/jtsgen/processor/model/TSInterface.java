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

package dz.jtsgen.processor.model;

import dz.jtsgen.processor.model.rendering.TSTypeVisitor;
import org.immutables.value.Value;

import java.util.List;


@Value.Immutable
public abstract class TSInterface extends TSType {
    
    @Override
    @Value.Derived
    public String getKeyword() {
        return "interface";
    }

    @Override
    public void accept(TSTypeVisitor visitor, int ident) {
        visitor.visit(this, ident);
    }

    @Override
    public TSType changedNamespace(String namespace, List<TSMember> members, List<TSMethod> methods, List<TSConstant> mappedConstants) {
        return TSInterfaceBuilder.copyOf(this).withNamespace(namespace).withMembers(members).withMethods(methods).withConstants(mappedConstants);
    }
}
