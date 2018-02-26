package net.sf.esfinge.classmock.api;

import java.util.Collection;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible to read all properties at method level.
 */
public interface IMethodReader {

    /**
     * Inform the name of your method.
     *
     * @return name of the method
     */
    String name();

    /**
     * Inform the class type returned of your method.
     *
     * @return type of the returned value
     */
    Class<?> returnType();

    /**
     * Inform the visibility of your method.
     *
     * @return visibility of the method
     */
    VisibilityEnum visibility();

    /**
     * Inform the modifiers that you want in your method.
     *
     * @return modifiers of the method
     */
    Collection<ModifierEnum> modifiers();

    /**
     * Inform the parameters that you want in your method.
     *
     * @return collection of parameters
     */
    Collection<IFieldReader> parameters();

    /**
     * Inform the annotations that you want in your method.
     *
     * @return collection of annotations
     */
    Collection<IAnnotationReader> annotations();

    /**
     * Inform the exceptions that you want in your method to throw.
     *
     * @return collection of exceptions
     */
    Collection<Class<?>> exceptions();

    /**
     * Inform the default value to your method.
     *
     * <p>
     * ONLY USED FOR ANNOTATION !!!!
     *
     * @return value
     */
    Object value();
}