package net.sf.esfinge.classmock.api;

import java.util.Collection;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

public interface IFieldReader {

    /**
     * @return the name of the field
     */
    String name();

    /**
     * @return the type of the field
     */
    Class<?> type();

    /**
     * @return the generics of the field
     */
    Class<?> generics();

    /**
     * Only used in constants
     *
     * @return the value of the field
     */
    Object value();

    /**
     * @return the visibility of the field
     */
    VisibilityEnum visibility();

    /**
     * @return collection of modifiers of the field
     */
    Collection<ModifierEnum> modifiers();

    /**
     * @return collection of annotations of the field
     */
    Collection<IAnnotationReader> annotations();

    /**
     * @return if the field has the getter method
     */
    boolean hasGetter();

    /**
     * @return if the field has the setter method
     */
    boolean hasSetter();
}