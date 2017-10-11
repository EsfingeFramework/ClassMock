package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.JavaEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

public interface IClassWriter extends IAnnotationWriter {

    /**
     * Set the output as a regular class
     *
     * @return IClassWriter
     */
    IClassWriter asClass();

    /**
     * Set the output as a interface
     *
     * @return IClassWriter
     */
    IClassWriter asInterface();

    /**
     * Set the output as a abstract class
     *
     * @return IClassWriter
     */
    IClassWriter asAbstract();

    /**
     *
     * @param javaEnum
     *            the version of java entity
     * @return IClassWriter
     */
    IClassWriter version(JavaEnum javaEnum);

    /**
     * @param name
     *            of the entity
     * @return IClassWriter
     */
    IClassWriter name(String name);

    /**
     * @param visibility
     *            of the entity
     * @return IClassWriter
     */
    IClassWriter visibility(VisibilityEnum visibility);

    /**
     * @param modifiers
     *            of the entity
     * @return IClassWriter
     */
    IClassWriter modifiers(ModifierEnum... modifiers);

    /**
     * @param superclass
     *            to be extended
     * @return ISuperClassWriter
     */
    ISuperClassWriter superclass(Class<?> superclass);

    /**
     * @param classes
     *            of interfaces
     * @return IClassWriter
     */
    IClassWriter interfaces(Class<?>... classes);

    /**
     * @param name
     *            of the field
     * @param type
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter field(String name, Class<?> type);

    /**
     * @param name
     *            of the method
     * @return IMethodWriter
     */
    IMethodWriter method(String name);

    /**
     * @param method
     *            to be added
     * @return IMethodWriter
     */
    IMethodWriter method(IMethodReader method);

    /**
     * @return the entity builded
     */
    Class<?> build();
}