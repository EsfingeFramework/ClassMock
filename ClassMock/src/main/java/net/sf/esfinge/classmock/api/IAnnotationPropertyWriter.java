package net.sf.esfinge.classmock.api;

/**
 * Class responsible to define properties for annotations.
 */
public interface IAnnotationPropertyWriter {

    /**
     * Define the value to the property named as "value".
     *
     * @param value
     *            to set
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter property(Object value);

    /**
     * Define the value to the respective property
     *
     * @param property
     *            the name of the property to set
     * @param value
     *            the value of the property set
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter property(final String property, final Object value);

    /**
     * This method allows you to add a new annotation fluently to the same element that you are now defining.
     *
     * @return IAnnotationWriter
     */
    IAnnotationWriter and();
}