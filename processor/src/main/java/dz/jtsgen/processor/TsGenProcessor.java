/*
 * Copyright 2016 Dragan Zuvic
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

package dz.jtsgen.processor;

import dz.jtsgen.annotations.TSModule;
import dz.jtsgen.annotations.TypeScript;
import dz.jtsgen.processor.jtp.TSModuleHandler;
import dz.jtsgen.processor.jtp.TSModuleInfoEnforcer;
import dz.jtsgen.processor.jtp.conv.JavaTypeProcessor;
import dz.jtsgen.processor.jtp.conv.TSProcessingInfo;
import dz.jtsgen.processor.jtp.conv.TSProcessingInfoBuilder;
import dz.jtsgen.processor.jtp.conv.TypeScriptAnnotationProcessor;
import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.renderer.TSRenderer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static dz.jtsgen.processor.jtp.helper.RoundEnvHelper.filteredTypeSriptElements;
import static dz.jtsgen.processor.model.TypeScriptModel.newModelWithDefaultModule;
import static java.util.logging.Level.INFO;

/**
 * The main processor for generating the ambient typescript types.
 *
 * @author dzuvic
 */
@SupportedAnnotationTypes(value = {"dz.jtsgen.annotations.*"})
@SupportedOptions({
        "jtsgenLogLevel",
        "jtsgenModuleName",
        "jtsgenModuleVersion",
        "jtsgenModuleDescription",
        "jtsgenModuleAuthor",
        "jtsgenModuleLicense",
        "jtsgenModuleAuthorUrl"
})
public class TsGenProcessor extends AbstractProcessorWithLogging {

    // Order of annotations to process
    private static final List<Class<?>> PROCESSING_ORDER = Arrays.asList(TSModule.class, TypeScript.class);

    private static Logger LOG = Logger.getLogger(TsGenProcessor.class.getName());

    final private TypeScriptModel typeScriptModel = newModelWithDefaultModule();

    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            LOG.log(INFO, () -> String.format("P: Processing Annotations %s (isOver=%b)", annotations, roundEnv.processingOver()));
            if (roundEnv.processingOver() && !roundEnv.errorRaised()) {
                LOG.info(() -> "P: processing over. Start rendering.");
                new TSRenderer(processingEnv, typeScriptModel).writeFiles();
            } else if (roundEnv.processingOver() && roundEnv.errorRaised()) {
                LOG.info(() -> "P: processing over. error raised. nothing to do");
            } else {
                PROCESSING_ORDER.forEach(
                        (x) -> {
                            final Optional<? extends TypeElement> annotation = annotations
                                    .stream()
                                    .filter((y) -> y.getSimpleName().contentEquals(x.getSimpleName()))
                                    .findFirst();
                            LOG.finest(() -> String.format("P: Annotation %s member %s", x, annotation.isPresent()));
                            annotation.ifPresent(typeElement -> processElements(typeElement, roundEnv));
                        }
                );
            }
        } catch (Exception e) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "internal error in jtsgen " + e);
            System.err.println("FATAL error in jtsgen. Please file an issue with the following stack trace @ https://github.com/dzuvic/jtsgen/issues");
            e.printStackTrace();
        }

        return true;
    }

    // process all annotations
    private void processElements(TypeElement annotation, RoundEnvironment roundEnv) {
        LOG.info( () -> String.format("P: Processing Annotation %s", annotation.getSimpleName()));
        if (annotation.getSimpleName().contentEquals(TypeScript.class.getSimpleName())) {
            processTypeScriptAnnotation(annotation, roundEnv);
        } else if (annotation.getSimpleName().contentEquals(TSModule.class.getSimpleName())) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(TSModule.class);
            if (annotatedElements.size() > 1) annotatedElements.forEach(
                    x -> this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,"Multiple TSModule not supported. Multiple Modules with same ", x));
            new TSModuleHandler(this.processingEnv)
                    .process(annotatedElements)
                    .stream()
                    .findFirst()
                    .ifPresent(this.typeScriptModel::addModuleInfo);
        }
    }

    // process TypeScript Annotation this is after processing @TSModule
    private void processTypeScriptAnnotation(TypeElement annotation, RoundEnvironment roundEnv) {

        // ignore classes with TSIgnore
        Set<Element> annotatedElements = filteredTypeSriptElements(roundEnv);

        // this is needed for updating data from CLI and calculating a name space mapping, if needed
        new TSModuleInfoEnforcer(this.processingEnv,this.typeScriptModel).createUpdatedTSModuleInfo(annotatedElements).ifPresent( x -> {
            typeScriptModel.addModuleInfo(x);
            final TSProcessingInfo TSProcessingInfo = TSProcessingInfoBuilder.of(annotation, this.processingEnv, typeScriptModel) ;// new TSProcessingInfoImpl(annotation, this.processingEnv, typeScriptModel);
            final JavaTypeProcessor handler = new TypeScriptAnnotationProcessor(TSProcessingInfo);
            handler.processAnnotations(roundEnv);
        });
    }



    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
