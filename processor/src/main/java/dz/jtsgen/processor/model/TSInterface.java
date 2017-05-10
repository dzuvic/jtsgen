package dz.jtsgen.processor.model;

import dz.jtsgen.processor.model.rendering.TSTypeVisitor;

import javax.lang.model.element.Element;

public class TSInterface extends TSType {
    public TSInterface(Element e, String namespace, String name) {
        super(e, namespace, name);
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
