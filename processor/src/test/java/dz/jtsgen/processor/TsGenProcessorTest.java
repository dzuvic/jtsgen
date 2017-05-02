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
import dz.jtsgen.processor.helper.CompileHelper;
import dz.jtsgen.processor.helper.OutputHelper;
import dz.jtsgen.processor.helper.ReferenceHelper;
import dz.jtsgen.processor.helper.StringConstForTest;
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
import java.util.regex.Pattern;

import static com.google.testing.compile.Compiler.javac;
import static dz.jtsgen.processor.helper.StringConstForTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class TsGenProcessorTest {

    @Test
    public void check_simple_interface_Full_Logging() {
        Compilation c = CompileHelper.compileJtsDev(true,0, "InterFaceEmpty.java");
        // check that any logging is disabled is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.FINEST);
    }

    @Test
    public void check_simple_interface_No_Logging() {
        JavaFileObject[] files = {JavaFileObjects.forResource("jts/dev/InterFaceTest.java")};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
                .withOptions(new Object[]{"-Agone=nowhere", "-AjtsgenLogLevel=DUMMY"})
                .compile(files);

        // check that any logging is disabled is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.OFF);
    }

    @Test
    public void test_simple_interface_1() throws IOException {
        JavaFileObject[] files = {JavaFileObjects.forResource("jts/dev/InterFaceTest.java")};
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
        JavaFileObject[] files = {JavaFileObjects.forResource("InterFaceTestNoPackage.java")};
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
        Compilation c = CompileHelper.compileJtsDev(false, 0, "InterFaceTest.java", "InterFaceTestIgnored.java");
        assertEquals("must have Type InterFaceTest", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
        assertEquals("must not have Type InterFaceTestIgnored", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestIgnored\\s*\\{")).size());
    }

    @Test
    public void test_two_simple_interface_with_one_ignored_partially() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(false, 0, "InterFaceTest.java", "InterFaceTestWithOneIgnoredMethod.java");
        assertEquals("must have Type InterFaceTest", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
        assertEquals("must have Type InterFaceTestWithOneIgnoredMethod", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestWithOneIgnoredMethod\\s*\\{")).size());
        assertEquals("the member otherIntIgnored must not be included", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("otherIntIgnored")).size());
        assertEquals("the member otherStringNotIgnored must be included", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("otherStringNotIgnored:\\s+string;")).size());
    }

    @Test
    public void two_types() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(false,0, "InterFaceTest.java", "MemberTestObject.java");
        assertEquals("must have Type MemberTestObject", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+MemberTestObject\\s*\\{")).size());
        assertEquals("must have Type InterFaceTest", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
    }

    @Test
    public void test_container_types() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(false,0,  "MemberContainerTest.java");
        assertEquals("must have Type MemberContainerTest", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+MemberContainerTest\\s*\\{")).size());
        assertEquals("list must be mapped to Array<string>", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("list_Of_String:\\s+Array<string>;")).size());
        assertEquals("set must be mapped to Array<number>", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("set_Of_Int:\\s+Array<number>;")).size());
        assertEquals("list without type param must be mapped to Array", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("only_List:\\s+Array;")).size());
        assertEquals("java.util.Map must be mapped to Map<..>", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("map_of_string_to_list_of_string:\\s+Map<string,\\s+Array<string>>;")).size());
    }


    @Test
    public void test_simple_class() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(false,0,"MemberTestObject.java");

        assertEquals("must be readonly", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+readonly\\s+x_with_getter_only:\\s+number")).size());
        assertEquals("the setter/getter is not readonly", 1, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+x_with_getter_setter:\\s+number")).size());
        assertEquals("the setter must not be included", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("x_with_setter_only:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_private:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_protected:\\s+number")).size());
        assertEquals("don't include the non public members", 0, OutputHelper.findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_package_protected:\\s+number")).size());
    }

    @Test
    public void members_with_module_definitions() throws IOException {
        final String folderName = "jtsgen.testm1";
        final String tdsFilename = "test-m1.d.ts";
        Compilation c = CompileHelper.compileForModule("jts/modules/testM1", folderName, tdsFilename,false,0, "MemberWithModuleDef.java", "package-info.java");
        assertEquals("must have author Me Myself And I", 1, OutputHelper.findSourceLine(c, folderName, PACKAGE_JSON, Pattern.compile("^\\s+\"author\":\\s+\"Me Myself And I\"")).size());
        assertEquals("must have Type MemberWithModuleDef", 1, OutputHelper.findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+MemberWithModuleDef\\s*\\{")).size());
        assertEquals("java.util.Date must be converted to string", 1, OutputHelper.findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+date_string:\\s+string;")).size());
    }

}