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

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.createTSTargetByMapping;
import static org.junit.Assert.*;

public class TSTargetFactoryTest {
    @Test
    public void test_createTSTargetByMapping_Type_Param() throws Exception {
        createTSTargetByMapping("java.util.Collection<T> -> Array<T>").map(
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
    public void test_createTSTargetByMapping_Type_TS_Literal() throws Exception {
        createTSTargetByMapping("java.util.Collection<T> -> `T`[]").map(
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
    public void test_createTSTargetByMapping_Two_Types_Param() throws Exception {
        createTSTargetByMapping("java.util.Map<K,V> -> Map<K,V>").map(
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