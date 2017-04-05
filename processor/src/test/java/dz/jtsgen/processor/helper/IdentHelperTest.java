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

package dz.jtsgen.processor.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdentHelperTest {
    @Test
    public void identPrefix() throws Exception {
        assertEquals(IdentHelper.identPrefix(-1),"");
        assertEquals(IdentHelper.identPrefix(0),"");
        assertEquals(IdentHelper.identPrefix(1),"  ");
        assertEquals(IdentHelper.identPrefix(2),"    ");
        assertEquals(IdentHelper.identPrefix(10),"                    ");
        assertEquals(IdentHelper.identPrefix(11),"                      ");
        assertEquals(IdentHelper.identPrefix(12),"                        ");
        assertEquals(IdentHelper.identPrefix(13),"                          ");
        assertEquals(IdentHelper.identPrefix(14),"                            ");
        assertEquals(IdentHelper.identPrefix(20),"                                        ");

    }

}