package net.sf.esfinge.classmock.api;

public interface ISuperClassWriter {

    /**
     * @param superclass
     *            to be extend
     * @return ISuperClassWriter
     */
    ISuperClassWriter superclass(Class<?> superclass);

    /**
     * @param genericParameter
     *            of the annotation
     * @return ISuperClassWriter
     */
    ISuperClassWriter generics(Class<?> genericParameter);
}