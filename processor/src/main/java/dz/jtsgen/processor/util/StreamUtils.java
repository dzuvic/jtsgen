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

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Collection of stream related utility functions.
 */
public final class StreamUtils {
        private StreamUtils() {
        }

    private static <X, Y, Z> Stream<Z> zip(Stream<X> x, Stream<Y> y, BiFunction<X, Y, Z> f) {
        assert x != null && y != null;
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

}
