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
public class MemberTestObject {
    
    private int member_private=0;
    protected int member_protected=0;
    int member_package_protected=0;
    public int member_public=0;

    private int x_with_getter_only=0;
    private int x_with_setter_only=0;
    private int x_with_getter_setter=0;

    public int getX_with_getter_only() {
        return x_with_getter_only;
    }

    public void setX_with_setter_only(int x_with_setter_only) {
        this.x_with_setter_only = x_with_setter_only;
    }

    public int getX_with_getter_setter() {
        return x_with_getter_setter;
    }

    public void setX_with_getter_setter(int x_with_getter_setter) {
        this.x_with_getter_setter = x_with_getter_setter;
    }

    private int y_with_getter_private=0;

    private int getY_with_getter_private() {
        return y_with_getter_private;
    }
}
