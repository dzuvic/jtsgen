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
import com.google.testing.compile.JavaFileObjects;
import dz.jtsgen.processor.TsGenProcessor;
import org.junit.Assert;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.util.Arrays;

import static com.google.testing.compile.Compiler.javac;
import static dz.jtsgen.processor.helper.StringConstForTest.JTS_DEV;
import static dz.jtsgen.processor.helper.StringConstForTest.JTS_DEV_D_TS;
import static dz.jtsgen.processor.helper.StringConstForTest.PACKAGE_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Some Compile Helper for testing
 */
public class CompileHelper {

    /** small helper for compiling several java files in an unified way */
    public static Compilation compileJtsDev(boolean debugLog, int warningCount, String... fileNames) {
        return compileForModule("jts/dev", debugLog, warningCount, fileNames);
    }

    /** small helper for compiling several java files in an unified way for a specific module */
    public static Compilation compileForModule(String packageDir, boolean debugLog, int warningCount, String... fileNames) {
        assert fileNames != null;
        JavaFileObject[] files = Arrays.stream(fileNames).map((x) -> JavaFileObjects.forResource(packageDir + "/" +x)).toArray(JavaFileObject[]::new);
        Compilation c =  javac()
                .withProcessors(new TsGenProcessor())
                .withOptions(debugLog ? new Object[]{"-AjtsgenLogLevel=FINEST"} : new Object[]{})
                .compile(files);

        if (c.errors().size()!=0) {
            c.errors().forEach(System.err::println);
            fail("No Compilation error should occur");
        }

        if (c.warnings().size()!= warningCount) {
            c.warnings().forEach(System.err::println);
            fail("Warnings count differ "+warningCount+" (expected) !="+c.warnings().size());
        }

        assertEquals(c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.WARNING)).count(), warningCount);
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV, PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV, JTS_DEV_D_TS).isPresent());

        return c;
    }
}
