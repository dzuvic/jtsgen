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
import dz.jtsgen.processor.dsl.model.TSMappedTypeContainer;
import dz.jtsgen.processor.dsl.model.TSMappedTypeVarBuilder;
import dz.jtsgen.processor.dsl.model.TypeMappingExpression;
import dz.jtsgen.processor.model.ConversionCoverage;
import dz.jtsgen.processor.util.Either;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static dz.jtsgen.processor.dsl.parser.CustomMappingParserFactory.parser;
import static org.junit.jupiter.api.Assertions.*;

class CustomMappingParserImplTest {

    private CustomMappingParser testee = parser();
    private Either<String, TypeMappingExpression> t = null;

    @Before
    void init() {
        this.testee = parser();
    }

    @Test
    void parse_noTypeVars() {
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
    void parse_AngleTypeVars() {
        t=testee.parse("a.b.c<T,U> |-> a<T,U>");
        assertEquals(
                t.check("bad names", x -> x.names().contains("a"))
                .check("arrow must be DIRECT", x -> x.conversionCoverage() == ConversionCoverage.DIRECT)
                .check("must have type var T", x -> x.typeVariables().contains("T"))
                .check("must have type var U", x -> x.typeVariables().contains("U"))
                .check("must have an a", x -> x.tsExpressionElements().get(0).value().equals("a"))
                .check("must have added the ts container <T,U>", x -> x.tsExpressionElements().get(1).value().equals("<T,U>"))
                .leftOrNull()
                , null
        );
        assertTrue(t.value().tsExpressionElements().get(1) instanceof TSMappedTypeContainer);
        TSMappedTypeContainer t2 = (TSMappedTypeContainer) t.value().tsExpressionElements().get(1);

        assertEquals(t2.expressions().size(),5);
        assertEquals(t2.expressions().get(0).value(),"<");
        assertEquals(t2.expressions().get(1), TSMappedTypeVarBuilder.of("T"));
        assertEquals(t2.expressions().get(2).value(),",");
        assertEquals(t2.expressions().get(3), TSMappedTypeVarBuilder.of("U"));
        assertEquals(t2.expressions().get(4).value(),">");
    }

    @Test
    void parse_backtickTypeVar() {
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

    @Test
    void check_error_backtick() {
        t=testee.parse("List` -> error");
        assertEquals(t.leftValue(),"backtick only allowed RHS for Type Variables'`' at pos 4 of expression List` -> error");
    }

    @Test
    void check_error_TSclose() {
        t=testee.parse("List -> bla>");
        assertEquals(t.leftValue(),"closing angle bracket without opening'>' at pos 11 of expression List -> bla>");
    }

    @Test
    void check_error_TSTypeVarOnlyOnRHS() {
        t=testee.parse("List -> List<T>");
        assertEquals(t.leftValue(),"TS type variable not defined on LHS ()'T' at pos 13 of expression List -> List<T>");
    }

    @Test
    void check_error_TSTypeVarOnlyOn() {
        t=testee.parse("List<T> -> List<T>");
        assertEquals(t.value().toString(),"TypeMappingExpression{typeVariables=[T], names=[List], tsExpressionElements=[TSMappedTerminal{value=List}, TSMappedTypeContainer{expressions=[TSMappedTerminal{value=<}, TSMappedTypeVar{value=T}, TSMappedTerminal{value=>}], value=<T>}], conversionCoverage=SUBTYPES}");
    }

    @Test
    void check_error_closing_bracket() {
        t=testee.parse("List >T -> error");
        assertEquals(t.leftValue(),"closing angle bracket without opening'>' at pos 5 of expression List >T -> error");
    }

    @Test
    void check_error_closing_bracket2() {
        t=testee.parse("List <T -> error");
        assertEquals(t.leftValue(),"unexpected Type Script Identifier'error' at pos 11 of expression List <T -> error");
    }

    @Test
    void check_error_typevar_not_defined() {
        t=testee.parse("List <T> -> error<U,V>");
        assertEquals(t.leftValue(),"TS type variable not defined on LHS (T)'U' at pos 18 of expression List <T> -> error<U,V>");
    }

    @Test
    void check_error_no_java_identifier() {
        t=testee.parse("0List <T> -> error<U,V>");
        assertEquals(t.leftValue(),"Invalid token '0List <T> -> error<U,V>' at pos 0 of expression 0List <T> -> error<U,V>");
    }
}