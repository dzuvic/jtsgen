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

package dz.jtsgen.processor.jtp.visitors;

import dz.jtsgen.processor.jtp.processors.TSProcessingInfo;
import dz.jtsgen.processor.model.TypeScriptModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;


public class TSProcessingInfoImpl implements TSProcessingInfo {
    // the annotation, that is being processed
    private final TypeElement annotation;

    // for convenience, so the Visitors don't need any constructors.
    private final ProcessingEnvironment pEnv;

    // the actual TypeScript Model
    private final TypeScriptModel tsModel;

    public TSProcessingInfoImpl(TypeElement annotation, ProcessingEnvironment pEnv, TypeScriptModel tsModel) {
        this.annotation = annotation;
        this.pEnv = pEnv;
        this.tsModel = tsModel;
    }

    public TypeElement getAnnotation() {
        return annotation;
    }

    public ProcessingEnvironment getpEnv() {
        return pEnv;
    }

    public TypeScriptModel getTsModel() {
        return tsModel;
    }

}
