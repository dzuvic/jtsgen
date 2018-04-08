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

package dz.jtsgen.processor.model;

import dz.jtsgen.annotations.NameSpaceMappingStrategy;
import dz.jtsgen.annotations.OutputType;
import dz.jtsgen.processor.util.StringUtils;
import org.immutables.value.Value;

import java.util.*;
import java.util.regex.Pattern;

import static dz.jtsgen.annotations.TSModule.*;

/**
 * Describes a JavaScript Module.
 * <p>
 * Created by zuvic on 16.02.17.
 */
@Value.Immutable
public abstract class TSModuleInfo {

    /**
     *
     * @param moduleVersion if null use current
     * @param moduleDescription if null use current
     * @param moduleAuthor if null use current
     * @param moduleLicense if null use current
     * @param moduleAuthorUrl if null use current
     * @param moduleName if null use current
     * @param outputType if null use the current
     * @return return a copy with params changed
     */
    public TSModuleInfo withModuleData(
              String moduleVersion
            , String moduleDescription
            , String moduleAuthor
            , String moduleLicense
            , String moduleAuthorUrl
            , String moduleName
            , OutputType outputType
    ) {
        return TSModuleInfoBuilder.copyOf(this)
                .withModuleName(moduleName == null ? this.getModuleName() : moduleName)
                .withModuleVersion(moduleVersion== null ? this.getModuleVersion(): moduleVersion )
                .withModuleDescription(moduleDescription== null ? this.getModuleDescription(): moduleDescription )
                .withModuleAuthor(moduleAuthor== null ? this.getModuleAuthor(): moduleAuthor )
                .withModuleLicense(moduleLicense== null ? this.getModuleLicense(): moduleLicense )
                .withModuleAuthorUrl(moduleAuthorUrl== null ? this.getModuleAuthorUrl(): moduleAuthorUrl )
                .withOutputType( outputType == null ? this.getOutputType() : outputType);
    }
    

    /**
     * @return is Module is default module
     */
    @Value.Derived
    public boolean getDefault() {
        return !this.getJavaPackage().isPresent();
    }



//    @Value.Default
    public abstract Optional<String> getUmdVariableName();

    /**
     * the module name, package name friendly, that appears also in the declared namespace
     * @return the module name
     */
    @Value.Default
    public String getModuleName() {
        return UNKNOWN;
    }

    @Value.Check
    protected void check() {
        if (! StringUtils.isPackageFriendly(this.getModuleName())) throw new IllegalArgumentException("The module name '" + this.getModuleName() + "' is not package name friendly");
    }

    /**
     * @return the module name for package.json, this must also be package name friendly, because the java compiler
     * only accepts packages names for sub dirs (as ressource)
     */
    @Value.Derived
    public String getModuleDirectoryName() {
        return StringUtils.lastOf(this.getModuleName(),"/");
    }

    @Value.Default
    public String getModuleVersion() {
        return "1.0.0";
    }

    @Value.Default
    public String getModuleDescription() {
        return UNKNOWN;
    }

    @Value.Default
    public String getModuleAuthor() {
        return UNKNOWN;
    }

    @Value.Default
    public String getModuleLicense() {
        return UNKNOWN;
    }

    @Value.Default
    public String getModuleAuthorUrl() {
        return UNKNOWN;
    }

    @Value.Default
    public Map<String,TSTargetType> getCustomMappings() {
        return Collections.unmodifiableMap(new LinkedHashMap<String,TSTargetType>());
    }

    public abstract Optional<String> getJavaPackage();

    @Value.Default
    public List<Pattern> getExcludes() {
        return Collections.unmodifiableList(new ArrayList<>());
    }

    @Value.Default
    public List<NameSpaceMapping> getNameSpaceMappings() {
        return Collections.unmodifiableList(new ArrayList<>());
    }

    @Value.Default
    public OutputType getOutputType() {
        return OutputType.NAMESPACE_AMBIENT_TYPE;
    }

    @Value.Default
    public NameSpaceMappingStrategy getNameSpaceMappingStrategy() {
        return NameSpaceMappingStrategy.ALL_TO_ROOT;
    }

    @Value.Default
    public boolean generateTypeGuards() {
        return false;
    }

    @Value.Default
    public List<String> additionalTypes() {
        return Collections.unmodifiableList(new ArrayList<>());
    }

    @Value.Default
    public List<String> getterPrefixes() {
        return Collections.unmodifiableList(Arrays.asList(GETTER_EXRPESSION, IS_EXPRESSION));
    }

    @Value.Default
    public List<String> setterPrefixes() {
        return Collections.unmodifiableList(Collections.singletonList(SETTER_EXPRESSION));
    }
}
