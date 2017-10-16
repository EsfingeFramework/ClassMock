package net.sf.esfinge.classmock.api;

import java.util.Collection;

import net.sf.esfinge.classmock.api.enums.JavaEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

public interface IClassReader {

    /**
     * @return boolean
     */
    boolean isClass();

    /**
     * @return boolean
     */
    boolean isInterface();

    /**
     * @return boolean
     */
    boolean isAbstract();

    /**
     * @return boolean
     */
    boolean isEnum();

    /**
     * @return boolean
     */
    boolean isAnnotation();

    /**
     * @return the name of the entity
     */
    String name();

    /**
     * @return the version of the JAVA
     */
    JavaEnum version();

    /**
     * @return the reader of superclass
     */
    ISuperClassReader superclass();

    /**
     * @return the visibility of the entity
     */
    VisibilityEnum visibility();

    /**
     * @return a collection of modifiers of the entity
     */
    Collection<ModifierEnum> modifiers();

    /**
     * @return a collection of interfaces of the entity
     */
    Collection<Class<?>> interfaces();

    /**
     * @return a collection of fields of the entity
     */
    Collection<IFieldReader> fields();

    /**
     * @return a collection of methods of the entity
     */
    Collection<IMethodReader> methods();

    /**
     * @return a collection of annotations of the entity
     */
    Collection<IAnnotationReader> annotations();
}