package net.sf.esfinge.classmock.api;

public interface IAnnotationPropertyWriter {

    /**
     *
     * @param value
     *            to set
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter property(Object value);

    /**
     *
     * @param property
     *            the name of the property to set
     * @param value
     *            the value of the property set
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter property(final String property, final Object value);

    /**
     * @return IAnnotationWriter
     */
    IAnnotationWriter and();
}