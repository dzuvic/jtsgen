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

/**
 * A mapper that maps a namespace of string
 */
@FunctionalInterface
public interface NameSpaceMapper {

    /**
     * the first name space that matches the string, will be replaced with the mapped value, usually an empty string
     *
     * @param originNameSpace   the original string to be mapped
     * @return a new string with name space mapped.
     */
    String mapNameSpace(String originNameSpace);
}
