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

package jts.dev;

import dz.jtsgen.annotations.TypeScript;

@TypeScript
public class MemberDefaultMappingTest {
    
    public int  member_int=0;
    public long member_long=0;
    public short member_short=0;
    public double member_double=0;
    public float member_float=0;
    public char member_char=0;
    public boolean member_boolean=true;

    public Object member_Object=null;
    public Boolean member_Boolean=null;
    public Integer member_Integer=null;
    public Long member_o_Long=null;
    public Float member_o_float=null;
    public Double member_o_double=null;
    public Short member_o_short=null;
    public Character member_o_character=null;
    
}
