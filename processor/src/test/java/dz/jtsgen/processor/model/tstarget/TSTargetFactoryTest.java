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

import dz.jtsgen.processor.model.TSTargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.IDENTITY;
import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.createTSTargetByDSL;
import static org.junit.jupiter.api.Assertions.*;

class TSTargetFactoryTest {

    @Test
    void checkConstructor() {
        TSTargetFactory tf1 = new TSTargetFactory();
        TSTargetFactory tf2 = new TSTargetFactory();

        // check standard constructor
        assertNotEquals(tf1,tf2);
    }

    @Test
    @DisplayName("Check returning IDENTITY when calling TargetFactory")
    void checkIdentity() {
        Map<String, TSTargetType> emptyMap=new HashMap<>();
        Map<?,?> x1 =  TSTargetFactory.mapNamespaces(emptyMap, x -> x);
        Map<?,?> x2 =  TSTargetFactory.mapNamespaces(emptyMap, IDENTITY);
        Map<?,?> testee =  TSTargetFactory.mapNamespaces(emptyMap, IDENTITY);

        assertEquals(testee, x2);
        assertEquals(testee, x1);
    }

    @Test
    void test_createTSTargetByMapping_Type_Param() {
        createTSTargetByDSL("java.util.Collection<T> -> Array<T>").map(
            x -> {
                assertEquals(x.typeParameters(), Collections.singletonList("T"));
                assertEquals(x.getJavaType(),"java.util.Collection");
                assertTrue(x.isReferenceType());
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
                assertTrue(x.isReferenceType());
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
                assertTrue(x.isReferenceType());
                assertEquals(x.typeParameterTypes(), new HashMap<>());
                return x;
            }
        ).orElseThrow(AssertionError::new);
    }

}