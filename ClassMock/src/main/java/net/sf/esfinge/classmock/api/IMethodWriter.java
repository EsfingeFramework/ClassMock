package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible for define all properties at field level.
 */
public interface IMethodWriter extends IAnnotationWriter {

    /**
     * Define the name of your method.
     *
     * @param name
     *            of the method
     * @return IMethodWriter
     */
    IMethodWriter name(String name);

    /**
     * Define the class type returned of your method. If not specified,
     * the default type is the same type of the generated entity.
     *
     * @param clazz
     *            of the returned type of the method
     * @return IMethodWriter
     */
    IMethodWriter returnType(Class<?> clazz);

    /**
     * Define the class type name of your method as void.
     *
     * @return set return as void
     */
    IMethodWriter returnTypeAsVoid();

    /**
     * The default type is the same type of the generated entity.
     *
     * @return set return as the one of the entity.
     */
    IMethodWriter returnTypeAsSelfType();

    /**
     * Define the visibility for your method.
     *
     * <p>
     * Ex: PUBLIC, PRIVATE or PROTECTED
     *
     * @param visibility
     *            of the method
     * @return IMethodWriter
     */
    IMethodWriter visibility(VisibilityEnum visibility);

    /**
     * Define the modifiers that you want in your method.
     *
     * <p>
     * Ex: FINAL, ABSTRACT...
     *
     * @param modifiers
     *            to the method
     * @return IMethodWriter
     */
    IMethodWriter modifiers(ModifierEnum... modifiers);

    /**
     * Define the exceptions that you want in your method throw.
     *
     * @param exceptions
     *            of the method
     * @return IMethodWriter
     */
    IMethodWriter exceptions(Class<?>... exceptions);

    /**
     * Define the default value that you want in your method.
     *
     * <p>
     * ONLY USED FOR ANNOTATION !!!!
     *
     * @param value
     *            to be default (annotations only)
     * @return IFieldWriter
     */
    IMethodWriter value(Object value);

    /**
     * Define the parameter that you want in your method.
     * The default type is the same type of the generated entity.
     *
     * @param name
     *            of the parameter
     * @return IFieldWriter
     */
    IFieldWriter parameter(String name);

    /**
     * Define the parameter that you want in your method.
     *
     * @param name
     *            of the parameter
     * @param type
     *            of the parameter
     * @return IFieldWriter
     */
    IFieldWriter parameter(String name, Class<?> type);
}