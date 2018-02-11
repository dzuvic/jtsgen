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

package dz.jtsgen.processor.model.tstarget; 

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.createTSTargetByDSL;
import static org.junit.jupiter.api.Assertions.*;

class TSTargetFactoryTest {
    @Test
    void test_createTSTargetByMapping_Type_Param() {
        createTSTargetByDSL("java.util.Collection<T> -> Array<T>").map(
            x -> {
                assertEquals(x.typeParameters(), Collections.singletonList("T"));
                assertEquals(x.getJavaType(),"java.util.Collection");
                assertEquals(x.isReferenceType(),true);
                assertEquals(x.typeParameterTypes(), new HashMap<>());
                return x;
            }
        ).orElseThrow(AssertionError::new);
    }

    @Test
    void test_createTSTargetByMapping_Type_TS_Literal() {
        createTSTargetByDSL("java.util.List<T> -> `T`[]").map(
            x -> {
                assertEquals(x.typeParameters(), Collections.singletonList("T"));
                assertEquals(x.getJavaType(),"java.util.List");
                assertEquals(x.isReferenceType(),true);
                assertEquals(x.typeParameterTypes(), new HashMap<>());
                return x;
            }
        ).orElseThrow(AssertionError::new);
    }

    @Test
    void test_createTSTargetByMapping_Two_Types_Param() {
        createTSTargetByDSL("java.util.Map<K,V> -> Map<K,V>").map(
            x -> {
                assertEquals(x.typeParameters(), Arrays.asList("K","V"));
                assertEquals(x.getJavaType(),"java.util.Map");
                assertEquals(x.isReferenceType(),true);
                assertEquals(x.typeParameterTypes(), new HashMap<>());
                return x;
            }
        ).orElseThrow(AssertionError::new);
    }

}