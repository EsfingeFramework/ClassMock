package net.sf.esfinge.classmock.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.esfinge.classmock.api.ISuperClassReader;
import net.sf.esfinge.classmock.api.ISuperClassWriter;

public class SuperClassImp implements ISuperClassReader, ISuperClassWriter {

    private Class<?> superclass;

    private final List<Class<?>> generics = new ArrayList<>();

    /**
     * @param superclass
     *            to be associate
     */
    public SuperClassImp(final Class<?> superclass) {

        this.superclass = superclass;
    }

    @Override
    public ISuperClassWriter superclass(final Class<?> superclass) {

        this.superclass = superclass;
        return this;
    }

    @Override
    public ISuperClassWriter generics(final Class<?> genericParameter) {

        this.generics.add(genericParameter);
        return this;
    }

    @Override
    public Class<?> superclass() {

        return this.superclass;
    }

    @Override
    public Collection<Class<?>> generics() {

        return this.generics;
    }
}