package dz.jtsgen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * constructors annotated with this will be included in the generated TS interface definitions
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = {ElementType.CONSTRUCTOR})
public @interface TSConstructor {
}
