package net.sf.esfinge.classmock.api;

import java.util.Map;

public interface IAnnotationPropertyReader {

    /**
     * @return map of properties of name and value.
     */
    Map<String, Object> properties();

    /**
     * @param writer
     *            to set
     */
    void setAnd(IAnnotationWriter writer);
}