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

package dz.jtsgen.processor.jtp;

import dz.jtsgen.annotations.NameMappingStrategy;
import dz.jtsgen.annotations.NameSpaceMappingStrategy;
import dz.jtsgen.annotations.OutputType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Some additional unit tests
 */
class TSModuleHandlerTest {

    private TSModuleHandler testee;

    private ProcessingEnvironment processingEnv;

    private ExecutableElement executableElement;
    private TypeMirror annotationTypeMirror;
    private AnnotationValue annotationValue;
    private TypeElement annotationElement;
    private Elements elementUtils;


    //processingEnv.getElementUtils().getTypeElement(TSModule.class.getName()).asType();

    @BeforeEach
    void init() {
        Messager messenger = mock(Messager.class);

        this.annotationValue = mock(AnnotationValue.class);

        this.annotationTypeMirror = mock(TypeMirror.class);
        this.executableElement = mock(ExecutableElement.class);

        this.annotationElement = mock(TypeElement.class);
        when(this.annotationElement.asType()).thenReturn(this.annotationTypeMirror);

        this.elementUtils = mock(Elements.class);
        when(this.elementUtils.getTypeElement(any())).thenReturn(this.annotationElement);

        this.processingEnv = mock(ProcessingEnvironment.class);
        when(processingEnv.getMessager()).thenReturn(messenger);
        when(processingEnv.getElementUtils()).thenReturn(this.elementUtils);

        // finally....
        this.testee = new TSModuleHandler(processingEnv);
    }

    @Test
    void test_asString() {
        assertEquals("",testee.asString(annotationValue));
        when(this.annotationValue.getValue()).thenReturn("");
        assertEquals("",testee.asString(annotationValue));
    }

    @Test
    void moduleNameHandling_null_empty() {
        assertEquals("unknown",testee.processModuleName(this.executableElement, this.annotationValue));
        when(this.annotationValue.getValue()).thenReturn("");
        assertEquals("unknown",testee.processModuleName(this.executableElement, this.annotationValue));
    }

    @Test
    void moduleNameHandling_null_not_package_friendly() {
        when(this.annotationValue.getValue()).thenReturn("-a");
        assertEquals("unknown",testee.processModuleName(this.executableElement, this.annotationValue));
    }

    @Test
    void testOutputType_invalid() {
        assertNull(testee.convertOutputType(null));
        assertNull(testee.convertOutputType(this.annotationValue));
        when(this.annotationValue.getValue()).thenReturn("XXX");
        assertNull(testee.convertOutputType(this.annotationValue));
    }

    @Test
    void testOutputType_valid() {
        when(this.annotationValue.getValue()).thenReturn(OutputType.MODULE.name());
        assertEquals(OutputType.MODULE, testee.convertOutputType(this.annotationValue));
    }

    @Test
    void testStrategy_invalid() {
        assertNull(testee.convertNameSpaceMappingStrategy(null));
        assertNull(testee.convertNameSpaceMappingStrategy(this.annotationValue));
        when(this.annotationValue.getValue()).thenReturn("XXX");
        assertNull(testee.convertNameSpaceMappingStrategy(this.annotationValue));
    }

    @Test
    void testStrategy_valid() {
        when(this.annotationValue.getValue()).thenReturn(NameSpaceMappingStrategy.MANUAL.name());
        assertEquals(NameSpaceMappingStrategy.MANUAL, testee.convertNameSpaceMappingStrategy(this.annotationValue));
    }

    @Test
    @DisplayName("check invalid name mapper strategy")
    void test_namemapper_Strategy_invalid() {
        assertNull(testee.convertNameMappingStrategy(null));
        assertNull(testee.convertNameMappingStrategy(this.annotationValue));
        when(this.annotationValue.getValue()).thenReturn("XXX");
        assertNull(testee.convertNameMappingStrategy(this.annotationValue));
    }

    @Test
    @DisplayName("check valid name mapper strategy")
    void testValidNameStrategy_valid() {
        when(this.annotationValue.getValue()).thenReturn(NameMappingStrategy.SIMPLE.name());
        assertEquals(NameMappingStrategy.SIMPLE, testee.convertNameMappingStrategy(this.annotationValue));
    }


}