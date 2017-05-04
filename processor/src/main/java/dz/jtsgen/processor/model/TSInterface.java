package dz.jtsgen.processor.model;

import dz.jtsgen.processor.model.rendering.TSTypeVisitor;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

public class TSInterface extends TSType {


    public TSInterface(TypeElement e) {
        super(e, e.getQualifiedName().toString());
    }

    @Override
    public String getKeyword() {
        return "interface";
    }

    @Override
    public void accept(TSTypeVisitor visitor, int ident) {
        visitor.visit(this, ident);
    }
}
