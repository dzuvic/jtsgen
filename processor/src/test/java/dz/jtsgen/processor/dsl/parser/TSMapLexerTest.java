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

import com.mifmif.common.regex.Generex;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TSMapLexerTest {

    private Lexer lexer;

    @Before
    public void init() {
        this.lexer = new Lexer();
    }

    @Test
    public void testSomePatterns() throws Exception {

        assertEquals(
                Arrays.asList(
                        TokenBuilder.of(TokenType.JIDENT,"abc",0),
                        TokenBuilder.of(TokenType.ARROW,"->",3),
                        TokenBuilder.of(TokenType.JIDENT,"T",5)
                ),
                lexer.lex("abc->T"));
    }

    @Test
    public void testRandomRegex() throws Exception {
        for (TokenType t: new TokenType[]{
                            TokenType.WHITESPACE,
                            TokenType.JIDENT,
                            TokenType.ANGLE_OPEN,
                            TokenType.ANGLE_CLOSE,
                            TokenType.BACKTICK,
                            TokenType.DELIM,
                            TokenType.DOT,
                            TokenType.ARROW,
                            TokenType.DARROW
                    }) {
            Generex generex = new Generex(t.getPattern().pattern());
            generex.setSeed(2);
            String random=generex.random();
            List<Token> tokens = lexer.lex(random);

            assertFalse("should match at least once for input '" + random +"'",tokens.isEmpty());
            assertEquals(
                    t,
                    tokens.iterator().next().type()
            );
        }
    }

    @Test
    // when no arrow, instead of TS_LIT* JIDENT should be matched
    public void testTSLits1() throws Exception {

        List<Token> tokens = lexer.lex("abcdefEfgH<T>");
        assertEquals(TokenType.JIDENT, tokens.get(0).type());
        assertEquals(TokenType.ANGLE_OPEN, tokens.get(1).type());

        List<Token> tokens2 = lexer.lex("->abcdefEfgH<T>");
        assertEquals(TokenType.ARROW, tokens2.get(0).type());
        assertEquals(TokenType.TSLIT, tokens2.get(1).type());
        assertEquals(TokenType.ANGLE_OPEN, tokens2.get(2).type());
    }

    @Test
    // after an arrow, instead of JIDENT TSLIT should be matched
    public void testTSLits2() throws Exception {

        List<Token> tokens = lexer.lex("->abcdefEfgH<T>");

        assertEquals(TokenType.ARROW, tokens.get(0).type());
        assertEquals(TokenType.TSLIT, tokens.get(1).type());
    }

    @Test
    // after an arrow, instead of JIDENT TSLIT should be matched
    public void testTSLits3() throws Exception {
        List<Token> tokens = lexer.lex("a.b.c->a");
        assertEquals(TokenType.JIDENT, tokens.get(0).type());
        assertEquals(TokenType.ARROW, tokens.get(5).type());
        assertEquals(TokenType.TSLIT, tokens.get(6).type());
    }

    @Test
    public void name() throws Exception {
        Pattern p = Pattern.compile("^[\\p{Graph}\\p{Blank}&&[^\\`]]+");
    }
}