package net.sf.esfinge.classmock.api;

/**
 * Class responsible to inform the super class and it generic signature.
 */
public interface ISuperClassWriter {

    /**
     * Define a super class that your generated class will extends.
     *
     * @param superclass
     *            to be extend
     * @return ISuperClassWriter
     */
    ISuperClassWriter superclass(Class<?> superclass);

    /**
     * Define the parameter type used at the generic signature.
     *
     * @param genericParameter
     *            the type of class
     * @return ISuperClassWriter
     */
    ISuperClassWriter generics(Class<?> genericParameter);
}