package dz.jtsgen.processor.jtp.conv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.AbstractTypeVisitor8;

import dz.jtsgen.processor.jtp.conv.visitors.JavaTypeConverter;
import dz.jtsgen.processor.jtp.info.TSProcessingInfo;
import dz.jtsgen.processor.model.TSExecutableMemberBuilder;
import dz.jtsgen.processor.model.TSRegularMember;
import dz.jtsgen.processor.model.TSRegularMemberBuilder;
import dz.jtsgen.processor.model.TSTargetType;

class PreserveExecutablesJavaTypeElementExtractingVisitor extends JavaTypeElementExtractingVisitor {
  private static Logger LOG = Logger.getLogger(PreserveExecutablesJavaTypeElementExtractingVisitor.class.getName());

  PreserveExecutablesJavaTypeElementExtractingVisitor(TypeElement typeElementToConvert, TSProcessingInfo visitorParam, JavaTypeConverter javaTypeConverter) {
    super(typeElementToConvert,visitorParam,javaTypeConverter);
  }

  @Override
  public Void visitExecutable(ExecutableElement e, Void notcalled) {
    LOG.fine(() -> String.format("JTExV visiting executable %s", e.toString()));
    final String rawName = e.getSimpleName().toString();  //  nameOfMethod(e).orElse("");
    final String name = mappedName(rawName);
    final boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
    final boolean isIgnored = isIgnored(e);
    final boolean isReadOnly = readOnlyAnnotation(e) || readOnlyAnnotation(this.typeElementToConvert);
    final boolean isInit = rawName.startsWith("<");
    if (!isPublic ||  isIgnored || isInit) return null; // return early for not converting private types
    final TSTargetType returnType = convertTypeMirrorToTsType(e, tsProcessingInfo);
    LOG.fine(() -> "is getter or setter: " + (isPublic ? "public " : " ") + e.getSimpleName() + " -> " + name +"(" + rawName+ ")" + ":" + returnType + " " +(isIgnored?"(ignored)":""));


    final List<TSRegularMember> paramMembers = new ArrayList<>();
    final List<? extends VariableElement> functionParams = e.getParameters();
    for (VariableElement functionParam : functionParams) {
      final String paramName = functionParam.getSimpleName().toString();
      final TSTargetType paramType = convertTypeMirrorOfMemberToTsType(functionParam, tsProcessingInfo);
      paramMembers.add(TSRegularMemberBuilder.of(paramName, paramType, false));
    }

    final TSRegularMember [] parameters = paramMembers.toArray(new TSRegularMember[0]);
    if (members.containsKey(name)) {
      // can't be read only anymore
      final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
      members.put(name, TSExecutableMemberBuilder
          .of(
              name,
              isGetter(e) ? returnType : members.get(name).getType(),
              isReadOnly,
              parameters)
          .withComment(comment)
      );
    } else {
      final Optional<String> comment = Optional.ofNullable(this.tsProcessingInfo.getpEnv().getElementUtils().getDocComment(e));
      members.put(name, TSExecutableMemberBuilder
          .of(name, returnType, isReadOnly, parameters)
          .withComment(comment)
      );
    }
    extractableMembers.add(name);
    return null;
  }

  protected TSTargetType convertTypeMirrorToTsType(ExecutableElement theElement, TSProcessingInfo tsProcessingInfo) {
    AbstractTypeVisitor8<TSTargetType, Void> visitor = new MirrorTypeToTSConverterVisitor(theElement, tsProcessingInfo, javaTypeConverter);
    return visitor.visit(theElement.getReturnType());
  }

}