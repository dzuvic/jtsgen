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

package dz.jtsgen.processor.visitors;

import dz.jtsgen.processor.jtp.JavaTypeHandler;
import dz.jtsgen.processor.model.TSType;
import dz.jtsgen.processor.model.TypeScriptModel;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.SimpleElementVisitor8;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TSAVisitor extends SimpleElementVisitor8<List<TSType>, TSAVisitorParam> {

    private static Logger LOG = Logger.getLogger(TSAVisitor.class.getName());

    private ProcessingEnvironment env;
    private JavaTypeHandler javaTypeHandler;

    public TSAVisitor(TypeScriptModel typeScriptModel, ProcessingEnvironment processingEnv) {
        super(Collections.emptyList());
        javaTypeHandler = new JavaTypeHandler();
        this.env = processingEnv;
    }

    @Override
    public List<TSType> visitType(TypeElement e, TSAVisitorParam param) {
        LOG.log(Level.FINER,() -> String.format("visitType %s", e.toString() ));
        return javaTypeHandler.createTsModels(e, param);
    }

    @Override
    public List<TSType> visitVariable(VariableElement e, TSAVisitorParam aVoid) {
        LOG.log(Level.FINER,() -> String.format("visitVariable %s", e.toString() ));
        env.getMessager().printMessage(Diagnostic.Kind.ERROR,"TypeScript annotation not supported for variable.Use TSGenOption instead.", e);
        return super.visitVariable(e, aVoid);
    }

    @Override
    public List<TSType> visitUnknown(Element e, TSAVisitorParam aVoid) {
        LOG.log(Level.FINEST, () -> String.format("unkown Type: %s", e));
        return super.visitUnknown(e, aVoid);
    }

    @Override
    public List<TSType> visitPackage(PackageElement e, TSAVisitorParam aVoid) {
        env.getMessager().printMessage(Diagnostic.Kind.ERROR,"currently not supported for package", e);
        return super.visitPackage(e, aVoid);
    }
}
