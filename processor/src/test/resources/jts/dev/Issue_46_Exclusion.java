/*
 * Copyright (c) 2018 Dragan Zuvic
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

// Generating bogus Java types due to not aborting the converter recursion
// The following will lead to generating a lot of not needed Java Types:
// Test checks, if Marker interfaces are discarded
@TypeScript
public interface Issue_46_Exclusion<T extends Comparable> {
    T getUpperBound();
}