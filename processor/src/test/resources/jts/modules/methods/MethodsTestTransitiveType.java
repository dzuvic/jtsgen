package jts.modules.methods;

import dz.jtsgen.annotations.TSMethod;
import dz.jtsgen.annotations.TypeScript;

@TypeScript
public interface MethodsTestTransitiveType {
    @TSMethod
    ResultItem calcItem(DataItem param);
}
