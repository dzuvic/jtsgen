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

import dz.jtsgen.processor.dsl.model.TSMappedTerminalBuilder;
import dz.jtsgen.processor.dsl.model.TSMappedTypeVarBuilder;
import dz.jtsgen.processor.dsl.model.TypeMappingExpression;
import dz.jtsgen.processor.dsl.model.TypeMappingExpressionBuilder;
import dz.jtsgen.processor.dsl.parser.CustomMappingParserFactory;
import dz.jtsgen.processor.model.ConversionCoverage;
import dz.jtsgen.processor.model.NameSpaceMapper;
import dz.jtsgen.processor.model.TSTargetType;
import dz.jtsgen.processor.util.Either;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * creates a TSTargetType by the string representation of the java to ts type mapping
 */
public final class TSTargetFactory {
    private final static Logger LOG = Logger.getLogger(TSTargetFactory.class.getName());
    private final static Pattern TYPE_PARAM_PATTERN = Pattern.compile("<\\s*(\\w+)\\s*(,\\s*\\w+\\s*)*>");
    private final static NameSpaceMapper IDENTITY = x -> x;

    /**
     *
     * @see <a href="http://jtsgen.readthedocs.io">jtsgen.readthedocs.io</a> for a reference of the DSL
     *
     *
     * @param mappingString the Mapping String
     * @return the corresponding TSTarget without namespace of that mapping string
     */
    public static Either<String,TSTargetType> createTSTargetByDSL(String mappingString) {
        LOG.fine(() -> "creating TSTarget by DSL " + mappingString);
        Either<String, dz.jtsgen.processor.dsl.model.TypeMappingExpression> expr = CustomMappingParserFactory.parser().parse(mappingString);
        Either<String, TSTargetType> result = expr.check("LHS of mapping DSL must not be empty", x -> !x.names().isEmpty())
                .map(x -> new TSTargetDeclType(x,null, true));

        return result;
    }

    /**
     *
     * @param mappingString the mapping DSL String
     * @return the TSTargettype with a name space from the LHS
     */
    public static Either<String,TSTargetType> createTSTargetByDSLWithNS(String mappingString) {
        LOG.fine(() -> "creating TSTarget by DSL (with NS) " + mappingString);

        Either<String, dz.jtsgen.processor.dsl.model.TypeMappingExpression> expr = CustomMappingParserFactory.parser().parse(mappingString);
        Either<String, TSTargetType> result = expr.check("LHS of mapping DSL must not be empty", x -> !x.names().isEmpty())
                .map(x -> new TSTargetDeclType(x,null, false));
        return result;
    }

    /**
     *
     * @param embeddedType the embedded type
     * @return the array type
     */
    public static TSTargetType createTSTargetFromArray(TSTargetType embeddedType) {
        Map<String,TSTargetType> param = new HashMap<>();
        param.put("T",embeddedType);
        return new TSTargetArrayType(arrayExpression(), param);
    }

    private static TypeMappingExpression arrayExpression() {
        return TypeMappingExpressionBuilder.builder()
                .names(new ArrayList<>())
                .typeVariables(Collections.singleton("T"))
                .conversionCoverage(ConversionCoverage.DIRECT)
                .tsExpressionElements(Arrays.asList(
                        TSMappedTypeVarBuilder.of("T")
                        ,TSMappedTerminalBuilder.of("[]")
                ))
                .build();
    }


    static Map<String, TSTargetType> mapNamespaces(Map<String, TSTargetType> typeParamMap, NameSpaceMapper mapper) {
        if (mapper == IDENTITY) return  typeParamMap;
        Map<String, TSTargetType> result = new HashMap<>();
        for (Map.Entry<String, TSTargetType> x : typeParamMap.entrySet()) {
            result.put(x.getKey(), x.getValue().mapNameSpace(mapper));
        }
        return result;
    }

}
