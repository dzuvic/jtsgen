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
import dz.jtsgen.processor.renderer.helper.ModuleResourceHelper;
import dz.jtsgen.processor.renderer.helper.PrintWriterWithLogging;
import dz.jtsgen.processor.renderer.model.TypeScriptRenderModel;
import dz.jtsgen.processor.renderer.module.tsd.TSDGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dz.jtsgen.processor.renderer.query.PackageQuery.topPackages;
import static dz.jtsgen.processor.util.StringUtils.dotToUpperCamelCase;
import static dz.jtsgen.processor.util.StringUtils.isPackageFriendly;

/**
 * exports a  module
 * <p>
 * Created by zuvic on 16.02.17.
 */
public final class ModuleGenerator {

    private static final String JTSGEN_OUTPUT_OPTION = "jtsgenModuleName";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEVERSION =  "jtsgenModuleVersion";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEDESCRIPTION =  "jtsgenModuleDescription";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEAUTHOR =  "jtsgenModuleAuthor";
    private static final String JTSGEN_OUTPUT_OPTION_MODULELICENSE =  "jtsgenModuleLicense";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEAUTHORURL =  "jtsgenModuleAuthorUrl";

    private static Logger LOG = Logger.getLogger(ModuleGenerator.class.getName());

    private TypeScriptRenderModel model;
    private ProcessingEnvironment env;
    private final TSDGenerator tsdGenerator;

    public ModuleGenerator(TypeScriptRenderModel model, ProcessingEnvironment env) {
        this.model = model;
        this.env = env;
        this.tsdGenerator = new TSDGenerator(model, env);
    }

    public void writeModule(TSModuleInfo module) {
        correctModule(module).ifPresent(moduleInfo -> {
            try {
                writePackageJson(moduleInfo);
                tsdGenerator.writeTypes(moduleInfo);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Caught Exception", e);
                this.env.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not write output file(s) " + e.getMessage());
            }
        });

    }

    /**
     * @return the module name of all type script types, that are not bound to any package module
     */
    public Optional<String> mainModuleName() {
        if (env.getOptions().get(JTSGEN_OUTPUT_OPTION) != null) {
            String moduleName = env.getOptions().get(JTSGEN_OUTPUT_OPTION);
            if (isPackageFriendly(moduleName)) return Optional.of(env.getOptions().get(JTSGEN_OUTPUT_OPTION));
            env.getMessager().printMessage(Diagnostic.Kind.ERROR,"the module name for the default module specified using the jtsgenModuleName option is not a valid package name. Unfortunately the java compiler need a valid package name when creating a ressource");
            return Optional.empty();
        }
        List<String> commonOrTop = topPackages(model);
        LOG.finest(() -> "TSR common: " + (commonOrTop==null?"null": commonOrTop.toString() + ", size:"+commonOrTop.size()));
        if (commonOrTop == null || commonOrTop.size() != 1) {
            env.getMessager().printMessage(Diagnostic.Kind.ERROR,"module name could not be determined automatically. Specify the name for the default module using (-ajtsgenModuleName) or create common super package");
            return Optional.empty();
        }
        final String moduleName = commonOrTop.iterator().next();
        if ("".equals(moduleName)) {
            env.getMessager().printMessage(Diagnostic.Kind.WARNING,"determined module name is empty, using 'UnknownModule' as module name. Default module name can be set using (-ajtsgenModuleName) or using a common super package");
            return Optional.of("Unknown");
        }
        return Optional.of(dotToUpperCamelCase(moduleName));
    }

    /**
     * @param module original module
     * @return the module with generated/corrected name
     */
    private Optional<TSModuleInfo> correctModule(TSModuleInfo module) {
        if (! module.isDefault() ) return Optional.of(module);
        Optional<String> mainModuleName = mainModuleName();
        if ( ! mainModuleName.isPresent()) return Optional.empty();
        String moduleVersion = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEVERSION);
        String moduleDescription = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEDESCRIPTION);
        String moduleAuthor = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEAUTHOR);
        String moduleLicense = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULELICENSE);
        String moduleAuthorUrl = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEAUTHORURL);
        return Optional.of(
                new TSModuleInfo(mainModuleName.get(),null).withModuleData(moduleVersion
                ,moduleDescription
                ,moduleAuthor
                ,moduleLicense
                ,moduleAuthorUrl)
        );
    }


    private void writePackageJson(TSModuleInfo module) throws IOException {
        String packageJson = PackageJsonGenerator.packageJsonFor(module, model);
        FileObject package_json_file_object = ModuleResourceHelper.createResource(env, module, "package.json");

        try (PrintWriter out = new PrintWriterWithLogging(package_json_file_object.openWriter(), "package.json")) {
            out.println(packageJson);
        }
    }

}
