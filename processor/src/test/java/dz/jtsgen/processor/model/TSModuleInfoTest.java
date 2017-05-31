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

package dz.jtsgen.processor.model;

import dz.jtsgen.annotations.OutputType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TSModuleInfoTest {

    @Test
    public void tsmodule_equals() throws Exception {
        TSModuleInfo same=new TSModuleInfo("a","b");
        assertTrue(same.equals(same));
        assertFalse(same.equals("hello"));
        assertEquals(new TSModuleInfo("a","p"), new TSModuleInfo("a","p"));
        assertNotEquals(new TSModuleInfo("a","p"), new TSModuleInfo("x","p"));
        assertNotEquals(new TSModuleInfo("a","p"), new TSModuleInfo("a","x"));

        final TSModuleInfo apModule = new TSModuleInfo("a", "p");
        assertEquals(apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE)
                , apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE));

        assertNotEquals(apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE)
                      , apModule.withModuleData("x","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE));

        assertNotEquals(apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE)
                      , apModule.withModuleData("a","x","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE));

        assertNotEquals(apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE)
                      , apModule.withModuleData("a","b","x","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE));

        assertNotEquals(apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE)
                      , apModule.withModuleData("a","b","c","x","d","f", OutputType.EXTERNAL_NAMESPACE_FILE));

        assertNotEquals(apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE)
                      , apModule.withModuleData("a","b","c","c","x","x", OutputType.EXTERNAL_NAMESPACE_FILE));
        

        assertNotEquals(apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_FILE)
                , apModule.withModuleData("a","b","c","c","d","f", OutputType.EXTERNAL_NAMESPACE_AMBIENT_TYPE));
    }

    @Test
    public void tsmoduleNullables() throws Exception {
        TSModuleInfo testee=new TSModuleInfo("a","b").withModuleData(null,null,null,null,null,null,null);
        assertEquals("unknown", testee.getModuleAuthor());
        assertEquals("unknown", testee.getModuleAuthorUrl());
        assertEquals("unknown", testee.getModuleLicense());
        assertEquals("1.0.0", testee.getModuleVersion());
    }

    @Test(expected = AssertionError.class)
    public void tsPackageFriendly() throws Exception {
        new TSModuleInfo("-a","-a");

    }

    @Test(expected = AssertionError.class)
    public void check_packagefriendly_assertion() throws Exception {
        new TSModuleInfo("-a","-b");
    }
}