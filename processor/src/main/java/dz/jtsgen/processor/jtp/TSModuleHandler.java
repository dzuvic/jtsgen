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

import dz.jtsgen.annotations.TSModule;
import dz.jtsgen.processor.model.TSModuleInfo;
import dz.jtsgen.processor.model.TSTargetType;
import dz.jtsgen.processor.model.tstarget.TSTargetFactory;
import dz.jtsgen.processor.util.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.util.RegExHelper.compileToPattern;

/**
 * Handles TSModule Annotations
 */
public final class TSModuleHandler {

    private final ProcessingEnvironment env;
    private final TypeMirror annotationElement;

    public TSModuleHandler(ProcessingEnvironment processingEnv) {
        annotationElement = processingEnv.getElementUtils().getTypeElement(TSModule.class.getName()).asType();
        this.env = processingEnv;
    }

    public Set<TSModuleInfo> process(Set<? extends Element> annotatedElements) {
        return annotatedElements
                .stream()
                .map((x) -> {
                    Optional<? extends AnnotationMirror> bla = x.getAnnotationMirrors().stream().filter((y) -> y.getAnnotationType().equals(this.annotationElement)).findFirst();
                    return bla.map((y) -> {
                                final String packageName = x.toString();
                                AnnotationValue moduleName = null, version = null, author = null, authorUrl = null, description = null, license = null, customTypeMapping = null, exclAnnotationValue = null;
                                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : y.getElementValues().entrySet()) {
                                    final String simpleName = entry.getKey().getSimpleName().toString();
                                    if ("version".equals(simpleName)) version = entry.getValue();
                                    else if ("moduleName".equals(simpleName)) moduleName = entry.getValue();
                                    else if ("author".equals(simpleName)) author = entry.getValue();
                                    else if ("authorUrl".equals(simpleName)) author = entry.getValue();
                                    else if ("description".equals(simpleName)) description = entry.getValue();
                                    else if ("authorUrl".equals(simpleName)) authorUrl = entry.getValue();
                                    else if ("license".equals(simpleName)) license = entry.getValue();
                                    else if ("customTypeMappings".equals(simpleName)) customTypeMapping = entry.getValue();
                                    else if ("excludes".equals(simpleName)) exclAnnotationValue = entry.getValue();
                                }

                                final String versionString = version != null ? (String) version.getValue() : null;
                                final String descriptionString = description != null ? (String) description.getValue() : null;
                                final String authorString = author != null ? (String) author.getValue() : null;
                                final String authorUrlString = authorUrl != null ? (String) authorUrl.getValue() : null;
                                final String licenseString = license != null ? (String) license.getValue() : null;
                                final String moduleNameString = moduleName != null ? (String) moduleName.getValue() : null;
                                if (moduleNameString == null || moduleNameString.isEmpty() || !StringUtils.isPackageFriendly(moduleNameString)) {
                                    this.env.getMessager().printMessage(Diagnostic.Kind.ERROR, "The module name '" + moduleNameString + "' is not package name friendly", x);
                                    return null;
                                }
                                Map<String, TSTargetType> customTypeMappingMap = convertTypeMapping(customTypeMapping, x);
                                List<Pattern> exclusionList = convertExclusion(exclAnnotationValue, x);
                                return new TSModuleInfo((String) moduleName.getValue(), packageName)
                                        .withModuleData(versionString, descriptionString, authorString, authorUrlString, licenseString)
                                        .withTypeMappingInfo(customTypeMappingMap, exclusionList);
                            }
                    );
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private List<Pattern> convertExclusion(AnnotationValue exclAnnotationValue, Element element) {
        if (exclAnnotationValue==null) return null;
        return new SimpleAnnotationValueVisitor8<List<Pattern>, Void>() {
                    @Override
                    public List<Pattern> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
                        return vals.stream()
                                .map(x -> {
                                    String value = (String) x.getValue();
                                    Optional<Pattern> result = compileToPattern(value);
                                    if (! result.isPresent()) env.getMessager().printMessage(Diagnostic.Kind.ERROR, "param not valid. Expecting a regular Expression. Got:" + x, element);
                                    return result ;
                                })
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toList());
                    }

        }.visit(exclAnnotationValue);
    }


    private Map<String, TSTargetType> convertTypeMapping(AnnotationValue customMappingValue, Element element) {
        if (customMappingValue==null) return null;
        List<TSTargetType> targetTypes = new SimpleAnnotationValueVisitor8<List<TSTargetType>, Void>() {
            @Override
            public List<TSTargetType> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
                return vals.stream()
                        .map(x -> {
                            String value = (String) x.getValue();
                            Optional<TSTargetType> result = TSTargetFactory.createTSTargetByMapping(value);
                            if (! result.isPresent()) env.getMessager().printMessage(Diagnostic.Kind.ERROR, "param not valid. Expecting origin and target type separated by '->'. Got:" + x, element);
                            return result ;
                        })
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }
        }.visit(customMappingValue);
        
        return targetTypes.stream().filter(Objects::nonNull).collect(Collectors.toMap(TSTargetType::getJavaType, Function.identity()));
    }
}
