package net.sf.esfinge.classmock.api;

import java.util.Collection;

import net.sf.esfinge.classmock.api.enums.JavaEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible to read all properties at class level.
 */
public interface IClassReader {

    /**
     * Inform if it's a Concrete Class.
     *
     * @return yes or no
     */
    boolean isClass();

    /**
     * Inform if it's an Interface.
     *
     * @return yes or no
     */
    boolean isInterface();

    /**
     * Inform if it's an Abstract Class.
     *
     * @return yes or no
     */
    boolean isAbstract();

    /**
     * Inform if it's an Enum.
     *
     * @return yes or no
     */
    boolean isEnum();

    /**
     * Inform if it's an Annotation.
     *
     * @return yes or no
     */
    boolean isAnnotation();

    /**
     * Inform the defined name.
     *
     * @return the name of the entity
     */
    String name();

    /**
     * Inform the version of JRE that your entity will be compiled.
     *
     * @return the version of the JRE
     */
    JavaEnum version();

    /**
     * Inform the super class that your generated entity will extends.
     *
     * @return the reader of superclass
     */
    ISuperClassReader superclass();

    /**
     * Inform the visibility of your entity.
     *
     * @return the visibility
     */
    VisibilityEnum visibility();

    /**
     * Inform the modifiers that you want in your entity.
     *
     * @return a collection of modifiers
     */
    Collection<ModifierEnum> modifiers();

    /**
     * List the interfaces that your generated entity will implements.
     *
     * @return a collection of interfaces
     */
    Collection<Class<?>> interfaces();

    /**
     * List all the fields defined for your entity.
     *
     * @return a collection of fields
     */
    Collection<IFieldReader> fields();

    /**
     * List all the methods defined for your entity.
     *
     * @return a collection of methods
     */
    Collection<IMethodReader> methods();

    /**
     * List all the annotations defined for your entity.
     *
     * @return a collection of annotations
     */
    Collection<IAnnotationReader> annotations();
}