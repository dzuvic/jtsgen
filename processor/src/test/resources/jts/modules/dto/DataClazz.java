package jts.modules.dto;

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
import dz.jtsgen.annotations.TSReadOnly;
import dz.jtsgen.annotations.TypeScript;

import javax.annotation.Nullable;

@TypeScript
public class DataClazz {

    /**
     * Key javadoc
     */
    private String key;

    /**
     * Value javadoc
     */
    private String value;

    /**
     * Key get javadoc
     */
    @TSReadOnly
    public String getKey() {
        return key;
    }

    /**
     * Value get javadoc
     */
    @Nullable
    public String getValue() {
        return value;
    }

    /**
     * Value set javadoc
     */
    public void setValue(String value) {
        this.value = value;
    }
}
