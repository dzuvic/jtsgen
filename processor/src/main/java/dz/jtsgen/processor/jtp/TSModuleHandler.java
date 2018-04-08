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

import dz.jtsgen.annotations.NameMappingStrategy;
import dz.jtsgen.annotations.NameSpaceMappingStrategy;
import dz.jtsgen.annotations.OutputType;
import dz.jtsgen.annotations.TSModule;
import dz.jtsgen.processor.model.*;
import dz.jtsgen.processor.model.tstarget.TSTargetFactory;
import dz.jtsgen.processor.util.Either;
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
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.util.RegExHelper.compileToPattern;
import static dz.jtsgen.processor.util.StringUtils.splitIntoTwo;

/**
 * Handles TSModule Annotations
 */
public final class TSModuleHandler {

    private final Logger LOG = Logger.getLogger(TSModuleHandler.class.getName());

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
                    return bla.flatMap((y) -> {
                                TSModuleInfoBuilder.Builder tsBuilder = TSModuleInfoBuilder.builder();
                                tsBuilder.javaPackage(x.toString());
                                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : y.getElementValues().entrySet()) {

                                    final AnnotationValue value =  entry.getValue();
                                    if (isNameAndNotNull("version", entry)) tsBuilder.moduleVersion(asString(value));
                                    else if (isNameAndNotNull("moduleName",entry)) tsBuilder.moduleName(processModuleName(x, value)) ;
                                    else if (isNameAndNotNull("author",entry)) tsBuilder.moduleAuthor(asString(value));
                                    else if (isNameAndNotNull("description",entry)) tsBuilder.moduleDescription(asString(value));
                                    else if (isNameAndNotNull("authorUrl",entry)) tsBuilder.moduleAuthorUrl(asString(value));
                                    else if (isNameAndNotNull("license",entry)) tsBuilder.moduleLicense(asString(value));
                                    else if (isNameAndNotNull("generateTypeGuards",entry)) tsBuilder.generateTypeGuards(asBoolean(value));
                                    else if (isNameAndNotNull("customTypeMappings",entry)) tsBuilder.customMappings(convertTypeMapping(entry));
                                    else if (isNameAndNotNull("excludes",entry)) tsBuilder.excludes(convertExclusion(entry.getValue(), x));
                                    else if (isNameAndNotNull("nameSpaceMapping",entry)) tsBuilder.nameSpaceMappings(convertToNameSpaceMappings(entry.getValue(), x));
                                    else if (isNameAndNotNull("outputType",entry)) tsBuilder.outputType(convertOutputType(entry.getValue()));
                                    else if (isNameAndNotNull("nameSpaceMappingStrategy",entry)) tsBuilder.nameSpaceMappingStrategy(convertNameSpaceMappingStrategy(entry.getValue()));
                                    else if (isNameAndNotNull("additionalTypes",entry)) tsBuilder.addAllAdditionalTypes(convertToListOfString(entry.getValue()));
                                    else if (isNameAndNotNull("getterPrefixes",entry)) tsBuilder.getterPrefixes(convertToListOfString(entry.getValue()));
                                    else if (isNameAndNotNull("setterPrefixes",entry)) tsBuilder.setterPrefixes(convertToListOfString(entry.getValue()));
                                    else if (isNameAndNotNull("nameMappingStrategy",entry)) tsBuilder.nameMappingStrategy(convertNameMappingStrategy(entry.getValue()));
                                    else LOG.warning("unknown param on annotation TSModule " + entry.getKey());
                                }
                                return Optional.of( tsBuilder.build());
                            }
                    );
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private List<String> convertToListOfString(AnnotationValue value) {
        if (value.getValue() == null) return new ArrayList<>();
        return new SimpleAnnotationValueVisitor8<List<String>, Void>() {
            @Override
            public List<String> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
                return vals.stream()
                        .map(x -> (String) x.getValue())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }.visit(value);
    }

    private boolean asBoolean(AnnotationValue value) {
        return (value.getValue() instanceof Boolean) ? (Boolean) value.getValue() : false ;
    }


    String asString(AnnotationValue value) {
        return (value.getValue() instanceof String) ? (String) value.getValue() : "" ;
    }

    String  processModuleName(Element element, AnnotationValue annotationValue) {
        if (annotationValue.getValue() == null || annotationValue.getValue().toString().isEmpty() || !StringUtils.isPackageFriendly(annotationValue.getValue().toString())) {
            this.env.getMessager().printMessage(Diagnostic.Kind.ERROR, "The module name '" + annotationValue.getValue() + "' is not package name friendly", element);
            return "unknown";
        } else return annotationValue.getValue().toString();

    }

    NameSpaceMappingStrategy convertNameSpaceMappingStrategy(AnnotationValue value) {
        return (value != null
                && value.getValue() != null
                && Arrays.stream(NameSpaceMappingStrategy.values()).anyMatch(x -> x.name().equals(value.getValue().toString())))
                ? NameSpaceMappingStrategy.valueOf(value.getValue().toString())
                :null;
    }

    NameMappingStrategy convertNameMappingStrategy(AnnotationValue value) {
        return (value != null
                && value.getValue() != null
                && Arrays.stream(NameSpaceMappingStrategy.values()).anyMatch(x -> x.name().equals(value.getValue().toString())))
                ? NameMappingStrategy.valueOf(value.getValue().toString())
                :null;
    }
    
    private boolean isNameAndNotNull(String theName, Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry) {
        final String simpleName = entry.getKey().getSimpleName().toString();
        return theName.equals(simpleName) && entry.getValue() != null;
    }

    OutputType convertOutputType(AnnotationValue outputTypeValue) {
        return (outputTypeValue != null
                && outputTypeValue.getValue() != null
                && Arrays.stream(OutputType.values()).anyMatch(x -> x.name().equals(outputTypeValue.getValue().toString())))
                ? OutputType.valueOf(outputTypeValue.getValue().toString())
                :null;

    }

    private List<NameSpaceMapping> convertToNameSpaceMappings(AnnotationValue nameSpaceMappingAnnotationValue, Element element) {
        if (nameSpaceMappingAnnotationValue == null) return null;
        return new SimpleAnnotationValueVisitor8<List<NameSpaceMapping>, Void>() {
            @Override
            public List<NameSpaceMapping> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
                return vals.stream()
                        .map(x -> {
                            String value = (String) x.getValue();
                            Optional<NameSpaceMapping> result = splitIntoTwo(value).flatMap(y -> Optional.of(NameSpaceMappingBuilder.of(y.getFirst(), y.getSecond())));
                            if (!result.isPresent())
                                env.getMessager().printMessage(Diagnostic.Kind.ERROR, "param not valid. Expecting a name space mapping. Got:" + x, element);
                            return result;
                        })
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }

        }.visit(nameSpaceMappingAnnotationValue);
    }

    private List<Pattern> convertExclusion(AnnotationValue exclAnnotationValue, Element element) {
        if (exclAnnotationValue == null) return null;
        return new SimpleAnnotationValueVisitor8<List<Pattern>, Void>() {
            @Override
            public List<Pattern> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
                return vals.stream()
                        .map(x -> {
                            String value = (String) x.getValue();
                            Optional<Pattern> result = compileToPattern(value);
                            if (!result.isPresent())
                                env.getMessager().printMessage(Diagnostic.Kind.ERROR, "param not valid. Expecting a regular Expression. Got:" + x, element);
                            return result;
                        })
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }

        }.visit(exclAnnotationValue);
    }


    private Map<String, TSTargetType> convertTypeMapping(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry) {
        AnnotationValue customMappingValue = entry.getValue();
        Element element = entry.getKey();
        if (customMappingValue == null) return new HashMap<>();
        List<TSTargetType> targetTypes = new SimpleAnnotationValueVisitor8<List<TSTargetType>, Void>() {
            @Override
            public List<TSTargetType> visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
                return vals.stream()
                        .map(x -> {
                            String value = (String) x.getValue();
                            Either<String,TSTargetType> result = TSTargetFactory.createTSTargetByDSL(value);
                            if (result.isLeft())
                                env.getMessager().printMessage(Diagnostic.Kind.ERROR, "param not valid:" + x + "error: " + result.leftValue(), element);
                            return result.toOptional();
                        })
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }
        }.visit(customMappingValue);

        return targetTypes.stream().filter(Objects::nonNull).collect(Collectors.toMap(TSTargetType::getJavaType, Function.identity()));
    }
}
