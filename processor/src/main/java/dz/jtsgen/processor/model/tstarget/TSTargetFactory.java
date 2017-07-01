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
import dz.jtsgen.processor.model.NameSpaceMapper;

import java.util.*;
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
    private final static NameSpaceMapper IDENTITY = x -> x;

    /**
     * @param mappingString the Mapping String
     * @return the corresponding TSTarget of that mapping string
     */
    public static Optional<TSTargetType> createTSTargetByMapping(String mappingString) {

        return TypeMappingExpression.splitIntoTwo(mappingString).flatMap( (TypeMappingExpression x) -> {

                    if ("".equals(x.getFirst()) || "".equals(x.getSecond())) return Optional.empty();

                    LinkedList<String> jTypeVars = typeVars(x.getFirst());
                    LinkedList<String> tsTypeVars = typeVars(x.getSecond());

                    if (jTypeVars.size() != tsTypeVars.size() && Collections.disjoint(jTypeVars, tsTypeVars)) {
                        LOG.info(() -> "TSTar: type variables disjoint: " + jTypeVars + " / " + tsTypeVars + " from " + mappingString);
                        return Optional.empty();
                    }
            return Optional.of(
                            new TSTargetDeclType(withoutTypeArgs(x.getFirst()), withoutTypeArgs(x.getSecond()), jTypeVars, null, x.getConversionCoverage())
                    );
                }
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
     * copies a reference type. if name space mapper is given, the names spaces are converted, too
     * @param tstype the original TSTarget
     * @param typeParamMap the Type param map
     * @return a copy of TSTargetType with resolved Type Params
     */
    public static TSTargetType copyWithTypeParams(TSTargetType tstype, Map<String, TSTargetType> typeParamMap, NameSpaceMapper nameSpaceMapper) {
        Optional<TSTargetInternal> tsTargetInternal = Optional.ofNullable (tstype instanceof TSTargetInternal ? (TSTargetInternal) tstype : null);
        NameSpaceMapper mapper = nameSpaceMapper == null ? IDENTITY : nameSpaceMapper;
        return new TSTargetDeclType(
                tstype.getJavaType(),
                tsTargetInternal.map(x -> mapper.mapNameSpace(x.tsTargetType())).orElse("none"),
                tstype.typeParameters(),
                mapNamespace(typeParamMap, mapper),
                tstype.conversionCoverage());
    }

    private static Map<String, TSTargetType> mapNamespace(Map<String, TSTargetType> typeParamMap, NameSpaceMapper mapper) {
        if (mapper == IDENTITY) return  typeParamMap;
        Map<String, TSTargetType> result = new HashMap<>();
        for (Map.Entry<String, TSTargetType> x : typeParamMap.entrySet()) {
            Optional<TSTargetInternal> tsTargetInternal = Optional.ofNullable(x.getValue() instanceof TSTargetInternal ? (TSTargetInternal) x.getValue() : null);
            if (tsTargetInternal.isPresent()) {
              result.put(x.getKey(),
                      new TSTargetDeclType(
                                      x.getValue().getJavaType(),
                                      tsTargetInternal.map(y -> mapper.mapNameSpace(y.tsTargetType())).orElse("none"),
                                      x.getValue().typeParameters(),
                                      mapNamespace(x.getValue().typeParameterTypes(), mapper),
                                      x.getValue().conversionCoverage())
                      );
            } else result.put(x.getKey(), x.getValue());
        }
        return result;
    }

    /**
     * converts the namespaces in the target type
     * @param type  the type to convert
     * @param mapper the mapper to use
     * @return  the converted target type
     */
    public static TSTargetType changeNameSpace(TSTargetType type, NameSpaceMapper mapper) {
        return type.isReferenceType() ?  copyWithTypeParams(type,type.typeParameterTypes(),mapper) : type;
    }

}
