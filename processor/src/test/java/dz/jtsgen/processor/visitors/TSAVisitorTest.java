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

package dz.jtsgen.processor.visitors;

import dz.jtsgen.processor.model.TypeScriptModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;

import static org.mockito.Mockito.*;

public class TSAVisitorTest {

    @Mock
    private TypeElement annotationMock;

    @Mock
    private ProcessingEnvironment envMockm;
    
    @Mock
    private Messager messagerMock;

    private TSAVisitorParam tsaVisitorParam;

    private TSAVisitor testee;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        TypeScriptModel tsm = TypeScriptModel.newModelWithDefaultModule();
        when(envMockm.getMessager()).thenReturn(messagerMock);
        this.tsaVisitorParam= new TSAVisitorParam(annotationMock,envMockm,tsm);
        testee = new TSAVisitor(tsaVisitorParam);
    }


    @Test
    public void visitVariable() throws Exception {
        VariableElement variableMock=mock(VariableElement.class);
        testee.visitVariable(variableMock, null);
        verify(messagerMock,atLeastOnce()).printMessage(any(),any(),any());
    }

    @Test(expected = UnknownElementException.class )
    public void visitUnknown() throws Exception {
        Element x=mock(Element.class);
        testee.visitUnknown(x, null);
    }

    @Test
    public void visitPackage() throws Exception {
        PackageElement x=mock(PackageElement.class);
        testee.visitPackage(x, null);
        verify(messagerMock,atLeastOnce()).printMessage(any(),any(),any());
    }

}