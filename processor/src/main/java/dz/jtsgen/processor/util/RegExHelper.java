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

package dz.jtsgen.processor.util;

import dz.jtsgen.processor.TsGenProcessor;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Some Helper functions dealing with Regular Expressions
 */
public final class RegExHelper {

    private static Logger LOG = Logger.getLogger(TsGenProcessor.class.getName());

    /**
     * @param value a regular expression
     * @return Optional of  compiled pattern if: parsable, not null and not empty
     */
    public static Optional<Pattern> compileToPattern(String value) {
        if (value == null || value.isEmpty()) return Optional.empty();
        try {
            return Optional.of(Pattern.compile(value));
        } catch (NullPointerException  |PatternSyntaxException e) {
            LOG.finest(()->"Could not parse as regular expression " + value + ":" +e.getMessage());
            return Optional.empty();
        }
    }

}
