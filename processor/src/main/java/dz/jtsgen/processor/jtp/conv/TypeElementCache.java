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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeElementCache {
    private final ProcessingEnvironment processingEnvironment;
    private final Map<String,TypeElement> cache = new ConcurrentHashMap<>();

    public TypeElementCache(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public TypeElement typeElementByCanonicalName(String canonicalName) {
        if (this.cache.containsKey(canonicalName)) return this.cache.get(canonicalName);
        else {
            TypeElement element = this.processingEnvironment.getElementUtils().getTypeElement(canonicalName);
            this.cache.put(canonicalName,element);
            return element;
        }
    }
}
