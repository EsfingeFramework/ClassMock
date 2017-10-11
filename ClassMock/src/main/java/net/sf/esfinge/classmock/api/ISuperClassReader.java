package net.sf.esfinge.classmock.api;

import java.util.Collection;

public interface ISuperClassReader {

    /**
     * @return class to be extended
     */
    Class<?> superclass();

    /**
     * @return collection of parameters
     */
    Collection<Class<?>> generics();
}