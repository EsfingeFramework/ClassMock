package net.sf.esfinge.classmock.api;

import java.util.Collection;

import net.sf.esfinge.classmock.api.enums.JavaEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

public interface IClassReader {

    boolean isClass();

    boolean isInterface();

    boolean isAbstract();

    String name();

    JavaEnum version();

    ISuperClassReader superclass();

    VisibilityEnum visibility();

    Collection<ModifierEnum> modifiers();

    Collection<Class<?>> interfaces();

    Collection<IFieldReader> fields();

    Collection<IMethodReader> methods();

    Collection<IAnnotationReader> annotations();
}