/*
 * Copyright (c) 2018 Dragan Zuvic
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

import dz.jtsgen.processor.helper.DeclTypeHelper;
import dz.jtsgen.processor.jtp.conv.visitors.JavaTypeConverter;
import dz.jtsgen.processor.model.*;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;

/**
 * this represents the default java type converter. It converts a TypeElement to single TypeScript Type
 */
public class DefaultJavaTypeConverter implements JavaTypeConverter {

    private final TypeElement javaLangObjectElement;
    private final TypeElement javaLangEnumElement;
    private final TSProcessingInfo processingInfo;


    private static Logger LOG = Logger.getLogger(TypeScriptAnnotationProcessor.class.getName());

    DefaultJavaTypeConverter(TSProcessingInfo processingInfo) {
        this.processingInfo = processingInfo;
        this.javaLangObjectElement = this.processingInfo.getpEnv().getElementUtils().getTypeElement("java.lang.Object");
        this.javaLangEnumElement = this.processingInfo.getpEnv().getElementUtils().getTypeElement("java.lang.Enum");
    }


    @Override
    public Optional<TSType> convertJavaType(TypeElement e) {
        LOG.log(Level.FINEST, () -> String.format("DJTC converting java type %s", e.toString()));
        return handleJavaType(e);
    }

//    private TSTargetType convertTypeMirrorToTsType(TypeElement theElement, TSProcessingInfo tsProcessingInfo) {
//        return new MirrotTypeToTSConverterVisitor(theElement, tsProcessingInfo, this).visit(theElement.asType());
//    }

    private Optional<TSType> handleJavaType(TypeElement element) {
        if (element == null) return Optional.empty();

        if (checkExclusion(element)) {
            LOG.info( () -> "DJTC Excluding " + element);
            return Optional.empty();
        }


//        System.out.println(convertTypeMirrorToTsType(element,processingInfo));
//        this.processingInfo.getTsModel().getModuleInfo().getCustomMappings();

        List<TSType> supertypes = convertSuperTypes(element);
        TSType result = null;

        List<TSTypeVariable> typeParams = element.getTypeParameters().stream()
                .map ( x -> {
                    List<TSType> convertedBounds = convertBounds(x);
                    return TSTypeVariableBuilder.builder()
                            .name(x.getSimpleName().toString())
                            .addAllBounds(convertedBounds)
                            .build();
                })
                .collect(Collectors.toList());
        LOG.fine(() -> "DJTC Element has type params: " + typeParams);

        switch (element.getKind()) {
            case CLASS:
            {
                result = TSInterfaceBuilder.of(element).withMembers(findMembers(element)).withSuperTypes(supertypes).withTypeParams(typeParams);
                break;
            }
            case INTERFACE: {
                result = TSInterfaceBuilder.of(element).withMembers(findMembers(element)).withSuperTypes(supertypes).withTypeParams(typeParams);
                break;
            }
            case ENUM: {
                result = TSEnumBuilder.of(element).withMembers(findEnumMembers(element)).withTypeParams(new ArrayList<>());
                break;
            }
            default: break;
        }
        return Optional.ofNullable(result);
    }

    private List<TSType> convertSuperTypes(TypeElement element) {
        final List<? extends TypeMirror> superTypes = this.processingInfo.getpEnv().getTypeUtils().directSupertypes(element.asType());
        LOG.finest(() -> "DJTC direct supertypes of " + element + " are " + superTypes);
        List<TypeElement> filteredSuperTypes = superTypes
                .stream().map(DeclTypeHelper::declaredTypeToTypeElement)
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());

        List<TSType> result = filteredSuperTypes.stream()
                .filter( x -> !isMarkerInterface(x))
                .filter( x -> !isTopType(x))
                .filter( x -> ! checkExclusion(x))
                .map(x -> {
                    LOG.info("DJTC converting supertype " +x);
                    return handleJavaType(x);
                })
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());

        this.processingInfo.getTsModel().addTSTypes(result);

        return result;
    }


    private List<TSType> convertBounds(TypeParameterElement element) {
        if (element.getBounds().isEmpty()) return new ArrayList<>();

        List<TSType>  result = element.getBounds().stream()
                .map(this.processingInfo.getpEnv().getTypeUtils()::asElement)
                .filter(x -> x instanceof TypeElement)
                .map(x -> (TypeElement) x)
                .filter( x -> !isMarkerInterface(x))
                .filter( x -> !isTopType(x))
                .filter( x -> ! checkExclusion(x))
                .map(x -> {
                    LOG.info("DJTC converting Bound " +x);
                    return handleJavaType(x);
                })
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());

        this.processingInfo.getTsModel().addTSTypes(result);

        return result;
    }


    private boolean isTopType(TypeElement typeElement) {
        boolean r =
                typeElement.getQualifiedName().equals(this.javaLangObjectElement.getQualifiedName())
                        || typeElement.getQualifiedName().contentEquals(this.javaLangEnumElement.getQualifiedName().toString());
        if (r) LOG.fine(() -> "DJTC " + typeElement + " is top type");
        return r;
    }

    private boolean isMarkerInterface(TypeElement typeElement) {
        boolean isMarker = typeElement.getKind() == ElementKind.INTERFACE
                && typeElement.getEnclosedElements().size() == 0;
        if (isMarker) LOG.fine(() -> "DJTC " + typeElement + " is marker interface");
        return isMarker;
    }

    private boolean checkExclusion(TypeElement element) {
        final String typeName=element.toString();
        boolean r= this.processingInfo.getTsModel().getModuleInfo().getExcludes().stream().anyMatch(
                x -> x.matcher(typeName).find()
        );
        if (r) LOG.fine(() -> "DJTC exclusion " + element);
        return r;
    }

    private Collection<? extends TSMember> findEnumMembers(TypeElement element) {
        return element.getEnclosedElements().stream()
                .filter(x->x.getKind()==ENUM_CONSTANT)
                .map ( x -> TSEnumMemberBuilder.of(x.getSimpleName().toString())
                ).collect(Collectors.toList());
    }

    private Collection<? extends TSMember> findMembers(TypeElement e) {
        LOG.fine(() -> "DJTC find members in  in java type " + e);
        JavaTypeElementExtractingVisitor visitor = new JavaTypeElementExtractingVisitor(e, processingInfo, this);
        e.getEnclosedElements().stream()
                .filter(x -> x.getKind()== ElementKind.FIELD || x.getKind()==ElementKind.METHOD)
                .forEach(visitor::visit);
        return visitor.getMembers();
    }
}
