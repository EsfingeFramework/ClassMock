package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

public interface IMethodWriter extends IAnnotationWriter {

    /**
     * @param name
     *            of the method
     * @return IMethodWriter
     */
    IMethodWriter name(String name);

    /**
     * @param clazz
     *            of the returned type of the method
     * @return IMethodWriter
     */
    IMethodWriter returnType(Class<?> clazz);

    /**
     * @param visibility
     *            of the method
     * @return IMethodWriter
     */
    IMethodWriter visibility(VisibilityEnum visibility);

    /**
     * @param modifiers
     *            to the method
     * @return IMethodWriter
     */
    IMethodWriter modifiers(ModifierEnum... modifiers);

    /**
     * @param exceptions
     *            of the method
     * @return IMethodWriter
     */
    IMethodWriter exceptions(Class<?>... exceptions);

    /**
     * @param name
     *            of the parameter
     * @param type
     *            of the parameter
     * @return IFieldWriter
     */
    IFieldWriter parameter(String name, Class<?> type);
}