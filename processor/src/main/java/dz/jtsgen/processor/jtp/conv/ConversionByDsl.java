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

package dz.jtsgen.processor.jtp.conv;

import dz.jtsgen.processor.model.TSTargetType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.util.StringUtils.withoutTypeArgs;

/**
 * This class is contains all helpers around types, that ca be directly converted by the declared DSL
 */
class ConversionByDsl {
    private static Logger LOG = Logger.getLogger(ConversionByDsl.class.getName());

    static Optional<TSTargetType> directConversionType(TypeMirror t, Map<String, TSTargetType> declaredTypeConversions, ProcessingEnvironment pEnv) {
        final String nameOfType = withoutTypeArgs(t.toString());
        return directConversionType(t,nameOfType,declaredTypeConversions,pEnv);
    }

    static Optional<TSTargetType> directConversionType(TypeMirror t, String nameOfType, Map<String, TSTargetType> declaredTypeConversions, ProcessingEnvironment pEnv) {
        if (declaredTypeConversions.containsKey(nameOfType)) {
            LOG.finest(() -> "bDSL: declared Type in conversion List:" + nameOfType);
            return Optional.of(declaredTypeConversions.get(nameOfType));
        }

        LOG.finest(() -> "bDSL: declared Type NOT in conversion List:" + nameOfType);

        // exclude the mother of all java types, any will be resolved later
        List<? extends TypeMirror> supertypes = pEnv.getTypeUtils().directSupertypes(t).stream().filter(
            x -> ! "java.lang.Object".equals(x.toString())
        ).collect(Collectors.toList());
        Set<String> superTypeNames = supertypes.stream().map(
                // the underlying type of the TypeMirror is a Symbol, which has all the information we need
                // unfortunately Oracle/Sun decided not being accessible. Casting to internal classes is not an option
                x -> withoutTypeArgs(x.toString())
        ).collect(Collectors.toSet());
        LOG.finest(() -> "bDSL: list of super types for " + nameOfType + ": " + superTypeNames);

        return declaredTypeConversions.values().stream().filter( x -> superTypeNames.contains(x.getJavaType())).findFirst();
    }
}
