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

package dz.jtsgen.processor.nsmap;

import dz.jtsgen.processor.model.*;
import dz.jtsgen.processor.model.tstarget.TSTargetFactory;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

final class DefaultNameSpaceModelMapper implements NameSpaceModelMapper {
    private static Logger LOG = Logger.getLogger(DefaultNameSpaceModelMapper.class.getName());

    private final TSModuleInfo moduleInfo;

    DefaultNameSpaceModelMapper(TSModuleInfo moduleInfo) {
        this.moduleInfo=moduleInfo;
    }

    @Override
    public TypeScriptModel mapNameSpacesOfModel(TypeScriptModel model) {

        List<NameSpaceMapping> mapping = new ArrayList<>(this.moduleInfo.getNameSpaceMappings());
        mapping.addAll(calculateMapping(model));
        return mapNameSpaces(mapping,model);
    }

    private TypeScriptModel mapNameSpaces(List<NameSpaceMapping> mapping, TypeScriptModel model) {
        List<TSType> mappedTSTypes = mapTsTypes(model.getTsTypes(), mapping);
        return model.withMappedData(mappedTSTypes);
    }

    private List<TSType> mapTsTypes(List<TSType> tsTypes, List<NameSpaceMapping> mapping) {
        SimpleNameSpaceMapper mapper = new SimpleNameSpaceMapper(mapping);
        return tsTypes.stream()
                .map( x -> {
                    String newNameSpace = mapper.mapNameSpace(x.getNamespace());
                    List<TSMember> mappedMembers = mapMembers(x.getMembers(), mapper);
                    LOG.finest( () -> "DNSM Mapping: " + x.getName() + " namespace: " +x.getNamespace() + " -> " + newNameSpace);
                    return x.changedNamespace(newNameSpace, mappedMembers);
                })
                .collect(Collectors.toList());
    }

    private List<TSMember> mapMembers(List<TSMember> members, NameSpaceMapper mapper) {
        return members.stream().map (x -> {
            TSTargetType newTSTarget = TSTargetFactory.changeNameSpace(x.getType(),mapper);
            LOG.finest(() -> "DNSM mapping member " + x.getName() + ": " + x.getType() + " -> " + newTSTarget);
            return x.changedTSTarget(newTSTarget);
        }).collect(Collectors.toList());
    }

    private List<NameSpaceMapping> calculateMapping(TypeScriptModel model) {
        List<? extends Element> elements = model.getTsTypes().stream()
                .map(TSType::getElement)
                .collect(Collectors.toList());
        return nsMappingStarategy(model).computeNameSpaceMapping(elements);

    }

    private NameSpaceMapperCalculator nsMappingStarategy(TypeScriptModel model) {
        switch (model.getModuleInfo().getNameSpaceMappingStrategy()) {
            case ALL_TO_ROOT:
                return new AllRootNameSpaceMapperCalculator();
            case TOP_LEVEL_TO_ROOT:
                return new TopLevelNameSpaceMapperCalculator();
            default: throw new IllegalStateException("enum not implemented: " + model.getModuleInfo().getNameSpaceMappingStrategy() );
        }
    }
}
