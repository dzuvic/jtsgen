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

package dz.jtsgen.processor.jtp;

import dz.jtsgen.processor.model.NameSpaceMapping;
import dz.jtsgen.processor.model.TSModuleInfo;
import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.nsmap.NameSpaceMapperCalculator;
import dz.jtsgen.processor.util.Tuple;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.nsmap.NameSpaceMapperCalculator.computeNameSpaceMapping;
import static dz.jtsgen.processor.util.NameSpaceHelper.topPackages;
import static dz.jtsgen.processor.util.StringUtils.dotToUpperCamelCase;
import static dz.jtsgen.processor.util.StringUtils.isPackageFriendly;

/**
 * This Helper generates a TSModuleInfo enriched by the information added in later stages
 */
public class TSModuleInfoEnforcer {

    private static final String JTSGEN_OUTPUT_OPTION_MODULENAME = "jtsgenModuleName";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEVERSION =  "jtsgenModuleVersion";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEDESCRIPTION =  "jtsgenModuleDescription";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEAUTHOR =  "jtsgenModuleAuthor";
    private static final String JTSGEN_OUTPUT_OPTION_MODULELICENSE =  "jtsgenModuleLicense";
    private static final String JTSGEN_OUTPUT_OPTION_MODULEAUTHORURL =  "jtsgenModuleAuthorUrl";

    private static Logger LOG = Logger.getLogger(TSModuleInfoEnforcer.class.getName());


    private  final ProcessingEnvironment env;
    private final TypeScriptModel model;

    public TSModuleInfoEnforcer(ProcessingEnvironment env, TypeScriptModel model) {
        this.env = env;
        this.model = model;
    }


    /**
     * @return the module with generated/corrected name
     */
    private TSModuleInfo correctModule(List<NameSpaceMapping> nsMapping, String notOverridenModuleName) {
        String moduleName = Optional.ofNullable(env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULENAME)).orElse(notOverridenModuleName);
        String moduleVersion = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEVERSION);
        String moduleDescription = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEDESCRIPTION);
        String moduleAuthor = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEAUTHOR);
        String moduleLicense = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULELICENSE);
        String moduleAuthorUrl = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULEAUTHORURL);
        return new TSModuleInfo(this.model.getModuleInfo())
                .withModuleData(moduleVersion, moduleDescription, moduleAuthor, moduleLicense, moduleAuthorUrl, moduleName, this.model.getModuleInfo().getOutputType())
                .withNameSpaceMapping(nsMapping);
    }

    /**
     * @return the module name of all type script types, that are not bound to any package module
     */
    private Optional<String> mainModuleName(Set<? extends Element> annotatedElements) {
        if (env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULENAME) != null) {
            String moduleName = env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULENAME);
            if (isPackageFriendly(moduleName)) return Optional.of(env.getOptions().get(JTSGEN_OUTPUT_OPTION_MODULENAME));
            env.getMessager().printMessage(Diagnostic.Kind.ERROR,"the module name for the default module specified using the jtsgenModuleName option is not a valid package name. Unfortunately the java compiler need a valid package name when creating a ressource");
            return Optional.empty();
        }
        if (! model.getModuleInfo().isDefault()) {
            return Optional.of(model.getModuleInfo().getModuleName());
        }
        Set<String> commonOrTop = topPackages(NameSpaceMapperCalculator.typesWithPackageNames(annotatedElements).stream().map(Tuple::getFirst).collect(Collectors.toList()));
        LOG.finest(() -> "TSR common: " + (commonOrTop==null?"null": commonOrTop.toString() + ", size:"+commonOrTop.size()));
        if (commonOrTop == null || commonOrTop.size() != 1) {
            env.getMessager().printMessage(Diagnostic.Kind.WARNING,"module name could not be determined automatically, using 'UnknownModule'. Specify the name for the default module using (-ajtsgenModuleName) or create common super package");
            return Optional.of("Unknown");
        }
        final String moduleName = commonOrTop.iterator().next();
        if ("".equals(moduleName)) {
            env.getMessager().printMessage(Diagnostic.Kind.WARNING,"determined module name is empty, using 'UnknownModule' as module name. Default module name can be set using (-ajtsgenModuleName) or using a common super package");
            return Optional.of("Unknown");
        }
        return Optional.of(dotToUpperCamelCase(moduleName));
    }

    // TODO: change this, in a later iteration the namespaces must be mapped by the renderer. It is not possible
    // TODO: to calculate the namespace mapping, when adding super/embedded types.all types must be converted to calculate the name space mapping
    public Optional<TSModuleInfo> createUpdatedTSModuleInfo(Set<? extends Element> annotatedElements) {

        final List<NameSpaceMapping> nameSpaceMappings = (this.model.usesDefaultNameSpaceMapping()) ?
                computeNameSpaceMapping(annotatedElements)
                : new ArrayList<>();

        return mainModuleName(annotatedElements).flatMap( x -> Optional.of(correctModule(nameSpaceMappings,x)));
    }
}
