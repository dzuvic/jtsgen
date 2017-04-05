/*
 * Copyright 2016 Dragan Zuvic
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

package dz.jtsgen.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import dz.jtsgen.processor.helper.ReferenceHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



@RunWith(JUnit4.class)
public class TsGenProcessorTest {

    public static final String JTSGEN_MYMODULE = "jtsgen.mymodule";
    public static final String PACKAGE_JSON = "package.json";
    public static final String MY_MODULE_D_TS = "my-module.d.ts";
    public static final String JTSGEN_UNKNOWN = "jtsgen.unknown";
    public static final String JTS_DEV = "JtsDev";

    @Test
     public void check_simple_interface_No_Logging() {
        final JavaFileObject INTERFACE_TEST = JavaFileObjects.forResource("java/InterFaceTest.java");
        TsGenProcessor processor = new TsGenProcessor();
        Object[] dummyOptions = {"-Agone=nowhere", "-AjtsgenLogLevel=DUMMY"};
        JavaFileObject[] files = {INTERFACE_TEST};
        Compilation c = javac()
                .withProcessors(processor)
                .withOptions(dummyOptions)
                .compile(files);

        // check that any logging is disabled is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.OFF);
    }

    @Test
    @Ignore // This test is only for development. No one wants excessive DEBUG output
     public void check_simple_interface_with_debug() throws IOException {
        final JavaFileObject INTERFACE_TEST = JavaFileObjects.forResource("InterFaceTest.java");
        TsGenProcessor processor = new TsGenProcessor();
        Object[] dummyOptions = {"-AjtsgenLogLevel=FINEST"};
        JavaFileObject[] files = {INTERFACE_TEST};
        Compilation c = javac()
                .withProcessors(processor)
                .withOptions(dummyOptions)
                .compile(files);

        // check debug is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.FINEST);
        
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV, PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV,"jts-dev.d.ts").isPresent());

        JavaFileObject testee = c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV, PACKAGE_JSON).get();
        System.out.println(testee.getCharContent(false));

        JavaFileObject testee2 = c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV,"jts-dev.d.ts").get();
        System.out.println("");
        System.out.println(testee2.getCharContent(false));
    }

    @Test
     public void test_simple_interface_1() throws IOException {
        final JavaFileObject INTERFACE_TEST = JavaFileObjects.forResource("java/InterFaceTest.java");
        JavaFileObject[] files = {INTERFACE_TEST};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
                .withOptions(new Object[]{"-AjtsgenModuleName=MyModule"})
                .compile(files);

        assertEquals(0, c.errors().size());

        // check debug is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.OFF);

        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT,JTSGEN_MYMODULE, PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT,JTSGEN_MYMODULE, MY_MODULE_D_TS).isPresent());

        JavaFileObject testee2 = c.generatedFile(StandardLocation.SOURCE_OUTPUT,JTSGEN_MYMODULE, MY_MODULE_D_TS).get();

        ReferenceHelper.assertEquals(
                c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTSGEN_MYMODULE, PACKAGE_JSON).get()
                , "simple_interface_1.package.json");

        ReferenceHelper.assertEquals(
                c.generatedFile(StandardLocation.SOURCE_OUTPUT,JTSGEN_MYMODULE, MY_MODULE_D_TS).get()
                , "simple_interface_1.my-module.d.ts");
    }

    @Test
     public void test_simple_interface_no_package() throws IOException {
        final JavaFileObject INTERFACE_TEST = JavaFileObjects.forResource("java/InterFaceTestNoPackage.java");
        TsGenProcessor processor = new TsGenProcessor();
        JavaFileObject[] files = {INTERFACE_TEST};
        Compilation c = javac()
                .withProcessors(processor)
                .withOptions(new Object[]{}) // "-AjtsgenLogLevel=FINEST"
                .compile(files);

        assertEquals(0, c.errors().size());

        // using default packages issues a warning
        assertEquals(c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.WARNING)).count(), 1);

        // module name is unknown
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTSGEN_UNKNOWN, PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTSGEN_UNKNOWN,"unknown.d.ts").isPresent());
    }

}