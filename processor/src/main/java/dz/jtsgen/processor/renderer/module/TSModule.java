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

package dz.jtsgen.processor.renderer.module;

import dz.jtsgen.processor.model.TSType;
import dz.jtsgen.processor.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * describes the content of a module
 * 
 * Created by zuvic on 16.02.17.
 */
public class TSModule {

    private final String moduleName;
    private final List<TSType> forModule;

    private String moduleVersion="1.0.0";
    private String moduleDescription="unknown";
    private String moduleAuthor="unknown";
    private String moduleLicense="unknown";
    private String moduleAuthorUrl="unknown";
    private String umdVariableName = null;

    public TSModule(String moduleName, List<TSType> forModule) {
        assert StringUtils.isPackageFriendly(moduleName);
        this.moduleName = moduleName;
        this.forModule = forModule;
    }

    /** the module name, package name friendly, that appears also in the declared namespace */
    public String getModuleName() {
        return moduleName;
    }

    /**
    * @return the module name for package.json, this must also be package name friendly, because the java compiler
    *         only accepts packages names for sub dirs (as ressource)
    *
    */
    public String getModuleDirectoryName() {
        return moduleName;
    }


    public List<TSType> getForModule() {
        return forModule;
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
        return StringUtils.camelCaseToDash(moduleName) + ".d.ts";
    }

    public String getModuleAuthorUrl() {
        return moduleAuthorUrl;
    }

    public Optional<String> getUmdVariableName() {
        return Optional.ofNullable(umdVariableName);
    }

}
