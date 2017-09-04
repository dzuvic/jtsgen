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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;

/**
 * this is the lexer for the mapping DSL. Due to an ambiguity after an ARROW or DARROW the matching
 * character classes differ if an ARROW occured or not. This is because, the user should have nearly the complete
 * control, of what after an ARROW (and between an equal)
 */
class Lexer {

    List<Token> lex(String input) {

        List<Token> tokens = new ArrayList<>();

        EnumSet<TokenType> tokenTypeSet = EnumSet.noneOf(TokenType.class);
        int idx = 0;
        String restString = input;

        while (!restString.isEmpty()) {
            for (TokenType tk : TokenType.values()) {
                Matcher m = tk.getPattern().matcher(restString);
                final Boolean prevMatchedAny = tk.getMustPreviouslyMatched().stream().anyMatch(tokenTypeSet::contains);
                final boolean found = m.find();
                if ((tk.getMustPreviouslyMatched().isEmpty() || prevMatchedAny) && found) {
                    final String group = m.group(0);
                    tokens.add(TokenBuilder.of(tk, group, idx));
                    tokenTypeSet.add(tk);
                    idx = idx + group.length();
                    restString = restString.substring(group.length());

                    break;
                }
            }
        }

        return tokens;
    }


}
