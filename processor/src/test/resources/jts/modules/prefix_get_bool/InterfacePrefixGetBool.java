package jts.modules.prefix_get_bool;

import dz.jtsgen.annotations.TypeScript;

@TypeScript
public interface InterfacePrefixGetBool {

    // the following two has to convert
    boolean getGetBoolean();
    boolean hasHasBoolean();

    // this must not be converted
    boolean isIsBoolean();
}