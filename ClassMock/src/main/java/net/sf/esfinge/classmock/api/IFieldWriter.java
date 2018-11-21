package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible for define all properties at field level.
 */
public interface IFieldWriter extends IAnnotationWriter {

    /**
     * Define the name of your field.
     *
     * @param name
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter name(final String name);

    /**
     * Define the class type of your field. If not specified,
     * the default type is the same type of the generated entity.
     *
     * @param type
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter type(final Class<?> type);

    /**
     * Define the parameter type of your field.
     *
     * @param generics
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter generics(Class<?> generics);

    /**
     * Define the field's initial value. This parameter, which may be
     * <tt>null</tt> if the field does not have an initial value,
     * must be an {@link Integer}, a {@link Float}, a {@link Long}, a
     * {@link Double} or a {@link String} (for <tt>int</tt>,
     * <tt>float</tt>, <tt>long</tt> or <tt>String</tt> fields
     * respectively). <b>This parameter is only used for static
     * fields. Its value is ignored for non static fields</b>, which
     * must be initialized through bytecode instructions in
     * constructors or methods.
     *
     * @param value
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter value(Object value);

    /**
     * Define the visibility for your field.
     *
     * <p>
     * Ex: PUBLIC, PRIVATE or PROTECTED
     *
     * @param visibility
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter visibility(final VisibilityEnum visibility);

    /**
     * Define the modifiers that you want in your field.
     *
     * <p>
     * Ex: FINAL, STATIC...
     *
     * @param modifiers
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter modifiers(ModifierEnum... modifiers);

    /**
     * Define if is necessary to generate a getter method for this field.
     *
     * @param getter
     *            if the field should have the getter method
     * @return IFieldWriter
     */
    IFieldWriter hasGetter(boolean getter);

    /**
     * Define if is necessary to generate a setter method for this field.
     *
     * @param setter
     *            if the field should have the setter method
     * @return IFieldWriter
     */
    IFieldWriter hasSetter(boolean setter);
}