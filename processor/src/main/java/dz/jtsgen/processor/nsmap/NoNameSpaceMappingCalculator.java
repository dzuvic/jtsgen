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

import dz.jtsgen.processor.model.NameSpaceMapping;


import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This name space mapping strategy maps everything to the root name space, if possible.
 * All conflicts should be left into a distinct name space
 */
final class NoNameSpaceMappingCalculator implements NameSpaceMapperCalculator {

    @Override
    public List<NameSpaceMapping> computeNameSpaceMapping(Collection<? extends Element> elements) {
        return new ArrayList<>();
    }

}
