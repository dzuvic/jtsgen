package dz.jtsgen.processor.model;


import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TypeScriptModel {

    // java package -> TSModuleInfo
    private TSModuleInfo moduleInfo = new TSModuleInfo("unknown", null).withTypeMappingInfo(null, defaultExclusion());

    private static List<Pattern> defaultExclusion() {
        return Arrays.stream(new String[]{"^sun", "^jdk.internal"}).map(Pattern::compile).collect(Collectors.toList());
    }

    // all converted TS Types
    private final List<TSType> tsTypes=new ArrayList<>();

    // a list of java types to ts mapping, that are added indirectly
    private Map<String, TSTargetType> tsTargetTypes = new HashMap<>();

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

    public Optional<TSTargetType> checkTSTargetType(String nameOfType) {
        return Optional.ofNullable(this.tsTargetTypes.get(nameOfType));
    }

    public void addTSTarget(TSTargetType tsTargetByMapping) {
        this.tsTargetTypes.put(tsTargetByMapping.getJavaType(),tsTargetByMapping);
    }
}
