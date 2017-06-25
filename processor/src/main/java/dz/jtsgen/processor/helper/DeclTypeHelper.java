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

package dz.jtsgen.processor.helper;


import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

public final class DeclTypeHelper {

    public static Optional<TypeElement> declaredTypeToTypeElement(TypeMirror typeMirror) {
        return typeToDeclType(typeMirror).flatMap(DeclTypeHelper::declTypeToTypeElement);
    }

    private static Optional<TypeElement> declTypeToTypeElement(DeclaredType declaredType) {
        return (declaredType != null) && declaredType.asElement() instanceof TypeElement ?
                Optional.of((TypeElement) declaredType.asElement())
                : Optional.empty();
    }

    private static Optional<DeclaredType> typeToDeclType(TypeMirror typeMirror)  {
        return typeMirror instanceof  DeclaredType ? Optional.of((DeclaredType) typeMirror) : Optional.empty();

    }
}
