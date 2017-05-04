/*
 * Copyright 2017 Dragan Zuvic
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

import dz.jtsgen.processor.model.*;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;

/**
 * creates interface models for each java interface or class.
 *
 * @author dzuvic initial
 */
public class JavaTypeHandler {

    private static Logger LOG = Logger.getLogger(JavaTypeHandler.class.getName());

    public List<TSType> createTsModels(TypeElement e, TSAVisitorParam aParam) {
        final List<TSType> result = new ArrayList<>();
        Optional<TSType> tsi = handleJavaType(e, aParam);
        LOG.log(Level.FINEST, () -> String.format("JTH tsi created %s", tsi.toString()));
        tsi.ifPresent(result::add);
        return result;
    }

    Optional<TSType> createTsModelWithEmbeddedTypes(TypeElement e, TSAVisitorParam aParam) {
        Optional<TSType> tsi = handleJavaType(e, aParam);
        LOG.log(Level.FINEST, () -> String.format("JTH single tsi created %s", tsi.toString()));
        return tsi;
    }

    private Optional<TSType> handleJavaType(TypeElement element, TSAVisitorParam tsaParam) {
        TSType result = null;
        switch (element.getKind()) {
            case CLASS: {
                result = new TSInterface(element).addMembers(findMembers(element, tsaParam));
                break;
            }
            case INTERFACE: {
                result = new TSInterface(element).addMembers(findMembers(element, tsaParam));
                break;
            }
            case ENUM: {
                result = new TSEnum(element).addMembers(findEnumMembers(element, tsaParam));
                break;
            }
            default: break;
        }
       return Optional.ofNullable(result);
    }

    private Collection<? extends TSMember> findEnumMembers(TypeElement element, TSAVisitorParam tsaParam) {
        return element.getEnclosedElements().stream()
                .filter(x->x.getKind()==ENUM_CONSTANT)
                .map ( x -> new TSEnumMember(x.getSimpleName().toString())
                ).collect(Collectors.toList());
    }

    private Collection<? extends TSMember> findMembers(TypeElement e, TSAVisitorParam aParam) {
        JavaTypeElementExtractingVisitor visitor = new JavaTypeElementExtractingVisitor(e, aParam);
        e.getEnclosedElements().stream()
                .filter(x -> x.getKind()== ElementKind.FIELD || x.getKind()==ElementKind.METHOD)
                .forEach((x) -> visitor.visit(x, aParam));
        return visitor.getMembers();
    }
}
