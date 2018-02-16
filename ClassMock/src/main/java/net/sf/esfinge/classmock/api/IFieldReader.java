package net.sf.esfinge.classmock.api;

import java.util.Collection;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible to read all properties at field level.
 */
public interface IFieldReader {

    /**
     * Inform the name of your field.
     *
     * @return the name of the field
     */
    String name();

    /**
     * Inform the class type of your field.
     *
     * @return the type of the field
     */
    Class<?> type();

    /**
     * Inform the parameter type of your field.
     *
     * @return the generics of the field
     */
    Class<?> generics();

    /**
     * Define the value of your field.
     *
     * @return the value of the field
     */
    Object value();

    /**
     * Inform the visibility of your field.
     *
     * @return the visibility of the field
     */
    VisibilityEnum visibility();

    /**
     * Inform the modifiers that you want in your field.
     *
     * @return collection of modifiers of the field
     */
    Collection<ModifierEnum> modifiers();

    /**
     * Inform the annotations that you want in your field.
     *
     * @return collection of annotations of the field
     */
    Collection<IAnnotationReader> annotations();

    /**
     * Inform if is necessary to generate a getter method for this field.
     *
     * @return if the field has the getter method
     */
    boolean hasGetter();

    /**
     * Inform if is necessary to generate a setter method for this field.
     *
     * @return if the field has the setter method
     */
    boolean hasSetter();
}