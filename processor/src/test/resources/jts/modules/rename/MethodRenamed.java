package jts.modules.rename;

import dz.jtsgen.annotations.TSMethod;
import dz.jtsgen.annotations.TypeScript;

@TypeScript
public interface MethodRenamed {

    @TSMethod(name = "bar")
    void foo();
}
