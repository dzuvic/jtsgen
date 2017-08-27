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

import java.util.*;
import java.util.regex.Pattern;

enum TokenType {
    // Token types cannot have underscores
    WHITESPACE("^[ \t\f]+"),
    ANGLE_OPEN("^\\<"),
    ANGLE_CLOSE("^\\>"),
    BACKTICK("^`"),
    DELIM("^,"),
    DOT("^\\."),
    ARROW("^-\\>"),
    DARROW("^\\|-\\>"),
    TSLIT("^[\\p{Graph}\\p{Blank}&&[^<>,`]]+", TokenType.ARROW, TokenType.DARROW),
    JIDENT("^[a-zA-Z_]+[a-zA-Z_]*"),
    INVALID("^.+") // the rest should be invisible or non ascii
    ;

    private final Pattern pattern;
    private final Set<TokenType> mustPreviouslyMatched;

    TokenType(String pattern, TokenType ... previousTokens) {
        this.pattern = Pattern.compile(pattern);
        this.mustPreviouslyMatched = Collections.unmodifiableSet(  new HashSet<>(Arrays.asList(previousTokens) ));
    }



    public Pattern getPattern() {
        return pattern;
    }

    public String groupName() {
        return this.name().replaceAll("_","x0x");
    }

    public Set<TokenType> getMustPreviouslyMatched() {
        return mustPreviouslyMatched;
    }
}
