/*
 * Copyright 2018 Dragan Zuvic
 *
 * This file is part of jtsgen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package dz.jtsgen.annotations;

/**
 * This enum defines the naming strategy of the members, especially when taken from getters
 * or setters.
 */
public enum NameMappingStrategy {

    /**
     * Leave names as is, don't try to map anything. just take the matched members as is.
     */
    SIMPLE,

    /**
     * Try to behave like the default Jackson name mapping strategy does. This is the default
     */
    JACKSON_DEFAULT,

    /**
     * The first character is uppercase, like UpperCamelCaseStrategy in Jackson
     */
    UPPER_CAMEL_CASE,

    /**
     * Snake Case, means, upper cases are interpreted as words that will be transformed to
     * lower case words separated by underscores
     */
    SNAKE_CASE
}
