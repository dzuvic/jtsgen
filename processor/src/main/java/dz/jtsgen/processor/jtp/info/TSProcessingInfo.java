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

import dz.jtsgen.processor.mapper.name.NameMapper;
import dz.jtsgen.processor.mapper.name.NameMapperFactory;
import dz.jtsgen.processor.model.TSTargetType;
import dz.jtsgen.processor.model.TypeScriptModel;
import org.immutables.value.Value;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.model.tstarget.TSTargets.defaultDeclaredTypeConversion;
import static java.util.Collections.unmodifiableMap;

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
        return Collections.unmodifiableSet(
                this.getTsModel().getModuleInfo().additionalTypes().stream()
                    .map( x -> this.elementCache().typeElementByCanonicalName(x))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
    }

    @Value.Lazy
    public ExecutableElementHelper executableHelper() {
        return ExecutableElementHelperImplBuilder.builder()
                .getterPrefixes( this.getTsModel().getModuleInfo().getterPrefixes())
                .setterPrefixes( this.getTsModel().getModuleInfo().setterPrefixes())
                .build();
    }

    @Value.Lazy
    public NameMapper nameMapper() {
        return NameMapperFactory.createNameMapper(this.getTsModel().getModuleInfo().nameMappingStrategy());
    }

    /**
     * @return a list of declared type conversion in a linked hash map
     */
    @Value.Lazy
    public Map<String, TSTargetType> declaredTypeConversions() {
        LinkedHashMap<String, TSTargetType> result = new LinkedHashMap<>();
        result.putAll(this.getTsModel().getModuleInfo().getCustomMappings());
        result.putAll(
                defaultDeclaredTypeConversion().stream()
                        .filter(x -> ! this.getTsModel().getModuleInfo().getCustomMappings().containsKey(x.getJavaType()))
                        .collect(Collectors.toMap(TSTargetType::getJavaType,Function.identity()))
        );
        return unmodifiableMap(result);
    }

}
