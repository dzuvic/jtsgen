/*
 * Copyright (c) 2021 Dominik Scholl
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

package jts.modules.constants;

import dz.jtsgen.annotations.*;

@TypeScript
public class ConstantsTest {

    /**
     * My string comment
     */
    @TSConstant(name="stringTest")
    public final String STRING_TEST = "asdf";

    @TSConstant
    public static final int INT_TEST = 47;

    @TSConstant
    public final boolean BOOL_TEST = true;

    @TSConstant
    private final long longTest = 13L;

    // No annotations
    public static final String ignored = "ignored";

    @TSIgnore
    @TSConstant
    public static final String ignoredToo = "ignored";

    @TSConstant
    public String ignoredNotFinal = "ignored";

    /**
     * Supported as field, but not as constant in interfaces
     */
    @TSConstant(name="typeTest")
    public static final FooType TYPE_TEST = new FooType();

}
