package dz.jtsgen.processor.model;

import dz.jtsgen.processor.model.rendering.TSTypeElement;

import javax.lang.model.element.Element;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TSType implements TSTypeElement {
    private final String namespace;
    private final String name;
    private final List<TSMember> members = new ArrayList<>();
    private String documentString;
    private List<TSType> superTypes = new ArrayList<>();
    private Element element;

    public TSType(Element e, String namespace, String name) {
        assert namespace != null;
        assert name != null;

        this.element = e;
        this.namespace = namespace;
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public List<TSMember> getMembers() {
        return members;
    }

    public String getDocumentString() {
        return documentString;
    }


    public List<TSType> getSuperTypes() {
        return superTypes;
    }

    public Optional<Element> getElement() {
        return Optional.ofNullable(element);
    }

    protected StringBuilder toStringBuilder() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName()).append("{");
        if (!superTypes.isEmpty()) builder.append("superTypes=")
                .append(this.getSuperTypes().stream().map( x -> x!=this ? x.toString() : "self").collect(Collectors.joining(",")))
                .append("; ");
        if (!members.isEmpty()) builder.append("members=").append(this.members.toString()).append("; ");
        return builder;
    }


    @Override
    public String toString() {
        return this.toStringBuilder().append("}").toString();
    }

    public abstract String getKeyword();

    public TSType addMembers(Collection<? extends TSMember> members) {
        this.getMembers().addAll(members);
        return this;
    }

    public TSType addSuperTypes(Collection<TSType> supertypes) {
        this.getSuperTypes().addAll(supertypes.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return this;
    }
}
