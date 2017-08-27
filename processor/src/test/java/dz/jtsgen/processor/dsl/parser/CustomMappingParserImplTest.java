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

package dz.jtsgen.processor.dsl.parser;

import dz.jtsgen.processor.dsl.CustomMappingParser;
import dz.jtsgen.processor.dsl.model.TSMappedTypeVarBuilder;
import dz.jtsgen.processor.dsl.model.TypeMappingExpression;
import dz.jtsgen.processor.model.ConversionCoverage;
import dz.jtsgen.processor.util.Either;
import org.junit.Before;
import org.junit.Test;

import static dz.jtsgen.processor.dsl.parser.CustomMappingParserFactory.parser;
import static org.junit.Assert.*;

public class CustomMappingParserImplTest {

    private CustomMappingParser testee = parser();
    private Either<String, TypeMappingExpression> t = null;

    @Before
    public void init() {
        this.testee = parser();
    }

    @Test
    public void parse_noTypeVars() throws Exception {
        t=testee.parse("a.b.c->a");
        assertTrue(t.isRight());
        assertEquals(
                t.check("java names must conatain a",x-> x.names().contains("a") ).
                check("arrow must be SUBTYPE", x->x.conversionCoverage()== ConversionCoverage.SUBTYPES)
                .check("must not have type vars", x->x.typeVariables().isEmpty())
                .checkOrLeft("must have an a", x->x.tsExpressionElements().get(0).value().equals("a")), null
        );

    }

    @Test
    public void parse_AngleTypeVars() throws Exception {
        t=testee.parse("a.b.c<T,U> |-> a<T,U>");
        System.out.println(t);
        assertEquals(
                t.check("bad names", x -> x.names().contains("a"))
                .check("arrow must be DIRECT", x -> x.conversionCoverage() == ConversionCoverage.DIRECT)
                .check("must have type var T", x -> x.typeVariables().contains("T"))
                .check("must have type var U", x -> x.typeVariables().contains("T"))
                .check("must have an a", x -> x.tsExpressionElements().get(0).value().equals("a"))
                .check("must have added the angle bracket literal", x -> x.tsExpressionElements().get(1).value().equals("<"))
                .check("must have TS type var T", x -> x.tsExpressionElements().get(2).equals(TSMappedTypeVarBuilder.of("T")))
                .check("must have added the delimeter", x -> x.tsExpressionElements().get(3).value().equals(","))
                .check("must have TS type var U", x -> x.tsExpressionElements().get(4).equals(TSMappedTypeVarBuilder.of("U")))
                .check("must have added the angle bracket literal", x -> x.tsExpressionElements().get(5).value().equals(">"))
                .leftOrNull()
                , null
        );
    }

    @Test
    public void parse_backtickTypeVar () throws Exception {
        t=testee.parse("Collection<T> -> `T`[]");
        assertEquals(
                t.check("java type must be Collection", x -> x.names().contains("Collection"))
                .check("arrow must be SUBTYPE", x -> x.conversionCoverage() == ConversionCoverage.SUBTYPES)
                .check("must have type var T", x -> x.typeVariables().contains("T"))
                .check("must have TS type var T", x -> x.tsExpressionElements().get(0).equals(TSMappedTypeVarBuilder.of("T")))
                .check("must have square bracket literals", x -> x.tsExpressionElements().get(1).value().equals("[]"))
                .leftOrNull()
                , null
        );
    }

}