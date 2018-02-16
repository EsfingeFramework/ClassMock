package net.sf.esfinge.classmock.api;

import java.util.Collection;

/**
 * Class responsible to define the super class and it generic signature.
 */
public interface ISuperClassReader {

    /**
     * Inform the super class that your generated class will extends.
     *
     * @return class to be extended
     */
    Class<?> superclass();

    /**
     * List all the parameters type used at the generic signature.
     * 
     * @return collection of parameters
     */
    Collection<Class<?>> generics();
}