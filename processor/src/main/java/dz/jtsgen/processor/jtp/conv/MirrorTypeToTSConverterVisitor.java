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


import dz.jtsgen.processor.jtp.conv.visitors.JavaTypeConverter;
import dz.jtsgen.processor.jtp.info.TSProcessingInfo;
import dz.jtsgen.processor.model.TSTargetType;
import dz.jtsgen.processor.model.TSType;
import dz.jtsgen.processor.model.TSTypeVariable;
import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.model.tstarget.TSTargetFactory;
import dz.jtsgen.processor.model.tstarget.TSTargets;
import dz.jtsgen.processor.util.StreamUtils;
import dz.jtsgen.processor.util.Tuple;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.AbstractTypeVisitor8;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.helper.TypeMirrorHelper.extractTypeVariablename;
import static dz.jtsgen.processor.jtp.conv.ConversionByDsl.directConversionType;
import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.*;
import static dz.jtsgen.processor.model.tstarget.TSTargets.*;
import static dz.jtsgen.processor.util.StreamUtils.firstOptional;
import static dz.jtsgen.processor.util.StringUtils.withoutTypeArgs;
import static java.lang.String.format;
import static javax.tools.Diagnostic.Kind.WARNING;

/**
 * This Visitor is used to convert a Java Type to an TS type
 * Created by zuvic on 04.04.17.
 */
class MirrorTypeToTSConverterVisitor extends AbstractTypeVisitor8<TSTargetType, Void> {

    private static Logger LOG = Logger.getLogger(MirrorTypeToTSConverterVisitor.class.getName());

    private final Element currentElement;

    private final TSProcessingInfo tsProcessingInfo;

    private final JavaTypeConverter javaTypeConverter;

    MirrorTypeToTSConverterVisitor(Element currentElement, TSProcessingInfo tsProcessingInfo, JavaTypeConverter javaTypeConverternverter) {
        this.tsProcessingInfo = tsProcessingInfo;
        this.currentElement = currentElement;
        this.javaTypeConverter = javaTypeConverternverter;
    }


    @Override
    public TSTargetType visitIntersection(IntersectionType t, Void x) {
        this.env().getMessager().printMessage(Diagnostic.Kind.ERROR, "intersection type not supported", currentElement);
        return ANY;
    }

    @Override
    public TSTargetType visitPrimitive(PrimitiveType t, Void x) {
        if (TypeKind.BOOLEAN == t.getKind()) return TSTargets.BOOLEAN;
        else if (TypeKind.CHAR == t.getKind()) return STRING;
        else return NUMBER;
    }

    @Override
    public TSTargetType visitNull(NullType t, Void x) {
        return TSTargets.NULL;
    }

    @Override
    public TSTargetType visitArray(ArrayType t, Void x) {
        TSTargetType embeddedArrayType = this.visit(t.getComponentType());
        return createTSTargetFromArray(embeddedArrayType);
    }

    @Override
    public TSTargetType visitDeclared(DeclaredType t, Void x) {
        LOG.fine(() -> "TSCV: visitDeclared " + t);
        String nameOfTypeWithTypeParams = t.toString();
        Optional<TSTargetType> tsTargetType = extractType(t);
        if (tsTargetType.isPresent()) {
            return tsTargetType.get();
        }
        this.env().getMessager().printMessage(WARNING, "declared type not known or found " + nameOfTypeWithTypeParams, currentElement);
        return TSTargets.ANY;
    }

    private Optional<TSTargetType> extractType(DeclaredType t) {
        final String nameOfType = withoutCustomAnnotations(withoutTypeArgs(t.toString()));
        return firstOptional(
                ()-> directConversionType(t, nameOfType, tsProcessingInfo.declaredTypeConversions(), env()),
                ()-> typeInModel(t, nameOfType),
                ()-> typeNotInModel(t,nameOfType)
        ).map(x -> {
                    if (x.typeParameters().size() > 0) return typeParametrized(t, x);
                    else return x;
                });
    }

    private String withoutCustomAnnotations(String withoutTypeArgs) {
        if(withoutTypeArgs.startsWith("@")) {
            return withoutTypeArgs.substring(withoutTypeArgs.lastIndexOf(' ')+1);
        }
        return withoutTypeArgs;
    }

    // check if type is in declared type
    private TSTargetType typeParametrized(DeclaredType declType, TSTargetType tstype) {
        if (declType.getTypeArguments() != null && declType.getTypeArguments().size() > 0) {
            LOG.finest(() -> "TCSV decl type type params:" + declType.getTypeArguments());
            if (tstype.typeParameters().size() != declType.getTypeArguments().size()) this.env().getMessager().printMessage(WARNING, "type params of origin and target type differ", currentElement);
            Map<String,TSTargetType> typeParamMap = StreamUtils
                    .zip(declType.getTypeArguments().stream(), tstype.typeParameters().stream())
                    .map(it -> {
                                    TSTargetType tsTargetType =  Optional.ofNullable(this.visit(it.getFirst())).orElse(TSTargets.ANY);
                                    return new Tuple<>(it.getSecond(), tsTargetType);
                    })
                    .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));
            return tstype.withTypeParams(typeParamMap);
        }
        return tstype;
    }
    // if type is not in declared types, then convert the type recursivly and put them into the model
    private Optional<TSTargetType> typeInModel(DeclaredType t, String nameOfType) {
        return model().checkTSTargetType(nameOfType);
    }

    private Optional<TSTargetType> typeNotInModel(DeclaredType t, String nameOfType) {
        Element javaElement = env().getTypeUtils().asElement(t);
        LOG.finest(() -> "TCSV: not in Model " + javaElement);
        //add a dummy type, to stop endless recursive calls
        createTSTargetByDSL("" + nameOfType + "->" + "any").ifPresent(x -> model().addTSTarget(x));
        final Optional<TypeElement> typeElement = Optional.ofNullable( (javaElement instanceof TypeElement) ? (TypeElement) javaElement :null);
        final Optional<TSType> tsType = typeElement.flatMap(javaTypeConverter::convertJavaType);
        final Optional<TSTargetType> result = tsType.flatMap(x -> {
            model().addTSTypes(Collections.singletonList(x));
            // added type variables on both sides
            final String typeVars = x.getTypeParams().size() == 0 ? "" :
                    "<" + x.getTypeParams().stream().map(TSTypeVariable::getName).collect(Collectors.joining(",")) + ">";
            return createTSTargetByDSLWithNS(
                    "" + nameOfType + typeVars
                            + "->"
                            + x.getName() + typeVars
                    ).toOptional();
        });
        LOG.finest(() -> "TCSV: converted " + t + " to " + tsType );
        // afterwards change the created mapping info accordingly
        if (result.isPresent()) {
            LOG.finest((""));
            model().addTSTarget(result.get());
        }
        return result;
    }

    @Override
    public TSTargetType visitError(ErrorType t, Void x) {
        this.env().getMessager().printMessage(Diagnostic.Kind.WARNING, "error type not supported, mapped to any", currentElement);
        return TSTargets.ANY;
    }

    @Override
    public TSTargetType visitTypeVariable(TypeVariable t, Void x) {
        LOG.fine(() -> "TSCV: visitTypeVariable " + t);
        this.env().getMessager().printMessage(WARNING, "arrays partially supported", currentElement);
        final String varName = extractTypeVariablename(t);
        return TSTargetFactory.createTSTargetByDSL(format("%s->%s", varName, varName)).orElse(TSTargets.ANY);
    }


    @Override
    public TSTargetType visitWildcard(WildcardType t, Void x) {
        this.env().getMessager().printMessage(Diagnostic.Kind.WARNING, "wildcard type not supported");
        return TSTargets.ANY;
    }

    @Override
    public TSTargetType visitExecutable(ExecutableType t, Void x) {
        this.env().getMessager().printMessage(Diagnostic.Kind.WARNING, "executable type not supported", currentElement);
        return TSTargets.ANY;
    }

    @Override
    public TSTargetType visitNoType(NoType t, Void x) {
        return TSTargets.VOID;
    }

    @Override
    public TSTargetType visitUnion(UnionType t, Void x) {
        this.env().getMessager().printMessage(Diagnostic.Kind.WARNING, "union type not supported", currentElement);
        return TSTargets.ANY;
    }

    private ProcessingEnvironment env() {
        return this.tsProcessingInfo.getpEnv();
    }

    private TypeScriptModel model() {
        return this.tsProcessingInfo.getTsModel();
    }
}
