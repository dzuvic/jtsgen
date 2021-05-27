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

import dz.jtsgen.processor.model.rendering.TSMethodElement;
import dz.jtsgen.processor.model.rendering.TSMethodVisitor;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Optional;

@Value.Immutable
public abstract class TSMethodMember implements TSMethod {

    @Value.Parameter
    public abstract String getName() ;

    @Value.Parameter
    public abstract TSTargetType getType();

    public abstract Optional<String> getComment();

    @Value.Default
    public boolean getInvalid() {
        return false;
    }

    @Override
    public void accept(TSMethodVisitor visitor, int ident) {
        visitor.visit(this, ident);
    }

    @Override
    @Value.Parameter
    public abstract Map<String, TSTargetType> getArguments();

    @Override
    public void accept(TSMethodElement visitor, int ident) {
        visitor.accept(this, ident);
    }

    @Override
    public TSMethod changedTSTarget(TSTargetType newTargetType, Map<String, TSTargetType> newArguments) {
        return TSMethodMemberBuilder.copyOf(this).withType(newTargetType).withArguments(newArguments);
    }
}
