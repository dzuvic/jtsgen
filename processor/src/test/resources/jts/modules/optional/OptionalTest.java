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

package jts.modules.optional;

import dz.jtsgen.annotations.*;

import dz.jtsgen.processor.test.Nullable;

@TypeScript
public class OptionalTest {

//    @TSProperty(name = "getRegularField")
    public Object a_regularField;

    @TSOptional
//    @TSProperty(name = "getOptionalField")
    public Object b_optionalField;

    @TSOptional
    @Nullable
//    @TSProperty(name = "getOptilnalNullableField")
    public Object c_optionalNullableField;


    // no method, a property
    @TSReadOnly
    public Object getD_RegularMethodProperty() {
        return null;
    }

    // no method, a property
    @TSOptional
    @TSReadOnly
    public Object getE_OptionalMethodProperty() {
        return null;
    }

    // no method, a property
    @TSOptional
    @Nullable
    @TSReadOnly
    public Object getF_OptionalNullableMethodProperty() {
        return null;
    }

    @TSOptional // should have no effect
    @Nullable
    @TSMethod
    public Object g_calcStuffRegular(Object optionalParam) {
        return null;
    }

    @TSOptional // should have no effect
    @Nullable
    @TSMethod
    public Object h_calcStuffOptional(@TSOptional Object optionalParam) {
        return null;
    }

    @TSOptional // should have no effect
    @Nullable
    @TSMethod
    public Object i_calcStuffOptionalNullable(@TSOptional @Nullable Object optionalParam) {
        return null;
    }


}