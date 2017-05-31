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

package dz.jtsgen.processor.renderer.model;

import dz.jtsgen.annotations.OutputType;
import dz.jtsgen.processor.model.TSModuleInfo;
import dz.jtsgen.processor.model.TSType;
import dz.jtsgen.processor.model.TypeScriptModel;

import java.util.List;

import static dz.jtsgen.annotations.OutputType.EXTERNAL_MODULE;
import static dz.jtsgen.annotations.OutputType.EXTERNAL_NAMESPACE_AMBIENT_TYPE;
import static dz.jtsgen.annotations.OutputType.EXTERNAL_NAMESPACE_FILE;
import static dz.jtsgen.processor.util.StringUtils.camelCaseToDash;

/**
 * render specific operations and queries of the model
 * <p>
 * Created by zuvic on 14.02.17.
 */
public class TypeScriptRenderModel extends TypeScriptModel {

    private static final String EMPTY_FILE_NAME = "";

    public TypeScriptRenderModel(TypeScriptModel model) {
        super(model);
    }

    public List<TSType> getTsTypesByModule(TSModuleInfo module) {
        return super.getTsTypes();
    }

    public OutputType getOutputType() {
        return this.getModuleInfo().getOutputType();
    }

    /**
     * @return the filename of the typings file if output type is sufficient
     */
    public String ambientFileNameByOutputType() {
        return ( this.getOutputType() == EXTERNAL_NAMESPACE_AMBIENT_TYPE
                 || this.getOutputType() == EXTERNAL_NAMESPACE_FILE )
                ? camelCaseToDash(this.getModuleInfo().getModuleName()) + ".d.ts"
                : EMPTY_FILE_NAME;
    }

    /**
     * @return the filename of the external module file if output type is sufficient
     */
    public String externalModuleNameByOutputType() {
        return ( this.getOutputType() == EXTERNAL_MODULE )
                ? camelCaseToDash(this.getModuleInfo().getModuleName()) + ".ts"
                : EMPTY_FILE_NAME;
    }

    public String fileByOutputType() {
        return EMPTY_FILE_NAME.equals(ambientFileNameByOutputType()) ? externalModuleNameByOutputType() : ambientFileNameByOutputType();
    }

    public String moduleDeclaration() {
        StringBuilder builder = new StringBuilder();
        return builder.append("declare ").append( this.getOutputType()==EXTERNAL_MODULE? "module":"namespace").append(" ")
                .append( this.getOutputType()==EXTERNAL_MODULE? "\"":"")
                .append(this.getModuleInfo().getModuleName())
                .append( this.getOutputType()==EXTERNAL_MODULE? "\"":"")
                .append(" {").toString();
    }
}
