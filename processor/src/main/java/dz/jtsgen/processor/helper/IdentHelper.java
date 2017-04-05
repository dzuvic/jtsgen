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

package dz.jtsgen.processor.helper;

public class IdentHelper {
    private IdentHelper() {
        // no instance
    }

    private static final String [] IDENT_SPACES = {
        "",
        "  ",
        "    ",
        "      ",
        "        ",
        "          ",
        "            ",
        "              ",
        "                ",
        "                  ",
        "                    ",
        "                      ",
        "                        "
    };

    private static final int IDENT_CACHE_LENGTH = IDENT_SPACES.length;
    public static final int LENGTH_MINUS_ONE = IDENT_CACHE_LENGTH - 1;

    public static String identPrefix(int level) {
        if (level < 1) return "";

        if (level < IDENT_CACHE_LENGTH) return IDENT_SPACES[level];

        StringBuilder builder=new StringBuilder( level*2);
        while (level >= IDENT_CACHE_LENGTH) {
            builder.append(IDENT_SPACES[LENGTH_MINUS_ONE]);
            level = level-LENGTH_MINUS_ONE;
        }
        return builder.append(IDENT_SPACES[level]).toString();
    }
}
