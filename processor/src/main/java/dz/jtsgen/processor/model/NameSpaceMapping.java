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

import dz.jtsgen.processor.util.Tuple;

/**
 * just a simple representation of a name space mapping
 *
 * the empty string represents the root name space
 */
public final class NameSpaceMapping extends Tuple<String,String> {
    public NameSpaceMapping(String first, String second) {
        super(first==null ? "" : first, second==null? "" :second);
    }

    public String originNameSpace(){ return this.getFirst();}

    public String targetNameSpace(){ return this.getSecond();}

    @Override
    public String toString() {
        return this.originNameSpace() +" -> " + this.targetNameSpace();
    }
}
