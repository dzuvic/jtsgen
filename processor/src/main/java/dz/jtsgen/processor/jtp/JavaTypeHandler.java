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

import dz.jtsgen.processor.model.TSInterface;
import dz.jtsgen.processor.model.TSMember;
import dz.jtsgen.processor.model.TSType;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * creates interface models for each java interface or class.
 *
 * @author dzuvic initial
 */
public class JavaTypeHandler {

    private static Logger LOG = Logger.getLogger(JavaTypeHandler.class.getName());
    
    public List<TSType> createTsModels(TypeElement e, TSAVisitorParam aParam) {
        final List<TSType> result = new ArrayList<>();
        TSInterface tsi = new TSInterface(e).addMembers(findMembers(e, aParam));
        LOG.log(Level.FINEST, () -> String.format("JTH tsi created %s", tsi.toString()));
        result.add(tsi);
        return result;
    }

    private Collection<? extends TSMember> findMembers(TypeElement e, TSAVisitorParam aParam) {
        JavaTypeElementExtractingVisitor visitor = new JavaTypeElementExtractingVisitor(e, aParam);
        e.getEnclosedElements().stream()
                .forEach((x) -> visitor.visit(x, aParam));
        return visitor.getMembers();
    }
}
