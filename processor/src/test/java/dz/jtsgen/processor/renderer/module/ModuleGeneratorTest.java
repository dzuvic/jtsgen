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

package dz.jtsgen.processor.renderer.module;

import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ModuleGeneratorTest {

    @Test
    @DisplayName("testing handling exceptions when writing module")
    void writeModule() throws Exception {
        TypeScriptRenderModel model = new TypeScriptRenderModel(TypeScriptModel.newModelWithDefaultModule());
        ProcessingEnvironment env = mock(ProcessingEnvironment.class);
        Filer filer = mock(Filer.class);
        Messager messager = mock(Messager.class);
        when(env.getMessager()).thenReturn(messager);
        when(filer.createSourceFile(any(),any(),any(),any())).thenThrow(new IOException("mocked IO Error"));
        when(filer.createResource(any(),any(),any())).thenThrow(new IOException("mocked IO Error"));
        when(env.getFiler()).thenReturn(filer);
        ModuleGenerator testee = new ModuleGenerator(model,env);
        testee.writeModule();

        verify(messager).printMessage(any(),any());
    }

}