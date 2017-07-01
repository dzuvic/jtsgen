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
import static dz.jtsgen.processor.helper.OutputHelper.countPatterns;
import static dz.jtsgen.processor.helper.OutputHelper.findSourceLine;
import static dz.jtsgen.processor.helper.StringConstForTest.*;
import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class TsGenProcessorTest {

    private final boolean DUMP_FILES = false;

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
                .withOptions("-Agone=nowhere", "-AjtsgenLogLevel=DUMMY")
                .compile(files);

        // check that any logging is disabled is disabled
        assertEquals(Logger.getLogger(TsGenProcessor.class.getPackage().getName()).getLevel(), Level.OFF);
    }

    @Test
    public void test_simple_interface_1() throws IOException {
        JavaFileObject[] files = {JavaFileObjects.forResource("jts/dev/InterFaceTest.java")};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
                .withOptions("-AjtsgenModuleName=MyModule")
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
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES,  0, "InterFaceTest.java", "InterFaceTestIgnored.java");
        assertEquals("must have Type InterFaceTest", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
        assertEquals("must not have Type InterFaceTestIgnored", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestIgnored\\s*\\{")).size());
    }

    @Test
    public void test_simple_interface_with_inheritance() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES,  0, "InterFaceInheritance1Test.java");
        assertEquals("must have Type InterFaceInheritance1Test extends InterFaceInheritance1TestParent", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceInheritance1Test\\s+extends\\s+InterFaceInheritance1TestParent\\s+\\{")).size());
        assertEquals("must have Type InterFaceInheritance1TestParent", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceInheritance1TestParent\\s+\\{")).size());
    }

    @Test
    public void test_two_simple_interface_with_one_ignored_partially() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES,  0, "InterFaceTest.java", "InterFaceTestWithOneIgnoredMethod.java");
        assertEquals("must have Type InterFaceTest", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
        assertEquals("must have Type InterFaceTestWithOneIgnoredMethod", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestWithOneIgnoredMethod\\s*\\{")).size());
        assertEquals("the member otherIntIgnored must not be included", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("otherIntIgnored")).size());
        assertEquals("the member otherStringNotIgnored must be included", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("otherStringNotIgnored:\\s+string;")).size());
    }

    @Test
    public void two_types() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES, 0, "InterFaceTest.java", "MemberTestObject.java");
        assertEquals("must have Type MemberTestObject", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+MemberTestObject\\s*\\{")).size());
        assertEquals("must have Type InterFaceTest", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTest\\s+\\{")).size());
    }

    @Test
    public void test_container_types() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES, 0,  "MemberContainerTest.java");
        assertEquals("must have Type MemberContainerTest", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+MemberContainerTest\\s*\\{")).size());
        assertEquals("list must be mapped to Array<string>", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("list_Of_String:\\s+Array<string>;")).size());
        assertEquals("set must be mapped to Array<number>", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("set_Of_Int:\\s+Array<number>;")).size());
        assertEquals("java.util.Map must be mapped to Map<..>", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("map_of_string_to_list_of_string:\\s+Map<string,\\s+Array<string>>;")).size());
    }


    @Test
    public void test_simple_class() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES, 0,"MemberTestObject.java");

        assertEquals("must be readonly", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+readonly\\s+x_with_getter_only:\\s+number")).size());
        assertEquals("the setter/getter is not readonly", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+x_with_getter_setter:\\s+number")).size());
        assertEquals("the setter must not be included", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("x_with_setter_only:\\s+number")).size());
        assertEquals("don't include the non public members", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_private:\\s+number")).size());
        assertEquals("don't include the non public members", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_protected:\\s+number")).size());
        assertEquals("don't include the non public members", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("member_package_protected:\\s+number")).size());
    }

    @Test
    public void test_simple_enum() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES, 0,"InterfaceWithEnum.java","SomeEnum.java");

        assertEquals("must have Type SomeEnum", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+enum\\s+SomeEnum\\s*\\{")).size());
        assertEquals("must have Type InterfaceWithEnum", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterfaceWithEnum\\s*\\{")).size());

        assertEquals("must include enum values", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+A, B, C\\s*$")).size());
        assertEquals("must have the member someEnum: SomeEnum", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+readonly\\s+someEnum:\\s+SomeEnum;")).size());

    }

    @Test
    public void person_examplApi_test() throws Exception {
        final String folderName = "exampleapi";
        final String tdsFilename = "example-api.d.ts";
        Compilation c = CompileHelper.compileForNoModule("jts/modules/person", folderName, tdsFilename, DUMP_FILES, 0, "Item.java", "Order.java", "package-info.java", "Person.java", "Sex.java");

        assertEquals("must have Type Sex", 1,    findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+enum\\s+Sex\\s*\\{")).size());
        assertEquals("must have Type Item",  1,  findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+Item\\s*\\{")).size());
        assertEquals("must have Type Order",  1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+Order\\s*\\{")).size());
        assertEquals("must have Type Person", 1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+Person\\s*\\{")).size());

        assertEquals("must be mapped to number", 1,      findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+price:\\s+number;")).size());
        assertEquals("must be mapped to Array<Item>", 1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+items:\\s+Array<Item>;")).size());
        assertEquals("must be mapped to Person", 1,      findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+customer:\\s+Person;")).size());
        assertEquals("must be mapped to string", 1,      findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+birthdate:\\s+string;")).size());
        assertEquals("must be mapped to Sex", 1,         findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+sex:\\s+Sex;")).size());
        
    }

    @Test
    public void test_default_exclusion() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES, 1,"InterFaceTestWithSunInternal.java");

        assertEquals("must have Type InterfaceWithEnum", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestWithSunInternal\\s*\\{")).size());
        assertEquals("must be mapped to any", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+readonly\\s+mustBeExcluded:\\s+any;")).size());

        assertEquals("must not have the type Version included", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+Version\\s*\\{")).size());
        assertEquals("must not have member from Version", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("jdkSpecialVersion:\\s+string;")).size());
    }

    @Test
    public void test_custom_exclusion() throws IOException {
        final String folderName = "exclusion_test";
        final String tdsFilename = "exclusion_test.d.ts";
        Compilation c = CompileHelper.compileForModule("jts/modules/exclude", folderName, tdsFilename, DUMP_FILES, 0, "InterFaceTestSelfExclusion.java", "package-info.java");

        // ?? What?
        assertEquals("must have Type InterfaceWithEnum", 0, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestSelfExclusion\\s*\\{")).size());
    }


    @Test
    public void test_custom_namespace_map() throws IOException {
        final String folderName = "namespace_test";
        final String tdsFilename = "namespace_test.d.ts";
        Compilation c = CompileHelper.compileForModule("jts/modules/nsmap", folderName, tdsFilename, DUMP_FILES, 0, "InterFaceTestNameSpaceMapped.java", "package-info.java");

        assertEquals("must have Type InterFaceTestNameSpaceMapped", 1, countPatterns(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestNameSpaceMapped\\s*\\{")) );
        assertEquals("must not have name space easy", 1, countPatterns(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+namespace\\s+easy")) );
    }

    @Test
    public void test_namespace_clash() throws IOException {
        final String folderName = "unknown";
        final String tdsFilename = "unknown.d.ts";
        Compilation c = CompileHelper.compileForModule("jts/modules/nsmap_clash", folderName, tdsFilename, DUMP_FILES, 1, "a/InterFaceClash.java", "b/InterFaceClash.java");

        assertEquals("must have Type InterFaceClash twice", 2, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceClash\\s*\\{")).size());
        assertEquals("must have namespace a", 1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+namespace\\s+a\\s*\\{")).size());
        assertEquals("must have namespace b", 1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+namespace\\s+b\\s*\\{")).size());
    }

    @Test
    public void test_MemberDefaultMappingTest() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES, 0,"MemberDefaultMappingTest.java");

        ReferenceHelper.assertEquals(
                c.generatedFile(StandardLocation.SOURCE_OUTPUT, JTS_DEV, JTS_DEV_D_TS).orElse(null)
                , "default_type_mappings.d.ts");

    }


    /**
     * Two types in different packages
     * - has NS mapped
     * - module name is unknown: two top level packages
     */
    @Test
    public void test_namespace_noclash() throws IOException {
        final String folderName = "unknown";
        final String tdsFilename = "unknown.d.ts";
        Compilation c = CompileHelper.compileForModule("jts/modules/nsmap_noclash", folderName, tdsFilename, DUMP_FILES, 1, "a/InterFaceNoClashA.java", "b/InterFaceNoClashB.java");

        assertEquals("must have Type InterFaceNoClashA", 1, countPatterns(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceNoClashA\\s*\\{")) );
        assertEquals("must have Type InterFaceNoClashB", 1, countPatterns(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceNoClashB\\s*\\{")) );

        assertEquals("must not have namespace", 0, countPatterns(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+namespace")) );
    }

    @Test
    public void test_simple_date() throws IOException {
        Compilation c = CompileHelper.compileJtsDev(DUMP_FILES, 0,"InterfaceWithDate.java");
        assertEquals("must have Type InterfaceWithDate", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+InterfaceWithDate\\s*\\{")).size());
        assertEquals("must have Type Date", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+interface\\s+Date\\s*\\{")).size());

        // namespave java.util
        assertEquals("must not have namespace java", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+namespace\\s+java\\s*\\{")).size());
        assertEquals("must not have namespace util", 0, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+export\\s+namespace\\s+util\\s*\\{")).size());

        // must have some Date attributes
        assertEquals("must attribute hours: number;", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+hours:\\s*number;")).size());
        assertEquals("must attribute seconds: number;", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+seconds:\\s*number;")).size());
        assertEquals("must attribute month: number;", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+month:\\s*number;")).size());
        assertEquals("must attribute year: number;", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+year:\\s*number;")).size());
        assertEquals("must attribute minutes: number;", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+minutes:\\s*number;")).size());

        // must be mapped to java.util.Date
        assertEquals("must be mapped to Date", 1, findSourceLine(c, JTS_DEV, JTS_DEV_D_TS, Pattern.compile("^\\s+someDate:\\s+Date;")).size());
    }

    @Test
    public void test_output_type_d_ts_only() throws IOException {
        final String folderName = "no_module";
        final String tdsFilename = "no_module.d.ts";
        Compilation c = CompileHelper.compileForNoModule("jts/modules/outputNoModule", folderName, tdsFilename,DUMP_FILES,0, "InterFaceTestNameSpaceMapped.java", "package-info.java");
        assertFalse("must not contain package.json", c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName , PACKAGE_JSON).isPresent());
        assertEquals("must have Type InterFaceTestNameSpaceMapped", 1, countPatterns(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+InterFaceTestNameSpaceMapped\\s*\\{")));
    }

    @Test
    public void members_with_module_definitions() throws IOException {
        final String folderName = "testm1";
        final String tdsFilename = "test-m1.d.ts";
        Compilation c = CompileHelper.compileForModule("jts/modules/testM1", folderName, tdsFilename, DUMP_FILES, 0, "MemberWithModuleDef.java", "package-info.java");
        assertEquals("must have author Me Myself And I", 1, findSourceLine(c, folderName, PACKAGE_JSON, Pattern.compile("^\\s+\"author\":\\s+\"Me Myself And I\"")).size());
        assertEquals("must have authorUrl some authorUrl", 1, findSourceLine(c, folderName, PACKAGE_JSON, Pattern.compile("^\\s+\"authorUrl\":\\s+\"SomeAuthorUrl\"")).size());
        assertEquals("must have description some description", 1, findSourceLine(c, folderName, PACKAGE_JSON, Pattern.compile("^\\s+\"description\":\\s+\"some description\"")).size());
        assertEquals("must have license some license", 1, findSourceLine(c, folderName, PACKAGE_JSON, Pattern.compile("^\\s+\"license\":\\s+\"some license\"")).size());

        assertEquals("must have Type MemberWithModuleDef", 1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+export\\s+interface\\s+MemberWithModuleDef\\s*\\{")).size());
        assertEquals("java.util.Date must be converted to string", 1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s+date_string:\\s+string;")).size());
    }

    @Test
    public void test_output_type_no_module() throws IOException {
        final String folderName = "simple_file";
        final String tdsFilename = "simple_file.ts";
        Compilation c = CompileHelper.compileForNoModule("jts/modules/outputSimpleFile", folderName, tdsFilename, DUMP_FILES,0, "InterFaceTestSimpleFile.java", "package-info.java");

        ReferenceHelper.assertEquals(
                c.generatedFile(StandardLocation.SOURCE_OUTPUT, folderName, tdsFilename).get()
                , "no_module.simple_file.ts");
    }

    @Test
    public void test_output_type_external_module() throws IOException {
        final String folderName = "external_module";
        final String tdsFilename = "external_module.ts";
        Compilation c = CompileHelper.compileForModule("jts/modules/outputExternalModule", folderName, tdsFilename,DUMP_FILES,0, "InterFaceTestExportModule.java", "package-info.java");

        assertEquals("must have module defined in main", 1, findSourceLine(c, folderName, PACKAGE_JSON, Pattern.compile("^\\s+\"main\":\\s+\"external_module.ts\"")).size());
        assertEquals("must not have module defined as ambient type", 1, findSourceLine(c, folderName, PACKAGE_JSON, Pattern.compile("^\\s+\"typings\":\\s+\"\"")).size());
        assertEquals("must be a module", 1, findSourceLine(c, folderName, tdsFilename, Pattern.compile("^\\s*declare\\s+module\\s+\"external_module\"\\s+\\{")).size());
    }

}