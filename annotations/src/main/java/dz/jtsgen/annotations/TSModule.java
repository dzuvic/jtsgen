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

    String UNKNOWN = "unknown";
    String GETTER_EXRPESSION = "get([_a-zA-Z0-9].*)";
    String IS_EXPRESSION = "is([_a-zA-Z0-9].*)";
    String SETTER_EXPRESSION = "set([_a-zA-Z0-9].*)";

    /**
     * The module name of the JavaScript/TypeScript Module. This must be a java package friendly name.
     * @return The module name of the JavaScript/TypeScript Module. This must be a java package friendly name.
     */
    String moduleName();

    /**
     * The scope name of the module.
     * For example:
     *     <p><code>
     *         &#64;TSModule(<br>
     *         moduleName = "myModule",<br>
     *         moduleScope = "myScope"<br>
     *          )<br>
     *     </code></p>
     *     will result in your package.json:

     *     <p><code>
     *         {<br>
     *             "name" : "&#64;myScope/myModule"<br>
     *         }<br>
     *     </code></p>
     * @return If not null or empty: The scope name of the JavaScript/TypeScript Module. This must be a java package friendly name.
     */
    String moduleScope() default "";

    /**
     * @return The author number for the package.json file
     *
     */
    String author() default UNKNOWN;

    /**
     * @return The authorUrl number for the package.json file
     */
    String authorUrl() default UNKNOWN;

    /**
     * @return The license for the package.json file
     */
    String license() default UNKNOWN;

    /**
     * @return The description for the package.json file
     */
    String description() default UNKNOWN;


    /**
     * @return The version number for the package.json file
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
     *
     *   @return an array of custom mappings
     */
    String[] customTypeMappings() default {};

    /**
     * @return an array of regular expression to exclude type conversion.
     */
    String[] excludes() default {"^sun", "^jdk.internal", "^java.lang.Comparable"};


    /**
     * A list of name spaces, that should me mapped (shortened). The empty default maps the top level name space of the
     * directly converted types to the root name space.
     *
     *
     *  {@code "a.b.c -> " }: Maps a.b.c to root
     *
     *  @return an array of name space mappings
     *
     */
    String[] nameSpaceMapping() default {};


    /**
     *  @return the output format of the module
     */
    OutputType outputType() default OutputType.NAMESPACE_AMBIENT_TYPE;

    /**
     * @return name space mapping strategy
     */
    NameSpaceMappingStrategy nameSpaceMappingStrategy() default NameSpaceMappingStrategy.ALL_TO_ROOT;


    /**
     * @return true if generator also generates user defined type guards
     */
    boolean generateTypeGuards() default false;


    /**
     * @return a list of additional Java types, that should be included, although they're not annotated
     */
    String[] additionalTypes() default {};


    /**
     * a getter prefix is a regular expression with exactly one group that extracts the name of the property
     *
     * @return a list of getter prefixes
     */
    String[] getterPrefixes() default {GETTER_EXRPESSION, IS_EXPRESSION};

    /**
     * a setter prefix is a regular expression with exactly one group that extracts the name of the property
     *
     * @return a list of setter prefixes
     */
    String[] setterPrefixes() default {SETTER_EXPRESSION};

    /**
     * The name mapping strategy of the member names. This should match the strategy used in
     * your JSON Library. The default is NameMappingStrategy.SIMPLE
     *
     * @return the name mapping strategy
     */
    NameMappingStrategy nameMappingStrategy() default NameMappingStrategy.JACKSON_DEFAULT;

    /**
     * The export type of the enums. The default is EnumExportType.NUMERIC
     *
     * @return the enum export type
     */
    EnumExportStrategy enumExportStrategy() default EnumExportStrategy.NUMERIC;

    /**
     * Add a list of imports to your module. In combination with {@link #customTypeMappings()}, you can
     * use external types in your module.
     * <b>Please not that the &quot;import&quot; string will be prepended</b>
     *     <p>Example:<br>
     *         <code>
     *             imports = &quot;{FooClazz} from &#39;../foo/barModule&#39;&quot;
     *         </code><br>
     *             Will result in :<br>
     *         <code>
     *             import {FooClazz} from &#39;../foo/barModule&#39;;
     *         </code>
     *     </p>
     *
     * @return a list of imports, empty array by default.
     */
    String[] imports() default {};

    /**
     * This allows you to declare node module dependencies in your package json file.
     * Each entry in this list represents one dependency, including module name and version. <br>
     *     For example:
     *     <p><code>
     *         &#64;TSModule(<br>
     *         outputType = OutputType.MODULE,<br>
     *         moduleDependencies = {<br>
     *                 "\"@types/react-dom\": \"16.9.4\"",<br>
     *                 "\"react\": \"^16.12.0\"",<br>
     *                 "\"react-scripts\": \"3.2.0\""<br>
     *              }<br>
     *          )<br>
     *     </code></p>
     * @return list of dependencies as json-key-value pairs
     */
    String[] moduleDependencies() default {};

    /**
     * If not null or empty, the value if this proiperty wil be added as default export to your file.
     *
     * @return the default export
     */
    String defaultExport() default "";

    /**
     * If this is set to true, the generator will add an timestamp as comment to the generated code
     *
     * @return false by default
     */
    boolean printDate() default false;

    /**
     * If you set this to true, the generator will add the name of the original java class/interface as a comment to the
     * documentation of your element.
     *
     * @return false by default
     */
    boolean appendOriginalNamesToJavaDoc() default false;
}
