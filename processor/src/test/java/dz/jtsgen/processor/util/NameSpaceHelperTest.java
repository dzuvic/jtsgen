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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dz.jtsgen.processor.nsmap.NameSpaceHelper;
import org.junit.Test;

import static org.junit.Assert.*;

public class NameSpaceHelperTest {
    @Test
    public void topPackages() throws Exception {
        assertEquals(
                Sets.newHashSet("a.b.c"),
                NameSpaceHelper.topPackages(Lists.newArrayList("a.b.c","a.b.c.d.e.f","a.b.c.d.g"))
        );

        assertEquals(
                Sets.newHashSet("a.b.c", "u.v.w"),
                NameSpaceHelper.topPackages(Lists.newArrayList("a.b.c","a.b.c.d.e.f","a.b.c.d.g", "u.v.w","u.v.w.x","u.v.w.x.y"))
        );

        // the following is not quite right. it SHOULD emit smthg like a.b and u.v.w, but the current solution is sufficient.
        assertEquals(
                Sets.newHashSet("a.b"),
                NameSpaceHelper.topPackages(Lists.newArrayList("a.b", "a.b.c","a.b.c.d.e.f","a.b.c.d.g", "u.v.w","u.v.w.x","u.v.w.x.y"))
        );
    }

}