/*
 * Copyright (c) 2018 Dragan Zuvic
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

package dz.jtsgen.processor.jtp.conv;

import dz.jtsgen.processor.model.TypeScriptModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultJavaTypeConverterTest {


    private TSProcessingInfo param = Mockito.mock(TSProcessingInfo.class);
    private TypeElement elementMock = null;
    private TypeScriptModel tsModel = TypeScriptModel.newModelWithDefaultModule();


    @BeforeEach
    void init() {
        this.param = Mockito.mock(TSProcessingInfo.class);
        ProcessingEnvironment processingEnvMock = mock(ProcessingEnvironment.class);
        Elements elementsMock = mock(Elements.class);
        this. elementMock = mock(TypeElement.class);
        this.tsModel = TypeScriptModel.newModelWithDefaultModule();
        Types typeUtils = mock(Types.class);

        when(this.param.getpEnv()).thenReturn(processingEnvMock);
        when(processingEnvMock.getElementUtils()).thenReturn(elementsMock);
        when(elementsMock.getTypeElement(any())).thenReturn(this.elementMock);
        when(this.param.getTsModel()).thenReturn(this.tsModel);
        when(processingEnvMock.getTypeUtils()).thenReturn(typeUtils);
        when(typeUtils.directSupertypes(any())).thenReturn(new ArrayList<>());
    }

    @Test
    void convertJavaType_called_null() {
        DefaultJavaTypeConverter testee = new DefaultJavaTypeConverter(this.param);
        assertEquals(Optional.empty(), testee.convertJavaType(null));
    }

    @Test
    @DisplayName("check if an invalid Element is converted to an Optional.empty()")
    void convertWrongKindReturnsEmpty() {
        DefaultJavaTypeConverter teste = new DefaultJavaTypeConverter(this.param);
        when(this.elementMock.getKind()).thenReturn(ElementKind.EXCEPTION_PARAMETER);
        assertEquals(Optional.empty(), teste.convertJavaType(this.elementMock));
    }
}