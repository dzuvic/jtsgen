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

import dz.jtsgen.annotations.TSIgnore;
import dz.jtsgen.annotations.TypeScript;

@TypeScript
public interface InterFaceTestGenerics<T> {
    T getMyGeneric();
    MyPair<String, String> getMyPair();
}

class MyPair<U,V> {
    private final U u;
    private final V v;

    public U getU() { return u;}

    public V getV() { return v;}

    MyPair (U u, V v) {
        this.u = u;
        this.v = v;
    }
}
