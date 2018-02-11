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

package dz.jtsgen.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import dz.jtsgen.processor.helper.StringConstForTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static com.google.testing.compile.Compiler.javac;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * test that need a tomporary directory, e.g. testing the -d Option
 */
class TsGenProcessorTmpDirTest {

    private Path tmpDir;

    @BeforeEach
    void createTmpDir() throws IOException {
        this.tmpDir = Files.createTempDirectory("jtsgen-test");
    }

    @AfterEach
    void deleteTmpDir() throws IOException {
        Files.walkFileTree(this.tmpDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(final Path theDir, final IOException e) throws IOException {
                if (e != null) return TERMINATE;
                Files.delete(theDir);
                return CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path theFile, final BasicFileAttributes attrs) throws IOException {
                Files.delete(theFile);
                return CONTINUE;
            }
        });
    }


    @Test
    void test_simple_interface_minus_d_option() {
        JavaFileObject[] files = {JavaFileObjects.forResource("InterFaceTestNoPackage.java")};
        Compilation c = javac()
                .withProcessors(new TsGenProcessor())
                .withOptions("-d", tmpDir.toAbsolutePath().toString())
                .compile(files);

        assertEquals(0, c.errors().size());

        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_UNKNOWN, StringConstForTest.PACKAGE_JSON).isPresent());
        assertTrue(c.generatedFile(StandardLocation.SOURCE_OUTPUT, StringConstForTest.JTSGEN_UNKNOWN, "unknown.d.ts").isPresent());
    }

}