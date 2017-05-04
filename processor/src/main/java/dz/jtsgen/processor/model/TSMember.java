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

import dz.jtsgen.processor.model.rendering.TSMemberElement;
import dz.jtsgen.processor.model.rendering.TSMemberVisitor;
import dz.jtsgen.processor.model.tstarget.TSTargets;

public class TSMember implements TSMemberElement {
    public static final TSMember INVALD = new TSMember("invalid", TSTargets.NONE, true,true);

    private final String name;
    private final TSTargetType type;
    private final boolean readOnly;
    private final boolean invalid;

    private TSMember(String name, TSTargetType type, boolean readOnly, boolean invalid) {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.invalid = invalid;
    }

    public TSMember(String name, TSTargetType type, boolean readOnly) {
        this(name, type, readOnly, false);
    }

    public String getName() {
        return name;
    }

    public TSTargetType getType() {
        return type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isInvalid() {
        return invalid;
    }

    @Override
    public String toString(){
        StringBuilder builder=new StringBuilder();
        builder.append("TSMember{").append(name).append(":").append(type).append("}");
        return builder.toString();
    }

    @Override
    public void accept(TSMemberVisitor visitor) {
        visitor.visit(this);
    }
}
