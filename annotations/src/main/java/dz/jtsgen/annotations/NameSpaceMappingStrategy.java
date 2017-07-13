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

/** defines the name space mapping strategy  */
public enum NameSpaceMappingStrategy {

    /** Maps the top level types to the root. other types are kept in their name spaces */
    TOP_LEVEL_TO_ROOT,

    /** try map everything to the root name space, conflicting names are left in their name space */
    ALL_TO_ROOT,

    /** no name space mapping is calculated */
    MANUAL
}
