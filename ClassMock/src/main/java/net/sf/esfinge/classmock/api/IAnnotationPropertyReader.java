package net.sf.esfinge.classmock.api;

import java.util.Map;

/**
 * Class responsible to retrieve properties from an annotations.
 */
public interface IAnnotationPropertyReader {

    /**
     * List all properties and values that are in an annotation.
     *
     * @return map of properties of name and value.
     */
    Map<String, Object> properties();

    /**
     * Inform the element that will receive more annotation fluently.
     *
     * @param writer
     *            to set
     */
    void setAnd(IAnnotationWriter writer);
}