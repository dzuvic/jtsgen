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

package dz.jtsgen.processor.dsl.model;

import dz.jtsgen.processor.model.ConversionCoverage;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

import static dz.jtsgen.processor.model.ConversionCoverage.DIRECT;
import static dz.jtsgen.processor.model.ConversionCoverage.SUBTYPES;
import static dz.jtsgen.processor.util.StringUtils.notEmptytrimmed;

/**
 * simple representation of parsed arrow expression
 */
@Value.Immutable
public abstract class TypeMappingExpression {

    @Value.Parameter
    public abstract List<String> typeVariables();

    @Value.Parameter
    public abstract List<String> names();

    @Value.Parameter
    public abstract List<TSExpressionElement> tsExpressionElements();


    @Value.Parameter
    public abstract ConversionCoverage conversionCoverage();

}
