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

package dz.jtsgen.processor.mapper.name;

/**
 * Maps a word do snake case
 */
class SnakeCaseNameMapper implements NameMapper {

    @Override
    public String mapMemberName(String rawName) {
        if (rawName == null || rawName.length() == 0 || rawName.equals("_")) return rawName;
        StringBuilder result = new StringBuilder();
        boolean prev_ = false;
        char prev_c = rawName.charAt(0);

        for (int i = 0; i < rawName.length(); i++) {
            // skip the first _
            char c = rawName.charAt(i);

            if (i == 0 && c == '_') continue;

            if (Character.isUpperCase(c)) {
                if (!prev_ && prev_c != '_' && i > 0 && !Character.isUpperCase(prev_c)) {
                    result.append("_");
                    prev_ = true;
                }
            } else {
                prev_ = false;
            }
            prev_c = c;
            result.append(Character.toLowerCase(c));
        }
        return result.toString();
    }
}
