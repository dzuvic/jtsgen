/*
 * Copyright 2016 Dragan Zuvic
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

import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public abstract class TSRegularMember implements TSMember {

    @Value.Parameter
    public abstract String getName() ;

    @Value.Parameter
    public abstract TSTargetType getType();

    @Value.Parameter
    public abstract boolean getReadOnly();

    public abstract Optional<String> getComment();

    @Value.Default
    public boolean getInvalid() {
        return false;
    }

    @Override
    public void accept(TSMemberVisitor visitor, int ident) {
        visitor.visit(this, ident);
    }

    @Override
    public TSMember changedTSTarget(TSTargetType newTargetType) {
        return TSRegularMemberBuilder.copyOf(this).withType(newTargetType);
    }
}
