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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static com.google.testing.compile.Compiler.javac;
import static dz.jtsgen.processor.helper.StringConstForTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class TsGenProcessorTest {

    @Test
    public void check_simple_interface_Full_Logging() {
        JavaFileObject[] files = {JavaFileObjects.forResource("java/InterFaceTest.java")};

        Compilation c = compileJtsDev(true,0, "java/InterFaceEmpty.java");

        // check that any logging is disabled is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.FINEST);
    }

    @Test
    public void check_simple_interface_No_Logging() {
        JavaFileObject[] files = {JavaFileObjects.forResource("java/InterFaceTest.java")};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
                .withOptions(new Object[]{"-Agone=nowhere", "-AjtsgenLogLevel=DUMMY"})
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

        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, StringConstForTest.MY_MODULE_D_TS).isPresent());

        JavaFileObject testee2 = c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, StringConstForTest.MY_MODULE_D_TS).get();

        ReferenceHelper.assertEquals(
                c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_MYMODULE, PACKAGE_JSON).get()
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
                .compile(files);

        assertEquals(0, c.errors().size());

        // using default packages issues a warning
        assertEquals(c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.WARNING)).count(), 1);

        // module name has to be is unknown
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTSGEN_UNKNOWN, PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTSGEN_UNKNOWN, "unknown.d.ts").isPresent());
    }

    @Test
    public void test_two_simple_interface_with_one_ignored() throws IOException {
        Compilation c = compileJtsDev(false, 0, "java/InterFaceTest.java", "java/InterFaceTestIgnored.java");
        assertEquals("must have Type InterFaceTest", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
        assertEquals("must not have Type InterFaceTestIgnored", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestIgnored\\s*\\{")).size());
    }

    @Test
    public void test_two_simple_interface_with_one_ignored_partially() throws IOException {
        Compilation c = compileJtsDev(false, 0, "java/InterFaceTest.java", "java/InterFaceTestWithOneIgnoredMethod.java");
        assertEquals("must have Type InterFaceTest", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
        assertEquals("must have Type InterFaceTestWithOneIgnoredMethod", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestWithOneIgnoredMethod\\s*\\{")).size());
        assertEquals("the member otherIntIgnored must not be included", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("otherIntIgnored")).size());
        assertEquals("the member otherStringNotIgnored must be included", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("otherStringNotIgnored:\\s+string;")).size());
    }

    @Test
    public void two_types() throws IOException {
        Compilation c = compileJtsDev(false,0, "java/InterFaceTest.java", "java/MemberTestObject.java");
        assertEquals("must have Type MemberTestObject", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+MemberTestObject\\s*\\{")).size());
        assertEquals("must have Type InterFaceTest", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
    }

    @Test
    public void test_simple_class() throws IOException {
        Compilation c = compileJtsDev(false,0,"java/MemberTestObject.java");

        assertEquals("must be readonly", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+readonly\\s+x_with_getter_only:\\s+number")).size());
        assertEquals("the setter/getter is not readonly", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+x_with_getter_setter:\\s+number")).size());
        assertEquals("the setter must not be included", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("x_with_setter_only:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_private:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_protected:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_package_protected:\\s+number")).size());
    }

    /** small helper for compiling several java files in an unified way */
    private Compilation compileJtsDev(boolean debugLog, int warningCount, String ... fileNames) {
        assert fileNames != null;
        JavaFileObject[] files = Arrays.stream(fileNames).map(JavaFileObjects::forResource).toArray(JavaFileObject[]::new);
        Compilation c =  javac()
                .withProcessors(new TsGenProcessor())
                .withOptions(debugLog ? new Object[]{"-AjtsgenLogLevel=FINEST"} : new Object[]{})
                .compile(files);

        assertEquals(0, c.errors().size());
        assertEquals(c.diagnostics().asList().stream().filter(x -> x.getKind().equals(Diagnostic.Kind.WARNING)).count(), warningCount);
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV, PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV, JTS_DEV_D_TS).isPresent());

        return c;
    }

}