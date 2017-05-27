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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Collection of stream related utility functions.
 */
public final class StreamUtils {

    private static <X, Y, Z> Stream<Z> zip(Stream<X> x, Stream<Y> y, BiFunction<X, Y, Z> f) {
        final Iterator<Y> iter = y.iterator();
        return x.filter( it -> iter.hasNext()).map(it -> f.apply(it, iter.next()));
    }

    /**
     * a simple zip function
     *
     * @param x first stream to zip
     * @param y second stream to zip
     * @param <X> type of the first stream
     * @param <Y> type of the second stream
     * @return a Stream of type Tuple&lt;X,Y&gt;
     */
    public static <X, Y> Stream<Tuple<X, Y>> zip(Stream<X> x, Stream<Y> y) {
        assert x != null && y != null;
        return zip(x, y, Tuple::new);
    }

    /**
     *
     * @param optionals functions returning optional
     * @param <T> common Type
     * @return the first result of the function not beeing empty
     */
    @SafeVarargs
    public static <T> Optional<T> firstOptional(Supplier<Optional<T>> ... optionals) {
        return Arrays.stream(optionals)
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .findFirst()
                .orElseGet(Optional::empty);
    }

}
