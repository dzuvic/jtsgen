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

import dz.jtsgen.processor.helper.ElementHelper;
import dz.jtsgen.processor.model.TypeScriptModel;
import org.immutables.value.Value;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * holds information needed accessing the model when traversing the AST.
 *
 * It also contains the helper that depends on the configuration.
 */
@Value.Immutable
public abstract class TSProcessingInfo {

    @Value.Parameter
    public abstract ProcessingEnvironment getpEnv();

    @Value.Parameter
    public abstract TypeScriptModel getTsModel();

    /**
     * @return the elements cache
     */
    @Value.Lazy
    public TypeElementCache elementCache() {
        return new TypeElementCacheImpl(this.getpEnv());
    }


    /**
     * @return a list of additional elements that needed to be converted
     */
    @Value.Lazy
    public Set<Element> additionalTypesToConvert() {
        return this.getTsModel().getModuleInfo().additionalTypes().stream()
                .map( x -> this.elementCache().typeElementByCanonicalName(x))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Value.Lazy
    public ExecutableElementHelper executableHelper() {
        return new ExecutableElementHelperImpl(
                this.getTsModel().getModuleInfo().getterPrefixes(),
                this.getTsModel().getModuleInfo().setterPrefixes());
    }


}
