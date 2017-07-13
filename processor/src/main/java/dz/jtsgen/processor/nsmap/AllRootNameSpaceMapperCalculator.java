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
import dz.jtsgen.processor.model.NameSpaceMappingBuilder;
import dz.jtsgen.processor.util.StringUtils;
import dz.jtsgen.processor.util.Tuple;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.nsmap.NameSpaceHelper.typesWithPackageNames;

/**
 * This name space mapping strategy maps everything to the root name space, if possible.
 * All conflicts should be left into a distinct name space
 */
class AllRootNameSpaceMapperCalculator implements NameSpaceMapperCalculator {
    private static Logger LOG = Logger.getLogger(AllRootNameSpaceMapperCalculator.class.getName());

    @Override
    public List<NameSpaceMapping> computeNameSpaceMapping(Collection<? extends Element> elements) {

        List<Tuple<String, String>> canonicalTypes = typesWithPackageNames(elements);
        Map<String, Long> sameTypeTypeCount = canonicalTypes.stream()
                .map(Tuple::getSecond)
                .collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );

        List<NameSpaceMapping> result = canonicalTypes
                .stream()
                .map(x -> {
                    if (sameTypeTypeCount.get(x.getSecond()) > 1) {
                        return NameSpaceMappingBuilder.of(x.getFirst() + "." + x.getSecond(), x.getFirst() + "." + x.getSecond()).withExact(true);
                    } else
                        return NameSpaceMappingBuilder.of(x.getFirst(),"").withExact(true);
                })
                .distinct()
                .sorted(
                        (x,y) -> Integer.compare(
                                StringUtils.countMatches(y.originNameSpace())
                                ,StringUtils.countMatches(x.originNameSpace())
                                 )
                )
                .collect(Collectors.toList());

        LOG.fine(() -> "ARNSMAP: calculated Namespacemap: " + result);
        return result;
    }

}
