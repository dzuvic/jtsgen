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

package dz.jtsgen.processor.jtp.helper;

import dz.jtsgen.annotations.TSIgnore;
import dz.jtsgen.annotations.TypeScript;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * some temporary helper
 */
public abstract class RoundEnvHelper {
    
    public static Set<Element> filteredTypeSriptElements(RoundEnvironment roundEnv) {
        final String tsIgnoreSimpleName = TSIgnore.class.getSimpleName();
        return roundEnv.getElementsAnnotatedWith(TypeScript.class).<Element>stream()
                .filter(
                        (ignoring) -> ignoring.getAnnotationMirrors().<AnnotationMirror>stream().noneMatch(
                                (y) -> tsIgnoreSimpleName.equals(y.getAnnotationType().asElement().getSimpleName().toString())))
                .collect(Collectors.toSet());
    }
}
