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


import dz.jtsgen.processor.model.TSTargetType;
import dz.jtsgen.processor.model.TypeScriptModel;
import dz.jtsgen.processor.model.tstarget.TSTargets;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.*;
import javax.lang.model.util.AbstractTypeVisitor8;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.createTSTargetByMapping;

/**
*  This Visitor is used to convert a Java Type to an TS type
 * Created by zuvic on 04.04.17.
 */
public class MirrotTypeToTSConverterVisitor extends AbstractTypeVisitor8<TSTargetType, Void> {

    private static Logger LOG = Logger.getLogger(MirrotTypeToTSConverterVisitor.class.getName());


    private final Map<String,TSTargetType> declaredTypeConversions;

    private final Element currentElement;

    private final ProcessingEnvironment env;

    private final TypeScriptModel model;

    MirrotTypeToTSConverterVisitor(Element currentElement, TSAVisitorParam tsaVisitorParam) {
        this.declaredTypeConversions = new HashMap<>();
        this.declaredTypeConversions.putAll(tsaVisitorParam.getTsModel().getModuleInfo().getCustomMappings());
        this.declaredTypeConversions.putAll(createDefaultDeclaredTypeConversion());
        this.currentElement = currentElement;
        this.model = tsaVisitorParam.getTsModel();
        this.env = tsaVisitorParam.getpEnv();
    }

    private Map<String, TSTargetType> createDefaultDeclaredTypeConversion() {
        return Stream.of(
                TSTargets.STRING,
                TSTargets.INTEGER,
                TSTargets.DOUBLE,
                TSTargets.NUMBER,
                TSTargets.LONG,
                TSTargets.SHORT,
                TSTargets.COLLECTION
        ).collect(Collectors.toMap(TSTargetType::getJavaType, Function.identity()));

    }

    @Override
    public TSTargetType visitIntersection(IntersectionType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"intersection type not supported", currentElement);
        return null;
    }

    @Override
    public TSTargetType visitPrimitive(PrimitiveType t, Void x) {
        if (TypeKind.BOOLEAN  == t.getKind() ) return TSTargets.BOOLEAN;
        else if (TypeKind.CHAR  == t.getKind() ) return TSTargets.STRING;
        else return TSTargets.DOUBLE;
    }

    @Override
    public TSTargetType visitNull(NullType t, Void x) {
        return TSTargets.NULL;
    }

    @Override
    public TSTargetType visitArray(ArrayType t, Void x) {
        return null;
    }

    @Override
    public TSTargetType visitDeclared(DeclaredType t, Void x) {
        LOG.fine(() -> "TSCV: visitDeclared " + t);
        String nameOfType=t.toString();
        if (this.declaredTypeConversions.containsKey(nameOfType)) {
            LOG.finest(()-> "TC: declared Type in conversion List:" + nameOfType);
            return this.declaredTypeConversions.get(nameOfType);
        }
        Optional<TSTargetType> tsTargetType = extractType(nameOfType, t);
        if (tsTargetType.isPresent()) {
            return tsTargetType.get();
        }
        this.env.getMessager().printMessage(Diagnostic.Kind.WARNING,"declared type not known or found " + nameOfType, currentElement);
        return TSTargets.ANY;
    }

    private Optional<TSTargetType> extractType(String nameOfType, DeclaredType t) {
        List<? extends TypeMirror> supertypes = env.getTypeUtils().directSupertypes(t);
        return Optional.empty();
    }

    @Override
    public TSTargetType visitError(ErrorType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"error type not supported", currentElement);
        return null;
    }

    @Override
    public TSTargetType visitTypeVariable(TypeVariable t, Void x) {
        LOG.fine(() -> "TSCV: visitTypeVariable " + t);
        this.env.getMessager().printMessage(Diagnostic.Kind.WARNING,"arrays partially supported", currentElement);
        //        TypeVariable innerT = (ArrayType.class.cast(t)).getComponentType();
        String innerType = "any"; //this.visit(innerT,x)
        return TSTargets.ANY;
    }

    @Override
    public TSTargetType visitWildcard(WildcardType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"wildcard type not supported");
        return null;
    }

    @Override
    public TSTargetType visitExecutable(ExecutableType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"executable type not supported", currentElement);
        return null;
    }

    @Override
    public TSTargetType visitNoType(NoType t, Void x) {
        return TSTargets.VOID;
    }

    @Override
    public TSTargetType visitUnion(UnionType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"union type not supported", currentElement);
        return null;
    }
}
