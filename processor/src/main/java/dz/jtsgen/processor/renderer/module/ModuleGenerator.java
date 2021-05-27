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

import static dz.jtsgen.annotations.OutputType.MODULE;
import static dz.jtsgen.annotations.OutputType.NAMESPACE_AMBIENT_TYPE;

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

    public void writeModule() {
        try {
            if (model.getOutputType() == NAMESPACE_AMBIENT_TYPE || model.getOutputType() == MODULE) {
                writePackageJson();
                writeReadmeMd();
            }
            tsdGenerator.writeTypes();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Caught Exception", e);
            this.env.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not write output file(s) " + e.getMessage());
        }
    }




    private void writePackageJson() throws IOException {
        String packageJson = PackageJsonGenerator.packageJsonFor(model.getModuleInfo(), model);
        FileObject package_json_file_object = ModuleResourceHelper.createResource(env, model.getModuleInfo(), "package.json");

        try (PrintWriter out = new PrintWriterWithLogging(package_json_file_object.openWriter(), "package.json")) {
            out.println(packageJson);
        }
    }

    private void writeReadmeMd() throws IOException {
        String readmeMd = model.getModuleInfo().getJavadoc();

        if(readmeMd == null || readmeMd.trim().isEmpty()) {
            return;
        }

        readmeMd = readmeMd.trim();

        if(readmeMd.isEmpty()) {
            return;
        }

        FileObject readme_md_file_object = ModuleResourceHelper.createResource(env, model.getModuleInfo(), "readme.md");

        try (PrintWriter out = new PrintWriterWithLogging(readme_md_file_object.openWriter(), "readme.md")) {
            out.println(readmeMd);
        }
    }

}
