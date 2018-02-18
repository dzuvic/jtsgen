/*
 * Copyright 2016 Dragan Zuvic
 *
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
 */

package dz.jtsgen.processor;


import com.google.common.collect.ImmutableSet;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.jupiter.api.Assertions.assertEquals;


class DummyProcessorTest {


    @Test
    void check_if_processors_are_tested() {
        final JavaFileObject HELLO_WORLD = JavaFileObjects.forResource("DummyForDummyProcessor.java");
        DummyProcessor processor = new DummyProcessor();
        Object[] dummyOptions = {"-Agone=nowhere"};
        JavaFileObject[] files = {HELLO_WORLD};
        Compilation unused = javac()
                .withProcessors(processor)
                .withOptions(dummyOptions)
                .compile(files);

        assertEquals(processor.isCalled(), true);
        assertThat(unused.generatedFiles()).hasSize(1);
        assertThat(unused.generatedFiles().get(0).toString()).contains(".class");
    }
}

class DummyProcessor extends AbstractProcessor {
    private boolean isCalled = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.isCalled = true;
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of("*");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    boolean isCalled() {
        return isCalled;
    }
}
