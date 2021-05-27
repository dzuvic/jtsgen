package jts.modules.rename;

import dz.jtsgen.annotations.TSProperty;
import dz.jtsgen.annotations.TypeScript;

@TypeScript
public interface PropertyRenamed {

    @TSProperty(name = "getTextMessage")
    String getText();
}
