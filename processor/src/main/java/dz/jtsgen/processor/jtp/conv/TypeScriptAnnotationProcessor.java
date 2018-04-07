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
package dz.jtsgen.processor.jtp.conv;

import dz.jtsgen.processor.helper.Sets;
import dz.jtsgen.processor.jtp.conv.visitors.JavaTypeConverter;
import dz.jtsgen.processor.jtp.conv.visitors.TSAVisitor;
import dz.jtsgen.processor.jtp.info.TSProcessingInfo;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dz.jtsgen.processor.jtp.helper.RoundEnvHelper.filteredTypeSriptElements;
import static java.util.Collections.singletonList;

/**
 * creates interface models for each java interface or class.
 *
 * @author dzuvic initial
 * @author dzuvic renamed to TypeScriptAnnotationProcessor
 */
public class TypeScriptAnnotationProcessor implements JavaTypeProcessor {

    private static Logger LOG = Logger.getLogger(TypeScriptAnnotationProcessor.class.getName());

    private final TSProcessingInfo processingInfo;

    private final JavaTypeConverter javaConverter;


    public TypeScriptAnnotationProcessor(TSProcessingInfo processingInfo) {
        this.processingInfo = processingInfo;

        // this might me changed using a different converter strategy
        javaConverter = new DefaultJavaTypeConverter(processingInfo);
    }

    @Override
    public void processAnnotations(RoundEnvironment roundEnv) {
          this.processElements(
                Sets.union(
                        this.processingInfo.additionalTypesToConvert(),
                        filteredTypeSriptElements(roundEnv)
                ));
    }


    @Override
    public void processElements(Set<Element> elements) {
        TSAVisitor tsaVisitor = new TSAVisitor();
        for (Element e : elements) {
            tsaVisitor.visit(e, javaConverter).ifPresent(x -> {
                        processingInfo.getTsModel().addTSTypes(singletonList(x));
                        LOG.log(Level.FINEST, () -> String.format("TSAP added %s to model", x.toString()));
                    }
            );
        }
    }
}
