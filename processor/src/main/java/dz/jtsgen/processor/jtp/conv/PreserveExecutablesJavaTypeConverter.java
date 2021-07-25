package dz.jtsgen.processor.jtp.conv;

import java.util.Collection;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import dz.jtsgen.processor.jtp.info.TSProcessingInfo;
import dz.jtsgen.processor.model.TSMember;

public class PreserveExecutablesJavaTypeConverter extends DefaultJavaTypeConverter {

  PreserveExecutablesJavaTypeConverter(TSProcessingInfo processingInfo) {
    super(processingInfo);
  }

  // TODO: the member lookup has changed in HEAD vs. the 0.5.0 release -- this needs to be updated for the new lookup scheme
  protected Collection<? extends TSMember> findMembers(TypeElement e) {
    LOG.fine(() -> "DJTC find members in  in java type " + e);
    JavaTypeElementExtractingVisitor visitor = new PreserveExecutablesJavaTypeElementExtractingVisitor(e, processingInfo, this);
    e.getEnclosedElements().stream()
        .filter(x -> x.getKind() == ElementKind.FIELD || x.getKind() == ElementKind.METHOD)
        .forEach(visitor::visit);
    return visitor.getMembers();
  }
}