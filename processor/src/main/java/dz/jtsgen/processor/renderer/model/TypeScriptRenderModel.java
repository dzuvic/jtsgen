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

import dz.jtsgen.processor.model.TSModuleInfo;
import dz.jtsgen.processor.model.TSType;
import dz.jtsgen.processor.model.TypeScriptModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * render specific operations and queries of the model
 * <p>
 * Created by zuvic on 14.02.17.
 */
public class TypeScriptRenderModel extends TypeScriptModel {

    public TypeScriptRenderModel(TypeScriptModel model) {
        super(model);
    }

    public List<TSType> getTsTypesByModule(TSModuleInfo module) {
        return super.getTsTypes();
    }

    public Collection<TSModuleInfo> moduleList() {
        return Collections.singletonList(this.getModuleInfo());
    }
}
