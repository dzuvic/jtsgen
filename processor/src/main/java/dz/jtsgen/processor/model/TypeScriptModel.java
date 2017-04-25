package dz.jtsgen.processor.model;


import java.util.*;
import java.util.stream.Collectors;

public class TypeScriptModel {

    // java package -> TSModuleInfo
    private final Map<String,TSModuleInfo> moduleInfos = new HashMap<>();

    /**
     * @return create an empty TypeScriptModel with one default module
     */
    public static TypeScriptModel newModelWithDefaultModule() {
        TypeScriptModel result = new TypeScriptModel();
        result.addModuleInfo(new TSModuleInfo("unknown", null));
        return result;
    }

    // all converted TS Types
    private final List<TSType> tsTypes=new ArrayList<>();

    private TypeScriptModel() {

    }

    /**
     * copy constructor for renderer
     */
    protected TypeScriptModel(TypeScriptModel ts) {
        this.tsTypes.addAll(ts.getTsTypes());
        this.moduleInfos.putAll(ts.getModuleInfos());
    }

    public void addModuleInfo(TSModuleInfo moduleInfo) {
        this.moduleInfos.put(moduleInfo.getJavaPackage().orElse(""),moduleInfo);
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

    protected Map<String, TSModuleInfo> getModuleInfos() {
        return moduleInfos;
    }

    public void addModuleInfos(Set<TSModuleInfo> tsModuleInfos) {
        assert tsModuleInfos != null;
        // if any modules are defined, then remove the default module
        if (this.getModuleInfos().get("") != null && this.getModuleInfos().get("").isDefault()) this.getModuleInfos().remove("");
        tsModuleInfos.forEach((x) -> this.getModuleInfos().put(x.getJavaPackage().orElseThrow(IllegalArgumentException::new),x));
    }
}
