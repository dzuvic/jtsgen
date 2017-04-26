package dz.jtsgen.processor.model;


import java.util.*;
import java.util.stream.Collectors;

public class TypeScriptModel {

    // java package -> TSModuleInfo
    private TSModuleInfo moduleInfo = new TSModuleInfo("unknown", null);

    // all converted TS Types
    private final List<TSType> tsTypes=new ArrayList<>();

    private TypeScriptModel() {

    }

    /**
     * @return create an empty TypeScriptModel with one default module
     */
    public static TypeScriptModel newModelWithDefaultModule() {
        return new TypeScriptModel();
    }


    /**
     * copy constructor for renderer
     */
    protected TypeScriptModel(TypeScriptModel ts) {
        this.tsTypes.addAll(ts.getTsTypes());
        this.moduleInfo= ts.moduleInfo;
    }

    public void addModuleInfo(TSModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public void addTSTypes(final List<TSType> visitorResult) {
        if (visitorResult == null) return;

        Collection<TSType> removeThem = tsTypes.stream()
                .filter(
                        (x) -> visitorResult.stream().anyMatch(
                                (y) -> x.getName().equals(y.getName()) && x.getNamespace().equals(y.getNamespace())
                       )
                )
                .collect(Collectors.toList());

        tsTypes.removeAll(removeThem);
        tsTypes.addAll(visitorResult);
    }


    public List<TSType> getTsTypes() {
        return tsTypes;
    }

    public TSModuleInfo getModuleInfo() {
        return this.moduleInfo;
    }
    
}
