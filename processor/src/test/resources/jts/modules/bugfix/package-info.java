@TSModule(
        moduleName = "MethodRealScenarioTest",
        outputType = OutputType.NO_MODULE,
        author = "Me",
        authorUrl = "Https://some.url",
        printDate = true,
        excludes = {
                "^sun",
                "^jdk.internal",
                "^java.lang.",
                "^com.google.gwt"
        },
        enumExportStrategy = EnumExportStrategy.STRING,
        customTypeMappings = {
                "com.google.gwt.dom.client.Element -> Element",
        },
        appendOriginalNamesToJavaDoc = true
)
package jts.modules.bugfix;

import dz.jtsgen.annotations.EnumExportStrategy;
import dz.jtsgen.annotations.OutputType;
import dz.jtsgen.annotations.TSModule;