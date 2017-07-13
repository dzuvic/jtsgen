package dz.jtsgen.processor.model;


import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TypeScriptModel {

    // java package -> TSModuleInfo
    private TSModuleInfo moduleInfo = TSModuleInfoBuilder.builder().excludes(defaultExclusion()).build();


    private static List<Pattern> defaultExclusion() {
        return Arrays.stream(new String[]{"^sun", "^jdk.internal","^java.lang.Comparable"}).map(Pattern::compile).collect(Collectors.toList());
    }

    // all converted TS Types
    private final List<TSType> tsTypes=new ArrayList<>();

    // a list of java types to ts mapping, that are added indirectly
    private final Map<String, TSTargetType> tsTargetTypes = new HashMap<>();

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
        this(ts.getTsTypes(), ts.getTsTargetTypes(), ts.getModuleInfo());
    }

    private TypeScriptModel(List<TSType> tsTypes, Map<String, TSTargetType> tsTargetTypes, TSModuleInfo moduleInfo) {
        this.tsTypes.addAll(tsTypes);
        this.tsTargetTypes.putAll(tsTargetTypes);
        this.moduleInfo= moduleInfo;
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

    public TypeScriptModel withMappedData(List<TSType> mappedTSTypes) {
        return new TypeScriptModel(mappedTSTypes,this.getTsTargetTypes(),this.moduleInfo);
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

    private Map<String, TSTargetType> getTsTargetTypes() {
        return tsTargetTypes;
    }

    public boolean usesDefaultNameSpaceMapping() {
        return moduleInfo.getNameSpaceMappings().size()==0;
    }

}
