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
import dz.jtsgen.processor.helper.OutputHelper;
import dz.jtsgen.processor.helper.ReferenceHelper;
import dz.jtsgen.processor.helper.StringConstForTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class TsGenProcessorTest {

    @Test
    public void check_simple_interface_No_Logging() {
        JavaFileObject[] files = {JavaFileObjects.forResource("java/InterFaceTest.java")};
        TsGenProcessor processor = new TsGenProcessor();
        Object[] dummyOptions = {"-Agone=nowhere", "-AjtsgenLogLevel=DUMMY"};
        Compilation c = javac()
                .withProcessors(processor)
                .withOptions(dummyOptions)
                .compile(files);

        // check that any logging is disabled is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.OFF);
    }

    @Test
    public void test_simple_interface_1() throws IOException {
        JavaFileObject[] files = {JavaFileObjects.forResource("java/InterFaceTest.java")};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
                .withOptions(new Object[]{"-AjtsgenModuleName=MyModule"})
                .compile(files);

        assertEquals(0, c.errors().size());

        // check debug is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.OFF);

        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, StringConstForTest.PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, StringConstForTest.MY_MODULE_D_TS).isPresent());

        JavaFileObject testee2 = c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, StringConstForTest.MY_MODULE_D_TS).get();

        ReferenceHelper.assertEquals(
                c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, StringConstForTest.PACKAGE_JSON).get()
                , "simple_interface_1.package.json");

        ReferenceHelper.assertEquals(
                c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, StringConstForTest.MY_MODULE_D_TS).get()
                , "simple_interface_1.my-module.d.ts");
    }

    @Test
    public void test_simple_interface_no_package() throws IOException {
        JavaFileObject[] files = {JavaFileObjects.forResource("java/InterFaceTestNoPackage.java")};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
                .withOptions(new Object[]{}) // "-AjtsgenLogLevel=FINEST"
                .compile(files);

        assertEquals(0, c.errors().size());

        // using default packages issues a warning
        assertEquals(c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.WARNING)).count(), 1);

        // module name is unknown
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_UNKNOWN, StringConstForTest.PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_UNKNOWN, "unknown.d.ts").isPresent());
    }

    @Test
    public void test_simple_class() throws IOException {
        JavaFileObject[] files = {JavaFileObjects.forResource("java/MemberTestObject.java")};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
//                .withOptions(new Object[]{"-AjtsgenLogLevel=FINEST"})
                .compile(files);

        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTS_DEV, StringConstForTest.PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS).isPresent());

        assertEquals(c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.WARNING)).count(), 0);
        assertEquals(c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.ERROR)).count(), 0);

        assertEquals("must be readonly", 1, OutputHelper.findSourceLine(c, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS, Pattern.compile("^\\s+readonly\\s+x_with_getter_only:\\s+number")).size());
        assertEquals("the setter/getter is not readonly", 1, OutputHelper.findSourceLine(c, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS, Pattern.compile("^\\s+x_with_getter_setter:\\s+number")).size());
        assertEquals("the setter must not be included", 0, OutputHelper.findSourceLine(c, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS, Pattern.compile("x_with_setter_only:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS, Pattern.compile("member_private:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS, Pattern.compile("member_protected:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS, Pattern.compile("member_package_protected:\\s+number")).size());
        assertEquals("the public member must be included", 1, OutputHelper.findSourceLine(c, StringConstForTest.JTS_DEV, StringConstForTest.JTS_DEV_D_TS, Pattern.compile("^\\s+member_public:\\s+number;")).size());
    }

}