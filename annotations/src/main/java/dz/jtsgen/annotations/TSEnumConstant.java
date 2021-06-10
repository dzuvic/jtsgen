/*
 * Copyright 2021 Dominik Scholl
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
 * With this annotation, you can override the exported value of enum constants, if the export strategy is {@link EnumExportStrategy#STRING}.
 * This is useful if you want to define a set of strings as enum that has developer friendly constant names.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = {ElementType.FIELD})
public @interface TSEnumConstant {

    /**
     * Change this if the value of your enum constant in java is not the same like the final name in javascript.
     *
     * @return the custom constant name, or empty string if {@link Enum#name()} should be used to determine the string value-
     */
    String value() default "";

}
