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
import dz.jtsgen.processor.util.Tuple;

import javax.lang.model.element.Element;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.util.StringUtils.lastOf;
import static dz.jtsgen.processor.util.StringUtils.untill;

/**
 * calculates a namespace mapping to map toplevel package to root:
 * 
 * a.b.c.X
 * a.b.c.Y
 * a.b.c.e.Z
 * -------
 * a.b.c -> ''
 *
 * a.X
 * a.Y
 * a.b.Y
 * -----
 * a-> ''
 *
 *
 * a.X
 * c.Y
 * a.b.Y
 * -----
 *  {}

 */
final public class NameSpaceMapperCalculator {
    private static Logger LOG = Logger.getLogger(NameSpaceMapperCalculator.class.getName());

    public static List<NameSpaceMapping> computeNameSpaceMapping(Collection<? extends Element> elements) {

        List<Tuple<String, String>> canonicalTypes = typesWithPackageNames(elements);

        LOG.fine(() -> "calculating namespaces for: " + canonicalTypes);

        Set<String> topPackages = NameSpaceHelper.topPackages( canonicalTypes.stream().map(Tuple::getFirst).collect(Collectors.toList()) );

        LOG.finest(() -> "top Packages are: " + topPackages);

        Map<String,Set<String>> topPackagesWitchTypes  = canonicalTypes.stream()
                .filter( x -> topPackages.contains(x.getFirst()))
                .collect(Collectors.groupingBy( Tuple::getFirst, Collectors.mapping(Tuple::getSecond, Collectors.toSet())));

        //
        Map<String,Integer> typesCount = new HashMap<>();
        for (Set<String> x : topPackagesWitchTypes.values()) for (String y : x) {
            final int oldCount = typesCount.get(y) == null ? 0 : typesCount.get(y);
            typesCount.put(y, oldCount + 1);
        }

        LOG.finest( () -> "Types Count: " + typesCount);

        if (typesCount.values().stream().anyMatch(x -> x >1)) {
            LOG.info(() -> "Same names in top packages");
            return new ArrayList<>();
        }
        List<NameSpaceMapping> computedNamesSpaces = topPackages.stream().map( x -> new NameSpaceMapping(x,"")).collect(Collectors.toList());

        LOG.fine( () -> "Default NameSpace mapping is " + computedNamesSpaces);
        return computedNamesSpaces;
    }

    public static List<Tuple<String, String>> typesWithPackageNames(Collection<? extends Element> annotatedElements) {
        return annotatedElements.stream()
                    .map(x -> new Tuple<>(untill(x.toString()), lastOf(x.toString())))
                    .collect(Collectors.toList());
    }

}
