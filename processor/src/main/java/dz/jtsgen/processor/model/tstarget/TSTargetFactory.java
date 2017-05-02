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

package dz.jtsgen.processor.model.tstarget;

import dz.jtsgen.processor.model.TSTargetType;
import dz.jtsgen.processor.util.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dz.jtsgen.processor.util.StringUtils.withoutTypeArgs;

/**
 * creates a TSTargetType by the string representation of the java to ts type mapping
 */
public final class TSTargetFactory {
    private final static Logger LOG = Logger.getLogger(TSTargetFactory.class.getName());
    private final static Pattern TYPE_PARAM_PATTERN = Pattern.compile("<\\s*(\\w+)\\s*(,\\s*\\w+\\s*)*>");

    /**
     * @param mappingString the Mapping String
     * @return the corresponding TSTarget of that mapping string
     */
    public static Optional<TSTargetType> createTSTargetByMapping(String mappingString) {
        if (mappingString == null || !mappingString.contains("->")) return Optional.empty();

        String[] params = mappingString.split("->");
        if (params.length != 2 && params[0] != null && params[1] != null) {
            return Optional.empty();
        }
        assert params[0] != null;
        final String javaTypeString = params[0].trim();
        final String tsTypeString = params[1].trim();
        LinkedList<String> jTypeVars = typeVars(javaTypeString);
        LinkedList<String> tsTypeVars = typeVars(javaTypeString);

        if (jTypeVars.size()!=tsTypeVars.size() && Collections.disjoint(jTypeVars,tsTypeVars)) {
            LOG.info(()-> "TSTar: type variables disjoint: " + jTypeVars + " / " + tsTypeVars + " from " + mappingString);
            return Optional.empty();
        }
        return Optional.of (
                jTypeVars.isEmpty() ?
                   new TSTargetSimpleType(javaTypeString, tsTypeString)
                   : new TSTargetDeclType(withoutTypeArgs(javaTypeString), withoutTypeArgs(tsTypeString),jTypeVars,null)
            );


    }

    private static String withoutParens(String javaTypeString) {
        if (javaTypeString == null) return "";
        return javaTypeString.replaceAll("[<,>]","");
    }


    private static LinkedList<String> typeVars(String javaTypeString) {
        final Matcher jMatcher = TYPE_PARAM_PATTERN.matcher(javaTypeString);
        LinkedList<String> jTypeVars = new LinkedList<>();
        if (jMatcher.find()) {
            for (int i = 1 ; i <= jMatcher.groupCount(); i++) {
                String item = withoutParens(jMatcher.group(i));
                if (item!=null && !item.isEmpty()) jTypeVars.add(item);
            }
        }
        return jTypeVars;
    }

    /**
     * @param tstype the original TSTarget
     * @param typeParamMap the Type param map
     * @return a copy of TSTargetType with resolved Type Params
     */
    public static TSTargetType copyWithTypeParams(TSTargetType tstype, Map<String, TSTargetType> typeParamMap) {
        Optional<TSTargetInternal> tsTargetInternal = Optional.ofNullable (tstype instanceof TSTargetInternal ? (TSTargetInternal) tstype : null);
        return new TSTargetDeclType(
                tstype.getJavaType(),
                tsTargetInternal.map(TSTargetInternal::tsTargetType).orElse("none"),
                tstype.typeParameters(),
                typeParamMap);
    }
}
