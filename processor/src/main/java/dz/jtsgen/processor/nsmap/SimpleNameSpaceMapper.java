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

import dz.jtsgen.processor.model.NameSpaceMapper;
import dz.jtsgen.processor.model.NameSpaceMapping;

import java.util.List;

/**
 * Simple name space mapper. Currently string based to keep things simple
 */
final class SimpleNameSpaceMapper implements NameSpaceMapper {
    private final List<NameSpaceMapping> mappings;

    SimpleNameSpaceMapper( List<NameSpaceMapping>  mappings) {
        this.mappings = mappings;
    }

    @Override
    public String mapNameSpace(String originNameSpace) {
        assert originNameSpace != null;
        return mappings.stream()
                .filter(x -> originNameSpace.startsWith(x.originNameSpace()))
                .findFirst()
                .map(x -> originNameSpace.replaceFirst("^" + x.originNameSpace(), x.targetNameSpace()).replaceFirst("^\\.", ""))
                .orElse(originNameSpace);
    }

}
