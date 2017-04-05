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
import dz.jtsgen.annotations.TypeScript;
import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.renderer.TSRenderer;
import dz.jtsgen.processor.visitors.TSAVisitor;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dz.jtsgen.processor.model.TypeScriptModel.newModel;
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
        "jtsgenModuleName"
})
public class TsGenProcessor extends AbstractProcessorWithLogging {

    private static Logger LOG = Logger.getLogger(TsGenProcessor.class.getName());

    private TypeScriptModel typeScriptModel = newModel();

    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        LOG.log(INFO, () -> String.format("Processing Annotations %s (isOver=%b)", annotations, roundEnv.processingOver()));


        if (roundEnv.processingOver() && !roundEnv.errorRaised()) {
            try {
                LOG.log(Level.INFO, () -> "P: processing over");
                new TSRenderer(processingEnv, typeScriptModel).writeFiles();
            } catch (Exception e) {
                internalError(e);
                throw e;
            }
        } else if (roundEnv.processingOver() && roundEnv.errorRaised()) {
            LOG.log(Level.INFO, () -> "P: processing over. error raised. nothing to do");
        } else {
            try {
                for (TypeElement annotation : annotations) {
                    if (annotation.getSimpleName().contentEquals(TypeScript.class.getSimpleName())) {
                        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(TypeScript.class);
                        final TSAVisitor typeScriptAnnotationVisitor = new TSAVisitor(typeScriptModel, this.processingEnv);
                        for (Element e : annotatedElements) {
                            typeScriptModel.addTSTypes(typeScriptAnnotationVisitor.visit(e, new TSAVisitorParam(annotation, this.processingEnv, typeScriptModel)));
                        }
                    }
                }
            } catch (Exception e) {
                internalError(e);
                throw e;
            }
        }

        return true;
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void internalError(Exception e) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,"internal error in jtsgen " + e);
        System.out.println("internal error in jtsgen");
        e.printStackTrace();
    }

}
