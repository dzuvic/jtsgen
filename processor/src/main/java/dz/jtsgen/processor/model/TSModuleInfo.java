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

import dz.jtsgen.annotations.OutputType;
import dz.jtsgen.processor.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

import static dz.jtsgen.processor.util.StringUtils.camelCaseToDash;

/**
 * Describes a JavaScript Module.
 * <p>
 * Created by zuvic on 16.02.17.
 */
public final class TSModuleInfo {

    private final String moduleName;
    private final String javaPackage;
    private final String moduleVersion;
    private final String moduleDescription;
    private final String moduleAuthor;
    private final String moduleLicense;
    private final String moduleAuthorUrl;
    private final String umdVariableName;
    private Map<String,TSTargetType> customMappings;
    private List<Pattern> excludes;
    private List<NameSpaceMapping> nameSpaceMappings;
    private final OutputType outputType;


    public TSModuleInfo(String moduleName, String javaPackage
    ) {
        this(moduleName, javaPackage, null, null, null, null, null, null, null, null, null, null);
    }

    // copy constructor
    public TSModuleInfo(TSModuleInfo module) {
        this(
                module.moduleName,
                module.javaPackage,
                module.moduleVersion,
                module.moduleDescription,
                module.moduleAuthor,
                module.moduleLicense,
                module.moduleAuthorUrl,
                module.umdVariableName,
                module.customMappings,
                module.excludes,
                module.nameSpaceMappings,
                module.outputType
        );
    }

    /**
     *
     * @param moduleVersion if null use current
     * @param moduleDescription if null use current
     * @param moduleAuthor if null use current
     * @param moduleLicense if null use current
     * @param moduleAuthorUrl if null use current
     * @param moduleName if null use current
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
        return new TSModuleInfo(
                moduleName == null ? this.moduleName : moduleName ,
                this.javaPackage,
                moduleVersion== null ? this.moduleVersion : moduleVersion ,
                moduleDescription== null ? this.moduleDescription : moduleDescription ,
                moduleAuthor== null ? this.moduleAuthor : moduleAuthor ,
                moduleLicense== null ? this.moduleLicense : moduleLicense ,
                moduleAuthorUrl== null ? this.moduleAuthorUrl : moduleAuthorUrl ,
                this.umdVariableName, this.customMappings, this.excludes, this.nameSpaceMappings, outputType
        );
    }

    public TSModuleInfo withTypeMappingInfo(Map<String, TSTargetType> mapping, List<Pattern> excludes, List<NameSpaceMapping> nameSpaceMappings) {
        return new TSModuleInfo(this.moduleName, this.javaPackage,
                       this.moduleVersion, this.moduleDescription, this.moduleAuthor, this.moduleLicense, this.moduleAuthorUrl,
                       this.umdVariableName, mapping, excludes, nameSpaceMappings, this.outputType);
    }

    /**
     *
     * @param nameSpaceMappings if null or empty use current
     * @return copy with changed value
     */
    public TSModuleInfo withNameSpaceMapping(List<NameSpaceMapping> nameSpaceMappings) {
        return this.withTypeMappingInfo(this.customMappings, this.excludes,
                (nameSpaceMappings == null || nameSpaceMappings.isEmpty()) ? this.nameSpaceMappings : nameSpaceMappings);
    }

    // the author was just too lazy writing a builder for this type...
    private TSModuleInfo(
            String moduleName
            , String javaPackage
            , String moduleVersion
            , String moduleDescription
            , String moduleAuthor
            , String moduleLicense
            , String moduleAuthorUrl
            , String umdVariableName
            , Map<String,TSTargetType> customMappings
            , List<Pattern> excludes
            , List<NameSpaceMapping> nameSpaceMappings
            , OutputType outputType

    ) {
        assert StringUtils.isPackageFriendly(moduleName);
        this.moduleName = moduleName;

        //Optional
        this.javaPackage = javaPackage;
        this.umdVariableName = umdVariableName;

        this.moduleVersion = moduleVersion == null ? "1.0.0" : moduleVersion;
        this.moduleDescription = moduleDescription == null ? "unknown" : moduleDescription;
        this.moduleAuthor = moduleAuthor == null ? "unknown" : moduleAuthor;
        this.moduleLicense = moduleLicense == null ? "unknown" : moduleLicense;
        this.moduleAuthorUrl = moduleAuthorUrl == null ? "unknown" : moduleAuthorUrl;
        this.outputType = outputType == null ? OutputType.TS_MODULE_DECLARED_NAMESPACE : outputType;

        final Map<String,TSTargetType> customMappingsCopy=new LinkedHashMap<>();
        final List<Pattern> excludesCopy = new ArrayList<>();
        final List<NameSpaceMapping> nameSpaceMappingCopy = new LinkedList<>();

        if (customMappings != null) customMappingsCopy.putAll(customMappings);
        if (excludes != null) excludesCopy.addAll(excludes);
        if (nameSpaceMappings != null) nameSpaceMappingCopy.addAll(nameSpaceMappings);

        this.customMappings=Collections.unmodifiableMap(customMappingsCopy);
        this.excludes=Collections.unmodifiableList(excludesCopy);
        this.nameSpaceMappings=Collections.unmodifiableList(nameSpaceMappingCopy);


    }

    /**
     * @return is Module is default module
     */
    public boolean isDefault() {
        return this.javaPackage == null;

    }


    public Optional<String> getJavaPackage() {
        return Optional.ofNullable(this.javaPackage);
    }

    public Optional<String> getUmdVariableName() {
        return Optional.ofNullable(umdVariableName);
    }

    /**
     * the module name, package name friendly, that appears also in the declared namespace
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @return the module name for package.json, this must also be package name friendly, because the java compiler
     * only accepts packages names for sub dirs (as ressource)
     */
    public String getModuleDirectoryName() {
        return moduleName;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public String getModuleAuthor() {
        return moduleAuthor;
    }

    public String getModuleLicense() {
        return moduleLicense;
    }

    /**
     * @return the filename of the typings file.
     */
    public String getModuleTyingsFile() {
        return camelCaseToDash(moduleName) + ".d.ts";
    }

    public String getModuleAuthorUrl() {
        return moduleAuthorUrl;
    }

    public Map<String,TSTargetType> getCustomMappings() {
        return customMappings;
    }

    public List<Pattern> getExcludes() {
        return excludes;
    }

    public List<NameSpaceMapping> getNameSpaceMappings() {
        return nameSpaceMappings;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TSModuleInfo)) return false;
        TSModuleInfo that = (TSModuleInfo) o;
        return Objects.equals(getModuleName(), that.getModuleName()) &&
                Objects.equals(getJavaPackage(), that.getJavaPackage()) &&
                Objects.equals(getModuleVersion(), that.getModuleVersion()) &&
                Objects.equals(getModuleDescription(), that.getModuleDescription()) &&
                Objects.equals(getModuleAuthor(), that.getModuleAuthor()) &&
                Objects.equals(getModuleLicense(), that.getModuleLicense()) &&
                Objects.equals(getModuleAuthorUrl(), that.getModuleAuthorUrl()) &&
                Objects.equals(getOutputType(), that.getOutputType()) &&
                Objects.equals(getUmdVariableName(), that.getUmdVariableName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModuleName(), getJavaPackage(), getModuleVersion(), getModuleDescription(), getModuleAuthor(), getModuleLicense(), getModuleAuthorUrl(), getUmdVariableName(), getOutputType());
    }
}
