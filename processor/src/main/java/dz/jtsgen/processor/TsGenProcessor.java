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

import com.google.auto.service.AutoService;
import dz.jtsgen.annotations.TSIgnore;
import dz.jtsgen.annotations.TSModule;
import dz.jtsgen.annotations.TypeScript;
import dz.jtsgen.processor.jtp.TSModuleHandler;
import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.renderer.TSRenderer;
import dz.jtsgen.processor.visitors.TSAVisitor;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.model.TypeScriptModel.newModelWithDefaultModule;
import static java.util.logging.Level.INFO;

/**
 * The main processor for generating the ambient typescript types.
 *
 * @author dzuvic
 */
@AutoService(Processor.class)
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
                            final Optional<? extends TypeElement> annotation = annotations.stream().filter((y) -> y.getSimpleName().contentEquals(x.getSimpleName())).findFirst();
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

    private void processElements(TypeElement annotation, RoundEnvironment roundEnv) {
        LOG.fine( () -> String.format("P: Processing Annotation %s", annotation.getSimpleName()));
        if (annotation.getSimpleName().contentEquals(TypeScript.class.getSimpleName())) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(TypeScript.class).stream().filter(
                    (ignoring) -> ignoring.getAnnotationMirrors().stream().noneMatch((y) -> {
                        return TSIgnore.class.getSimpleName().equals(y.getAnnotationType().asElement().getSimpleName().toString());
                    })
            ).collect(Collectors.toSet());
            final TSAVisitor typeScriptAnnotationVisitor = new TSAVisitor(typeScriptModel, this.processingEnv);
            for (Element e : annotatedElements) {
                typeScriptModel.addTSTypes(typeScriptAnnotationVisitor.visit(e, new TSAVisitorParam(annotation, this.processingEnv, typeScriptModel)));
            }
        } else if (annotation.getSimpleName().contentEquals(TSModule.class.getSimpleName())) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(TSModule.class);
            if (annotatedElements.size() > 1) annotatedElements.forEach( x -> this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,"Multiple TSModule not supported. Multiple Modules with same ", x));
            new TSModuleHandler(this.processingEnv).process(annotatedElements).stream().findFirst().ifPresent(this.typeScriptModel::addModuleInfo);
        }
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
