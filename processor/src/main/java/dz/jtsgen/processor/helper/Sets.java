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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Some helper functions for Sets
 */
public final class Sets {

    /**
     *
     * @param first the first collection of the union set
     * @param second the second collection of the union set
     * @param <U> Type of the union elements
     * @return an unmodifiable union set
     */
    public static <U> Set<U> union(Collection<U> first, Collection<U> second) {
        final Set<U> result = new HashSet<U>();
        result.addAll(first);
        result.addAll(second);
        return Collections.unmodifiableSet(result);
    }
}
