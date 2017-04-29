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
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.*;
import javax.lang.model.util.AbstractTypeVisitor8;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dz.jtsgen.processor.model.tstarget.TSTargetFactory.createTSTargetByMapping;

/**
*  This Visitor is used to convert a Java Type to an TS type
 * Created by zuvic on 04.04.17.
 */
public class MirrotTypeToTSConverterVisitor extends AbstractTypeVisitor8<String, Void> {

    private static Logger LOG = Logger.getLogger(MirrotTypeToTSConverterVisitor.class.getName());


    private final Map<String,String> declaredTypeConversions;

    private final Element currentElement;

    private final ProcessingEnvironment env;

    private final TypeScriptModel model;

    MirrotTypeToTSConverterVisitor(Element currentElement, TSAVisitorParam tsaVisitorParam) {
        this.declaredTypeConversions = createDefaultDeclaredTypeConversion();
        this.declaredTypeConversions.putAll(tsaVisitorParam.getTsModel().getModuleInfo().getCustomMappings());
        this.currentElement = currentElement;
        this.model = tsaVisitorParam.getTsModel();
        this.env = tsaVisitorParam.getpEnv();
    }

    private Map<String, String> createDefaultDeclaredTypeConversion() {
        return Stream.of(
                createTSTargetByMapping("java.lang.String -> string"),
                createTSTargetByMapping("java.lang.Integer -> number"),
                createTSTargetByMapping("java.lang.Double -> number"),
                createTSTargetByMapping("java.lang.Number -> number"),
                createTSTargetByMapping("java.lang.Long -> number"),
                createTSTargetByMapping("java.lang.Short -> number")
        ).filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toMap(TSTargetType::getJavaType, TSTargetType::toString));

    }

    @Override
    public String visitIntersection(IntersectionType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"intersection type not supported", currentElement);
        return null;
    }

    @Override
    public String visitPrimitive(PrimitiveType t, Void x) {
        if (TypeKind.BOOLEAN  == t.getKind() ) return "boolean";
        else if (TypeKind.CHAR  == t.getKind() ) return "string";
        else return "number";
    }

    @Override
    public String visitNull(NullType t, Void x) {
        return "null";
    }

    @Override
    public String visitArray(ArrayType t, Void x) {
        return null;
    }

    @Override
    public String visitDeclared(DeclaredType t, Void x) {
        String nameOfType=t.toString();
        if (this.declaredTypeConversions.containsKey(nameOfType)) {
            LOG.finest(()-> "TC: declared Type in conversion List:" + nameOfType);
            return this.declaredTypeConversions.get(nameOfType);
        }
        Optional<TSTargetType> tsTargetType = extractType(nameOfType, t);
        if (tsTargetType.isPresent()) {
            return tsTargetType.get().toString();
        }
        this.env.getMessager().printMessage(Diagnostic.Kind.WARNING,"declared type not known or found " + nameOfType, currentElement);
        return "any";
    }

    private Optional<TSTargetType> extractType(String nameOfType, DeclaredType t) {
        return Optional.empty();
    }

    @Override
    public String visitError(ErrorType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"error type not supported", currentElement);
        return null;
    }

    @Override
    public String visitTypeVariable(TypeVariable t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.WARNING,"arrays partially supported", currentElement);
        //        TypeVariable innerT = (ArrayType.class.cast(t)).getComponentType();
        String innerType = "any"; //this.visit(innerT,x)
        return "Array<"+ innerType+">";
    }

    @Override
    public String visitWildcard(WildcardType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"wildcard type not supported");
        return null;
    }

    @Override
    public String visitExecutable(ExecutableType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"executable type not supported", currentElement);
        return null;
    }

    @Override
    public String visitNoType(NoType t, Void x) {
        return "Void";
    }

    @Override
    public String visitUnion(UnionType t, Void x) {
        this.env.getMessager().printMessage(Diagnostic.Kind.ERROR,"union type not supported", currentElement);
        return null;
    }
}
