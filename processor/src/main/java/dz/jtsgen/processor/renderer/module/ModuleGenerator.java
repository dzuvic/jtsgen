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

import dz.jtsgen.annotations.OutputType;
import dz.jtsgen.processor.model.TSModuleInfo;
import dz.jtsgen.processor.renderer.helper.ModuleResourceHelper;
import dz.jtsgen.processor.renderer.helper.PrintWriterWithLogging;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;
import dz.jtsgen.processor.renderer.module.tsd.TSDGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * exports a  module
 * <p>
 * Created by zuvic on 16.02.17.
 */
public final class ModuleGenerator {



    private static Logger LOG = Logger.getLogger(ModuleGenerator.class.getName());

    private TypeScriptRenderModel model;
    private ProcessingEnvironment env;
    private final TSDGenerator tsdGenerator;

    public ModuleGenerator(TypeScriptRenderModel model, ProcessingEnvironment env) {
        this.model = model;
        this.env = env;
        this.tsdGenerator = new TSDGenerator(model, env);
    }

    public void writeModule(TSModuleInfo moduleInfo) {
        try {
            if (moduleInfo.getOutputType() == OutputType.TS_MODULE_DECLARED_NAMESPACE) writePackageJson(moduleInfo);
            tsdGenerator.writeTypes(moduleInfo);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Caught Exception", e);
            this.env.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not write output file(s) " + e.getMessage());
        }
    }




    private void writePackageJson(TSModuleInfo module) throws IOException {
        String packageJson = PackageJsonGenerator.packageJsonFor(module, model);
        FileObject package_json_file_object = ModuleResourceHelper.createResource(env, module, "package.json");

        try (PrintWriter out = new PrintWriterWithLogging(package_json_file_object.openWriter(), "package.json")) {
            out.println(packageJson);
        }
    }

}
