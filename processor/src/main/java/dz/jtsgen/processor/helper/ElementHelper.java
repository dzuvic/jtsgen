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

package dz.jtsgen.processor.helper;

import javax.lang.model.element.TypeElement;

public final class ElementHelper {

    /**
     *
     * @param typeElement the type element to check
     * @param clazz the type the element has to be
     * @return true if type element is represented by class clazz
     */
    public static boolean typeOf(TypeElement typeElement, Class<?> clazz) {

        // this should be done in different way, but checking the name should be sufficient
        return typeElement.getSimpleName().contentEquals(clazz.getSimpleName());
    }
}
