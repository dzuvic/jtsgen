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

package dz.jtsgen.processor.util;

import java.util.function.Supplier;

public final class LilltleLazy {

    /**
     * simple lazy generator. the supplier may be called multiple times.
     *
     * @param supplier  the supplier to call
     * @param <T>  the return type
     * @return new supplier, that caches the result
     */
    public static <T> Supplier<T> lazy(Supplier<T> supplier) {

        // it's a anonymous class, because a lambda accesses the closure effective final. can not use arrays (raw type)
        // and holder is a new instance. so maybe this approach could be faster.
        return new Supplier<T>() {

            private T value=null;

            @Override public T get() {
                if (this.value == null) this.value = supplier.get();
                return this.value;
            }
        };
    }
}
