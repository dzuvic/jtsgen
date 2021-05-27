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
 * With this annotation, you can export constants from your java code as typescript constants.
 * This annotation works only on fields of <b>primitive types and String</b> that are declared as <b>final</b>.
 * If the target TS type is an interface, the constants will be generated in alphabetical order right before the interface declaration.
 * You can use {@link #name()} to avoid name clashes if you have constants from multiple classes to export.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = {ElementType.FIELD})
public @interface TSConstant {

    /**
     * Change this if the name of your constant in java is not the same like the final name in javascript.
     *
     * @return the custom constant name, or empty sting if the actual property name should be used
     */
    String name() default "";

}
