package net.sf.esfinge.classmock.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.sf.esfinge.classmock.api.ISuperClassReader;
import net.sf.esfinge.classmock.api.ISuperClassWriter;

/**
 * Class responsible for implement all definitions of a super class.
 */
public class SuperClassImp implements ISuperClassReader, ISuperClassWriter {

    private Class<?> superclass;

    private final List<Class<?>> generics = new ArrayList<>();

    /**
     * Constructor to receive the super class.
     *
     * @param superclass
     *            to bind
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

    @Override
    public int hashCode() {

        return new HashCodeBuilder()
                        .append(this.superclass())
                        .append(this.generics())
                        .build();
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final SuperClassImp other = (SuperClassImp) obj;

        return new EqualsBuilder()
                        .append(this.superclass(), other.superclass())
                        .append(this.generics(), other.generics())
                        .build();
    }
}