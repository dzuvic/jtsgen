package dz.jtsgen.processor.model;

import java.util.Optional;

import org.immutables.value.Value;

import dz.jtsgen.processor.model.rendering.TSMemberVisitor;

@Value.Immutable
public abstract class TSExecutableMember implements TSMember {

  @Value.Parameter
  public abstract String getName() ;

  @Value.Parameter
  public abstract TSTargetType getType();

  @Value.Parameter
  public abstract boolean getReadOnly();

  @Value.Default
  public boolean getNullable() {
    return false;
  }

  @Value.Default
  public boolean getOptional() {
    return false;
  }

  public abstract Optional<String> getComment();

  @Value.Default
  public boolean getInvalid() {
    return false;
  }

  @Value.Parameter
  public abstract TSRegularMember[] getParameters();

  @Override
  public void accept(TSMemberVisitor visitor, int ident) {
    visitor.visit(this, ident);
  }

  @Override
  public TSMember changedTSTarget(TSTargetType newTargetType) {
    return TSExecutableMemberBuilder.copyOf(this).withType(newTargetType);
  }
}
