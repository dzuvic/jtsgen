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

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EitherTest {


    @Test
    void either_isLeft_isRight() {
        Assert.assertTrue( Either.left(1).isLeft());
        Assert.assertFalse( Either.left(1).isRight());
        Assert.assertTrue( Either.right(1).isRight());
        Assert.assertFalse( Either.right(1).isLeft());
    }

    @Test
    void either_values() {
        assertEquals(Either.left("a").leftValue(),"a");
        assertEquals(Either.right("a").value(),"a");
    }

    @Test
    void either_values_err1() {
        assertThrows(IllegalStateException.class, () -> Either.left("a").value());
    }

    @Test
    void either_values_err2() {
        assertThrows(IllegalStateException.class, () -> Either.right("a").leftValue() );
    }


    @Test
    void either_iterator_error() {
        Assert.assertFalse(Either.left("20").iterator().hasNext());
        assertThrows(NoSuchElementException.class,
                () -> Either.left("20").iterator().next()
        );
    }

    @Test
    void iterator() {
        Assert.assertTrue(Either.right("20").iterator().hasNext());
        assertEquals(Either.right("20").iterator().next(),"20");
    }

    @Test
    void either_idPresent() {
        Either.right("20").ifPresent(x -> assertEquals(x,"20"));
        Either.left("20").ifPresent(x -> fail("should not be called"));
    }

    @Test
    void test_flatmap() {
        assertEquals(
                Either.right(20),
                Either.right("20").flatMap( x -> Either.right(Integer.parseInt(x)))
                );

        assertEquals(
                Either.left("20"),
                Either.left("20").flatMap( x -> {
                    fail("must not be called");
                    return null;
                }));
    }

    @Test
    void either_toString() {
        assertEquals(Either.right("20").toString(),"Right{value=20}");
        assertEquals(Either.left("20").toString(),"Left{value=20}");
    }

    @Test
    void either_equals_and_hashcode() {
        Either<String,String> x0= Either.right("20");
        Either<String,String> x1= Either.right("20");
        Either<String,String> x2= Either.right("21");
        Either<String,String> y0= Either.left("20");
        Either<String,String> y1= Either.left("20");
        Either<String,String> y2= Either.left("21");

        assertTrue( x0.equals(x1));
        assertTrue( y0.equals(y1));
        assertEquals(x0.hashCode(),x1.hashCode());
        assertEquals(y0.hashCode(),y1.hashCode());

        assertFalse(x0.equals(null));
        assertFalse(y0.equals(null));
        assertFalse(x0.equals(""));
        assertFalse(y0.equals(""));
        assertFalse(x0.equals(x2));
        assertFalse(y0.equals(y2));
    }

    @Test
    void either_splitterator_fails() {
        assertThrows(
                dz.jtsgen.processor.util.NotImplented.class,
                () -> Either.right("20").spliterator()
        );
    }

    @Test
    void test_orNull() {
        assertNull(Either.right("20").leftOrNull());
        assertNull(Either.left("20").rightOrNull());
        assertEquals(
                "20",
                Either.right("20").rightOrNull()
        );
        assertEquals(
                "20",
                Either.left("20").leftOrNull()
        );
    }

    @Test
    void test_checkOrLeft() {
        assertEquals(
                        "20",
                        Either.left("20").checkOrLeft( "21", x->true)
                );

        assertEquals(
                        "21",
                        Either.right("20").checkOrLeft( "21", x->false)
                );
    }

    @Test
    void either_or_else_get() {
        assertEquals("20", Either.right("20").orElse("21"));
        assertEquals("20", Either.right("20").orElseGet( () -> "21"));
        assertEquals("20", Either.right("20").orElseThrow( () -> new IllegalArgumentException("wrong")));
        assertEquals("21", Either.left("20").orElse("21"));
        assertEquals("21", Either.left("20").orElseGet(() -> "21"));
    }

    @Test
    void either_or_else_get_fail() {
        assertThrows(IllegalArgumentException.class,
                () ->  Either.left("20").orElseThrow( () -> new IllegalArgumentException("wrong"))
        );
    }

    @Test
    void either_fold() {
        assertEquals( Either.right("20").fold(
                s -> 0,
                Integer::valueOf
        ), Integer.valueOf(20) );

        assertEquals( Either.<String,String>left("20").fold(
                s -> (0),
                Integer::valueOf
        ), Integer.valueOf(0) );
    }

    @Test
    void either_notnull_1() {
        assertThrows(NullPointerException.class,
                () -> Either.left("0").fold(null, null)
        );
    }

    @Test
    void either_notnull_2() {
        assertThrows(
                NullPointerException.class,
                () -> Either.left("0").fold(x->0,null)
        );
     }


    @Test
    void spliterator() {
        assertThrows(NotImplented.class,
                () -> Either.right("20").spliterator()
        );
    }

}