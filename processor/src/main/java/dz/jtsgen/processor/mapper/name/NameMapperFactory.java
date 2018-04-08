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

package dz.jtsgen.processor.mapper.name;

import dz.jtsgen.annotations.NameMappingStrategy;

import java.util.logging.Logger;

public final class NameMapperFactory {

    private static Logger LOG = Logger.getLogger(NameMapperFactory.class.getName());


    /**
     * @param strategy the mapping strategy to use
     * @return an instance of a NameMapper
     */
    public static NameMapper createNameMapper(NameMappingStrategy strategy) {
        switch (strategy) {
            case SIMPLE: return new VerySimpleNameMapper();
            case JACKSON_DEFAULT: return new JacksonDefaultNameMapper();
        }
        throw new IllegalStateException("No Mapper implemented for strategy " + strategy);
    }
}
