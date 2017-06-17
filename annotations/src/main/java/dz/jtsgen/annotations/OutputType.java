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

/**
 *  The type of the output.
 */
public enum OutputType {

    /** exports a namespace module (d.ts and package.json) with a declared name space, e.g. declare namespace */
    NAMESPACE_AMBIENT_TYPE,

    /** only the declared namespace in a single d.ts file */
    NAMESPACE_FILE,

    /** exports a external declared module, e.g. using declare module at top*/
    MODULE,

    /** exports a single file external with types and without module definition */
    NO_MODULE,


}
