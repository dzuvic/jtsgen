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

/**
 * Test inheritance across sub packages
 */
@TSModule(
        moduleName = "enum_string_override_test",
        enumExportStrategy = EnumExportStrategy.STRING
)
package jts.modules.enum_string_override;

import dz.jtsgen.annotations.EnumExportStrategy;
import dz.jtsgen.annotations.TSModule;
