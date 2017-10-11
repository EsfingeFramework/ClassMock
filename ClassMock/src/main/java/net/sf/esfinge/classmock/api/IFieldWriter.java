package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

public interface IFieldWriter extends IAnnotationWriter {

    /**
     * @param name
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter name(final String name);

    /**
     * @param type
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter type(final Class<?> type);

    /**
     * @param generics
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter generics(Class<?> generics);

    /**
     * @param value
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter value(Object value);

    /**
     * @param visibility
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter visibility(final VisibilityEnum visibility);

    /**
     * @param modifiers
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter modifiers(ModifierEnum... modifiers);

    /**
     * @param getter
     *            if the field should have the getter method
     * @return IFieldWriter
     */
    IFieldWriter hasGetter(boolean getter);

    /**
     * @param setter
     *            if the field should have the setter method
     * @return IFieldWriter
     */
    IFieldWriter hasSetter(boolean setter);
}