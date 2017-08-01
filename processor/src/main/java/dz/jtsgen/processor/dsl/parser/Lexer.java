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

import dz.jtsgen.processor.dsl.ExpLexer;
import dz.jtsgen.processor.dsl.TokenBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lexer {

    List<Token> lex(String input) {

        List<Token> tokens = new ArrayList<>();

        // Lexer logic begins here
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.getPattern()));
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
//        while (matcher.find()) {
//            if (matcher.group(TokenType.NUMBER.name()) != null) {
//                tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
//            } else if (matcher.group(TokenType.BINARYOP.name()) != null) {
//                tokens.add(new Token(TokenType.BINARYOP, matcher.group(TokenType.BINARYOP.name())));
//            } else if (matcher.group(TokenType.WHITESPACE.name()) != null) {
//            }
//        }
        while (matcher.find()) {
            for (TokenType tk : TokenType.values()) {
                if (matcher.group(ExpLexer.TokenType.WHITESPACE.name()) != null)
                    continue;
                else if (matcher.group(tk.name()) != null) {
                    tokens.add( TokenBuilder.of(tk, matcher.group(tk.name()), matcher.start(tk.name())) );
                    break;
                }
            }
        }

        return tokens;
    }


}
