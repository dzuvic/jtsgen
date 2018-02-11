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

import org.junit.jupiter.api.Test;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.HashSet;

import static org.mockito.Mockito.*;

class TsGenProcessorNoAbortTest {
    @Test
    void test_Exception_process() {
        TsGenProcessor testee=new TsGenProcessor();
        ProcessingEnvironment processingEnvMock = mock(ProcessingEnvironment.class);
        Messager messenger = mock(Messager.class);
        when(processingEnvMock.getMessager()).thenReturn(messenger);
        testee.init(processingEnvMock);
        testee.process(new HashSet<>(),null);
        verify(messenger).printMessage(any(),any());
    }

}