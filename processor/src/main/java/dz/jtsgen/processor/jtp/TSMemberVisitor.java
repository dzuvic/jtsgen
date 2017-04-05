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


import dz.jtsgen.processor.model.TSMember;
import dz.jtsgen.processor.visitors.TSAVisitorParam;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.util.TypeKindVisitor8;

class TSMemberVisitor extends TypeKindVisitor8<TSMember,TSAVisitorParam> {


    @Override
    public TSMember visitPrimitiveAsBoolean(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsBoolean(t, aVoid);
    }

    @Override
    public TSMember visitPrimitiveAsByte(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsByte(t, aVoid);
    }

    @Override
    public TSMember visitPrimitiveAsShort(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsShort(t, aVoid);
    }

    @Override
    public TSMember visitPrimitiveAsInt(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsInt(t, aVoid);
    }

    @Override
    public TSMember visitPrimitiveAsLong(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsLong(t, aVoid);
    }

    @Override
    public TSMember visitPrimitiveAsChar(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsChar(t, aVoid);
    }

    @Override
    public TSMember visitPrimitiveAsFloat(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsFloat(t, aVoid);
    }

    @Override
    public TSMember visitPrimitiveAsDouble(PrimitiveType t, TSAVisitorParam aVoid) {
        return super.visitPrimitiveAsDouble(t, aVoid);
    }


    @Override
    public TSMember visitArray(ArrayType t, TSAVisitorParam aVoid) {
        return super.visitArray(t, aVoid);
    }

    @Override
    public TSMember visitDeclared(DeclaredType t, TSAVisitorParam aVoid) {
        return super.visitDeclared(t, aVoid);
    }
}
