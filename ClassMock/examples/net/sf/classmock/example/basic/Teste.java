package net.sf.classmock.example.basic;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Teste {
   int valor() default 0;
   String value() default "";
   String[] array() default {};
   TesteEnum enumeration() default TesteEnum.TESTE1;
   Label label() default @Label("");
   Label[] labels() default {};
}
