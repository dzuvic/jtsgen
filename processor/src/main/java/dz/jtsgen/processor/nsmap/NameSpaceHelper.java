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

import dz.jtsgen.processor.util.Tuple;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.util.StringUtils.countMatches;
import static dz.jtsgen.processor.util.StringUtils.lastOf;
import static dz.jtsgen.processor.util.StringUtils.untill;

/**
 * Some name space helper
 */
public final class NameSpaceHelper {

    /**
     *
     * @param packageNames List of package names
     * @return the top level packages, meaning, the one with shortest path length
     */
    public static Set<String> topPackages(Collection<String> packageNames) {
        Set<String> result = new HashSet<>();
        int min=Integer.MAX_VALUE;

        List<Tuple<String,Integer>> nsPathLengths= packageNames.stream().map(x-> new Tuple<>(x, pathLength(x) )).collect(Collectors.toList());

        for (Tuple<String,Integer> i: nsPathLengths){
            if (i.getSecond() < min) {
                result = new HashSet<>();
                min=i.getSecond();
                result.add(i.getFirst());
            } else if (i.getSecond().equals(min)) result.add(i.getFirst());
        }
        return result;
    }

    /**
     * @param annotatedElements a Element list
     * @return a list of tuples with first: package name second: simple name
     */
    public static List<Tuple<String, String>> typesWithPackageNames(Collection<? extends Element> annotatedElements) {
        return annotatedElements.stream()
                    .map(x -> new Tuple<>(untill(x.toString()), lastOf(x.toString())))
                    .collect(Collectors.toList());
    }

    private static Integer pathLength(String x) {
        return "".equals(x)? 0 : countMatches(x) + 1;
    }
}
