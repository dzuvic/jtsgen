package dz.jtsgen.processor.model.rendering;

public interface TSConstantElement {
    void accept(TSConstantVisitor visitor, int ident);
}
