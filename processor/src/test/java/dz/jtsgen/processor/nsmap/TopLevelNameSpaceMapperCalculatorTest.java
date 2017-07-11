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

package dz.jtsgen.processor.nsmap;

import com.google.common.collect.Lists;
import dz.jtsgen.processor.model.NameSpaceMapping;
import dz.jtsgen.processor.model.NameSpaceMappingBuilder;
import org.junit.Test;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TopLevelNameSpaceMapperCalculatorTest {

    @Test
    public void testOnePackage() throws Exception {
        assertEquals(
                Collections.singletonList(NameSpaceMappingBuilder.of("a.b", "")),
                new TopLevelNameSpaceMapperCalculator().computeNameSpaceMapping(createDataSet("a.b.X", "a.b.Y"))
        );
    }

    @Test
    public void testTwoPackageClashing_NoMapping() throws Exception {
        assertEquals(
                new ArrayList<>(),
                new TopLevelNameSpaceMapperCalculator().computeNameSpaceMapping(createDataSet("a.b.X", "a.c.X"))
        );
    }

    @Test
    public void testTwoPackage() throws Exception {
        assertEquals(
                Lists.newArrayList(
                        NameSpaceMappingBuilder.of("a.b",""),
                        NameSpaceMappingBuilder.of("a.c","")
                ),
                new TopLevelNameSpaceMapperCalculator().computeNameSpaceMapping(createDataSet("a.b.X", "a.c.Y", "a.c.Z"))
        );
    }


    private Set<Element> createDataSet(String ... args) {
        return Arrays.stream(args).map(this::createElement).collect(Collectors.toSet());

    }

    private Element createElement(final String x) {
        return new ElementSkeleton() {
            @Override
            public String toString() {
                return x;
            }
        };
    }

    private static class ElementSkeleton implements Element {
        @Override
        public TypeMirror asType() {
            return null;
        }

        @Override
        public ElementKind getKind() {
            return null;
        }

        @Override
        public Set<Modifier> getModifiers() {
            return null;
        }

        @Override
        public Name getSimpleName() {
            return null;
        }

        @Override
        public Element getEnclosingElement() {
            return null;
        }

        @Override
        public List<? extends Element> getEnclosedElements() {
            return null;
        }

        @Override
        public List<? extends AnnotationMirror> getAnnotationMirrors() {
            return null;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return null;
        }

        @Override
        public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
            return null;
        }

        @Override
        public <R, P> R accept(ElementVisitor<R, P> v, P p) {
            return null;
        }
    }
}