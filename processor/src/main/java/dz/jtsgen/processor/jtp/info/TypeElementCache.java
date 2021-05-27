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

package dz.jtsgen.processor.jtp.info;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface TypeElementCache {

    /**
     *
     * @param canonicalName the type as string as fqn
     * @return the TypeElement seen by the compiler or null if not found
     */
    TypeElement typeElementByCanonicalName(String canonicalName);
}

class TypeElementCacheImpl implements TypeElementCache {
    private final ProcessingEnvironment processingEnvironment;
    private final Map<String,TypeElement> cache = new ConcurrentHashMap<>();

    TypeElementCacheImpl(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public TypeElement typeElementByCanonicalName(String canonicalName) {
        if (this.cache.containsKey(canonicalName)) return this.cache.get(canonicalName);
        else {
            TypeElement element = this.processingEnvironment.getElementUtils().getTypeElement(canonicalName);
            this.cache.putIfAbsent(canonicalName,element);
            return element;
        }
    }
}
