package net.sf.classmock.example.basic;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Domain {
	   String value() default "";
	   int outra() default 0;
}
