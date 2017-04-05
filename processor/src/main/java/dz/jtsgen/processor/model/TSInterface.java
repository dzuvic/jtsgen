package dz.jtsgen.processor.model;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

public class TSInterface extends TSType {


    public TSInterface(TypeElement e) {
        super(e, e.getQualifiedName().toString());
    }

    public TSInterface addMembers(Collection<? extends TSMember> members) {
        this.getMembers().addAll(members);
        return this;
    }
}
