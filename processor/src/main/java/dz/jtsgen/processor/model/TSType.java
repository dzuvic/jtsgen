package dz.jtsgen.processor.model;

import dz.jtsgen.processor.util.StringUtils;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TSType {
    private final String namespace;
    private final String name;
    private final String qualifiedName;
    private final List<TSMember> members = new ArrayList<>();
    private String documentString;
    private List<TSType> superTypes = new ArrayList<>();
    private Element element;

    public TSType(Element e, String qualifiedName) {
        assert qualifiedName != null;
        this.qualifiedName = qualifiedName;
        this.name = StringUtils.lastOf(qualifiedName);
        this.namespace = StringUtils.untill(qualifiedName);
        this.element = e;
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

    public String getQualifiedName() {
        return qualifiedName;
    }

    public Optional<Element> getElement() {
        return Optional.ofNullable(element);
    }

    protected StringBuilder toStringBuilder() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName()).append("{");
        builder.append("qualifiedName=").append(this.qualifiedName).append("; ");
        if (!superTypes.isEmpty()) builder.append("superTypes=").append(this.getSuperTypes().toString()).append("; ");
        if (!members.isEmpty()) builder.append("members=").append(this.members.toString()).append("; ");
        return builder;
    }


    @Override
    public String toString() {
        return this.toStringBuilder().append("}").toString();
    }

}
