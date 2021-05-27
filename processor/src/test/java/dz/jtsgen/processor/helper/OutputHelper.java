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

import com.google.testing.compile.Compilation;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Some Helper regarding the Test Output
 */
public class OutputHelper {

    public static List<String> findSourceLine(Compilation c, String packageName, String fileName, Pattern pattern) throws IOException {
        Assert.assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT,packageName, fileName).isPresent());
        JavaFileObject jfo = c.generatedFile(StandardLocation.SOURCE_OUTPUT,packageName, fileName).get();
        return findSourceLine(jfo, pattern);
    }

    public static List<String> findSourceLine(JavaFileObject jfo, Pattern pattern) throws IOException {
        try (Reader r = jfo.openReader(false)) {
            return IOUtils.readLines(r).stream().filter( (x) -> pattern.matcher(x).find()).collect(Collectors.toList());
        }
    }

    public static int countPatterns(Compilation c, String packageName, String fileName, Pattern pattern) throws IOException {
        return findSourceLine(c,packageName, fileName, pattern).size();
    }
}
