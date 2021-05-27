package dz.jtsgen.processor.model.rendering;

import dz.jtsgen.processor.model.TSConstant;

public interface TSConstantVisitor {
    void visit(TSConstant x, int ident);
}
