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
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.Assert.*;

public class EitherTest {


    @Test
    public void either_isLeft_isRight() throws Exception {
        Assert.assertTrue( Either.left(1).isLeft());
        Assert.assertFalse( Either.left(1).isRight());
        Assert.assertTrue( Either.right(1).isRight());
        Assert.assertFalse( Either.right(1).isLeft());
    }

    @Test
    public void either_values() throws Exception {
        assertEquals(Either.left("a").leftValue(),"a");
        assertEquals(Either.right("a").value(),"a");
    }

    @Test(expected = IllegalStateException.class)
    public void either_values_err1() throws Exception {
        Either.left("a").value();
    }

    @Test(expected = IllegalStateException.class)
    public void either_values_err2() throws Exception {
        Either.right("a").leftValue();
    }


    @Test(expected = NoSuchElementException.class)
    public void either_iterator_error() throws Exception {
        Assert.assertFalse(Either.left("20").iterator().hasNext());
        Either.left("20").iterator().next();
    }

    @Test
    public void iterator() throws Exception {
        Assert.assertTrue(Either.right("20").iterator().hasNext());
        assertEquals(Either.right("20").iterator().next(),"20");
    }

    @Test
    public void either_forEach() throws Exception {
        Either.right("20").forEach( x -> assertEquals(x,"20"));
    }

    @Test
    public void either_fold() throws Exception {
        assertEquals( Either.right("20").fold(
                s -> 0,
                Integer::valueOf
        ), Integer.valueOf(20) );

        assertEquals( Either.<String,String>left("20").fold(
                s -> (0),
                Integer::valueOf
        ), Integer.valueOf(0) );
    }

    @Test(expected = NullPointerException.class)
    public void either_notnull_1() throws Exception {
        Either.left("0").fold(null,null);
    }

    @Test(expected = NullPointerException.class)
     public void either_notnull_2() throws Exception {
         Either.left("0").fold(x->0,null);
     }


    @Test(expected = NotImplented.class)
    public void spliterator() throws Exception {
        Either.right("20").spliterator();
    }

}