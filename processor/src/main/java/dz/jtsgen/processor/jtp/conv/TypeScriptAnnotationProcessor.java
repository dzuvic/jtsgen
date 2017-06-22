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
import dz.jtsgen.processor.jtp.conv.visitors.TSAVisitor;
import dz.jtsgen.processor.model.*;
import dz.jtsgen.processor.nsmap.SimpleNameSpaceMapper;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static dz.jtsgen.processor.jtp.helper.RoundEnvHelper.filteredTypeSriptElements;
import static dz.jtsgen.processor.util.StringUtils.lastOf;
import static dz.jtsgen.processor.util.StringUtils.untill;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;

/**
 * creates interface models for each java interface or class.
 *
 * @author dzuvic initial
 * @author dzuvic renamed to TypeScriptAnnotationProcessor
 */
public class TypeScriptAnnotationProcessor implements JavaTypeProcessor, JavaTypeConverter {

    private static Logger LOG = Logger.getLogger(TypeScriptAnnotationProcessor.class.getName());

    private final TSProcessingInfo TSProcessingInfo;

    private final SimpleNameSpaceMapper namespaceMapper;

    public TypeScriptAnnotationProcessor(TSProcessingInfo TSProcessingInfo) {
        this.TSProcessingInfo = TSProcessingInfo;
        this.namespaceMapper = new SimpleNameSpaceMapper(TSProcessingInfo.getTsModel());
    }

    @Override
    public void processAnnotations(RoundEnvironment roundEnv) {
        TSAVisitor tsaVisitor = new TSAVisitor();
        for (Element e : filteredTypeSriptElements(roundEnv)) {
            tsaVisitor.visit(e,this).ifPresent(     x -> {
                TSProcessingInfo.getTsModel().addTSTypes(singletonList(x));
                LOG.log(Level.FINEST, () -> String.format("TSAP added %s to model", x.toString()));
                    }
            );
        }
    }

    @Override
    public Optional<TSType> convertJavaType(TypeElement e) {
        LOG.log(Level.FINEST, () -> String.format("TSAP converting java type %s", e.toString()));
        return handleJavaType(e);
    }

    private Optional<TSType> handleJavaType(TypeElement element) {
        if (element == null) return Optional.empty();

        if (checkExclusion(element)) {
            LOG.info( () -> "Excluding " + element);
            return Optional.empty();
        }
        TSType result = null;
        final String name = lastOf(element.toString());
        //TODO move this to renderer and keep the original name space
        final String namespace = this.namespaceMapper.mapNameSpace(untill(element.toString()));
        switch (element.getKind()) {
            case CLASS:
            {
                result = new TSInterface(element, namespace, name).addMembers(findMembers(element));
                break;
            }
            case INTERFACE: {
                result = new TSInterface(element, namespace, name).addMembers(findMembers(element));
                break;
            }
            case ENUM: {
                result = new TSEnum(element, namespace, name).addMembers(findEnumMembers(element));
                break;
            }
            default: break;
        }
       return Optional.ofNullable(result);
    }

    private boolean checkExclusion(TypeElement element) {
        final String typeName=element.toString();
        return this.TSProcessingInfo.getTsModel().getModuleInfo().getExcludes().stream().anyMatch(
                x -> x.matcher(typeName).find()
        );
    }

    private Collection<? extends TSMember> findEnumMembers(TypeElement element) {
        return element.getEnclosedElements().stream()
                .filter(x->x.getKind()==ENUM_CONSTANT)
                .map ( x -> new TSEnumMember(x.getSimpleName().toString())
                ).collect(Collectors.toList());
    }

    private Collection<? extends TSMember> findMembers(TypeElement e) {
        JavaTypeElementExtractingVisitor visitor = new JavaTypeElementExtractingVisitor(e, TSProcessingInfo);
        e.getEnclosedElements().stream()
                .filter(x -> x.getKind()== ElementKind.FIELD || x.getKind()==ElementKind.METHOD)
                .forEach(visitor::visit);
        return visitor.getMembers();
    }
}
