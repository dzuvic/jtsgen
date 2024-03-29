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

package jts.modules.enum_string_override;

import dz.jtsgen.annotations.TSConstant;
import dz.jtsgen.annotations.TSEnumConstant;

public enum SomeEnum {
    A,

    @TSConstant(name = "renamed_b")
    B,

    @TSEnumConstant(value = "value_c")
    C,

    @TSConstant(name = "X")
    @TSEnumConstant(value = "Y")
    D,

    @TSEnumConstant(value = InterfaceWithEnum.MY_CONSTANT)
    E
}

