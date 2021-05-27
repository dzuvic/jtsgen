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


import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import dz.jtsgen.processor.model.tstarget.TSTargetEnumValueType;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public abstract class TSEnumMember implements TSMember {

    private static final TSTargetType ENUM_MEMBER = new TSTargetEnumValueType();

    @Value.Parameter
    public abstract String getName();

    /**
     * @return the string representation of the enum. By default, it's the enum name. This is field is only used,
     * when EnumExportStrategy is STRING
     */
    public abstract Optional<String> getExportStrategyStringRepresentation();


    @Override
    @Value.Derived
    public TSTargetType getType() {
        return ENUM_MEMBER;
    }

    @Value.Default
    public boolean getInvalid() {
        return false;
    }

    @Value.Default
    public boolean getReadOnly() {
        return false;
    }

    @Value.Default
    public boolean getNullable() {
        return false;
    }
    @Value.Default
    public boolean getOptional() {
        return false;
    }

    @Override
    public void accept(TSMemberVisitor visitor, int ident) {
        visitor.visit(this, ident);
    }

    @Override
    public TSMember changedTSTarget(TSTargetType newTargetType) {
        return this;
    }
}
