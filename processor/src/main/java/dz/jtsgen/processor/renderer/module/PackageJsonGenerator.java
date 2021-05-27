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

import dz.jtsgen.processor.model.TSModuleInfo;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;

import java.util.List;

import static dz.jtsgen.processor.helper.IdentHelper.identPrefix;

final class PackageJsonGenerator {

    static String packageJsonFor(TSModuleInfo module, TypeScriptRenderModel model) {
        StringBuilder builder = new StringBuilder(200);
        String moduleName;
        if(module.getModuleScope() != null && !module.getModuleScope().isEmpty()) {
            moduleName = "@" + module.getModuleScope() + "/" + module.getModuleName();
        }
        else {
            moduleName = module.getModuleName();
        }
        builder.append("{").append(System.lineSeparator());
        addline(builder,"name", moduleName);
        addline(builder,"version",module.getModuleVersion());
        addline(builder,"description",module.getModuleDescription());
        addline(builder,"main", model.externalModuleNameByOutputType());
        addline(builder,"author",module.getModuleAuthor());
        addline(builder,"authorUrl",module.getModuleAuthorUrl());
        addline(builder,"license",module.getModuleLicense());
        addline(builder,"typings", model.ambientFileNameByOutputType());
        addObject(builder,"dependencies", printDependencies(module.getDependencies()), true);
        addObject(builder,"scripts", "", false);
        
        return builder.append("}").toString();
    }

    private static String printDependencies(List<String> dependencies) {
        if(dependencies == null || dependencies.isEmpty()) {
            return "";
        }
        return "\n\t" + String.join(",\n\t", dependencies.toArray(new String[0])) + "\n  ";
    }

    private static void addObject(StringBuilder builder, String name, String value, boolean komma) {
        builder.append(identPrefix(1)).append("\"").append(jsonString(name)).append("\"").append(":").append(" ")
          .append("{").append(value).append("}").append(komma?",":"").append(System.lineSeparator());
    }

    private static void addline(StringBuilder builder, String name, String value) {
        builder.append(identPrefix(1)).append("\"").append(jsonString(name)).append("\"").append(":").append(" ")
          .append("\"").append(jsonString(value)).append("\",").append(System.lineSeparator());
    }

    static private String jsonString(String content) {
        return content.replaceAll("\\\\","\\\\").replaceAll("\\\"","\"");
    }
}
