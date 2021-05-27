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

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Helper for creating the reference streams and / or files
 */
public class ReferenceHelper {
    public static InputStream createReferenceStreamFor(String resourcename) {
        return ReferenceHelper.class.getClassLoader().getResourceAsStream("reference/"+resourcename);
    }

    public static void assertEquals(JavaFileObject testeeFile, String referenceFile) throws IOException {
        String testee  = IOUtils.toString(testeeFile.openReader(true)).replace("\r\n", "\n");
        String reference = IOUtils.toString( createReferenceStreamFor(referenceFile), StandardCharsets.UTF_8).replace("\r\n", "\n");
        Assert.assertEquals(reference, testee);
    }
}
