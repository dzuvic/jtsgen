/*
 * Copyright 2017 Dragan Zuvic
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for defining of a JavaScript Module
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = {ElementType.PACKAGE})
public @interface TSModule {

    /**
     * The module name of the JavaScript/TypeScript Module. This must be a java package friendly name.
     */
    String moduleName();

    /**
     * The author number for the package.json file
     */
    String author() default "unknown";

    /**
     * The authorUrl number for the package.json file
     */
    String authorUrl() default "unknown";

    /**
     * The license for the package.json file
     */
    String license() default "unknown";

    /**
     * The description for the package.json file
     */
    String description() default "unknown";


    /**
     * The version number for the package.json file
     */
    String version() default "1.0.0";

    /**
     *   custom Type Mapping for the module: this is a list of String, each describing a type mapping. Each String consists
     *   of a Java Type (canonical name) and the resulting TypeScript Type. Both Types are separated with an arrow, e.g.:
     *
     *   <pre>
     *       {@code java.util.Date -> IDateJSStatic }
     *   </pre>
     *
     *   It is possible to declare type variables, that will be converted also, e.g. :
     *
     *   <pre>
     *       {@code java.util.List<T> -> Array<T> }
     *   </pre>
     *
     *   will convert any java.util.List or it's subtypes to an Array type in TypeScript. If the matched java type has
     *   a type parameter the converted type parameter will be inserted accordingly.
     *
     *   The Processor has no knowledge about the the necessary imports.
     */
    String[] customTypeMappings() default {};

    /**
     * regular expression to exclude type conversion.
     */
    String[] excludes() default {"^sun", "^jdk.internal"};


    /**
     * A list of name spaces, that should me mapped (shortened). The empty default maps the top level name space of the
     * directly converted types to the root name space.
     *
     *
     *  {@code "a.b.c -> " }: Maps a.b.c to root
     *
     */
    String[] nameSpaceMapping() default {};
    
}
