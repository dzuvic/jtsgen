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

package dz.jtsgen.processor.jtp.conv;

import dz.jtsgen.processor.jtp.info.TSProcessingInfo;
import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.model.tstarget.TSTargets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MirrotTypeToTSConverterVisitorTest {

    private TypeElement typeElement;

    private MirrotTypeToTSConverterVisitor testee;

    private TSProcessingInfo param;

    @BeforeEach
    void init() {
        this.typeElement = Mockito.mock(TypeElement.class);
        this.param = Mockito.mock(TSProcessingInfo.class);
        when(this.param.getTsModel()).thenReturn(TypeScriptModel.newModelWithDefaultModule());
        ProcessingEnvironment processingEnvMock = mock(ProcessingEnvironment.class);
        Messager messenger = mock(Messager.class);
        when(processingEnvMock.getMessager()).thenReturn(messenger);
        when(this.param.getpEnv()).thenReturn(processingEnvMock);


        this.testee = new MirrotTypeToTSConverterVisitor(this.typeElement,this.param, null) ;
    }


    @Test
    void visitUnused() {

        // currently not used
        assertEquals(testee.visitIntersection(null,null), TSTargets.ANY);
        assertEquals(testee.visitNull(null,null), TSTargets.NULL);
        assertEquals(testee.visitError(null,null), TSTargets.ANY);
        assertEquals(testee.visitWildcard(null,null), TSTargets.ANY);
        assertEquals(testee.visitExecutable(null,null), TSTargets.ANY);
        assertEquals(testee.visitUnion(null,null), TSTargets.ANY);
    }


}