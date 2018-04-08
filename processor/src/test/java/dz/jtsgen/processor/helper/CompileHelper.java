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

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.util.Arrays;

import static com.google.testing.compile.Compiler.javac;
import static dz.jtsgen.processor.helper.StringConstForTest.JTS_DEV;
import static dz.jtsgen.processor.helper.StringConstForTest.JTS_DEV_D_TS;
import static dz.jtsgen.processor.helper.StringConstForTest.PACKAGE_JSON;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Some Compile Helper for testing
 */
public class CompileHelper {

    /** small helper for compiling several java files in an unified way */
    public static Compilation compileJtsDev(boolean debugLog, int warningCount, String... fileNames) {
        return compileForModule("jts/dev", JTS_DEV, JTS_DEV_D_TS, debugLog, warningCount, fileNames);
    }

    public static Compilation compileForNoModule(String packageDir, String folderName, String tdsFilename, boolean debugLog, int warningCount, String... fileNames) {
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
        long myWarnings = c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.WARNING)).count();
        if (myWarnings != warningCount) {
            c.warnings().forEach(System.err::println);
            fail("Warnings count differ "+warningCount+" (expected) !="+c.warnings().size());
        }

        if (! c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName, tdsFilename).isPresent()) {
            c.generatedFiles().forEach(System.out::println);
        }

        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName, tdsFilename).isPresent());

        return c;
    }


    /** small helper for compiling several java files in an unified way for a specific module */
    public static Compilation compileForModule(String packageDir, String folderName, String tdsFilename, boolean debugLog, int warningCount, String... fileNames) {
        Compilation c = compileForNoModule(packageDir, folderName, tdsFilename, debugLog, warningCount, fileNames);

        if (! c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName , PACKAGE_JSON).isPresent() || ! c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName, tdsFilename).isPresent()) {
            c.generatedFiles().forEach(System.out::println);
        }

        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName , PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName, tdsFilename).isPresent());

        return c;
    }
}
