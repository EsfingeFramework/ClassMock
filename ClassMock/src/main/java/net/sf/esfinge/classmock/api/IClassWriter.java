package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.JavaEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible for define all properties at class level.
 */
public interface IClassWriter extends IAnnotationWriter {

    /**
     * Define your generated entity to be a <i>Concrete Class</i> (default).
     *
     * @return IClassWriter
     */
    IClassWriter asClass();

    /**
     * Define your generated entity to be an <i>Interface</i>.
     *
     * @return IClassWriter
     */
    IClassWriter asInterface();

    /**
     * Define your generated entity to be an <i>Abstract Class</i>.
     *
     * @return IClassWriter
     */
    IClassWriter asAbstract();

    /**
     * Define your generated entity to be an <i>Enum</i>.
     *
     * @return IClassWriter
     */
    IClassWriter asEnum();

    /**
     * Define your generated entity to be an <i>Annotation</i>.
     *
     * @return IClassWriter
     */
    IClassWriter asAnnotation();

    /**
     * Define the version of JRE that your entity will be compiled.
     *
     * @param javaEnum
     *            the version of java entity
     * @return IClassWriter
     */
    IClassWriter version(JavaEnum javaEnum);

    /**
     * Define the name of your entity, you can also inform the package as a prefix.
     *
     * <p>
     * Ex: my.fake.package.MyGenericDynClass
     *
     * @param name
     *            of the entity
     * @return IClassWriter
     */
    IClassWriter name(String name);

    /**
     * Define the visibility of your entity.
     *
     * <p>
     * Ex: PUBLIC, PRIVATE or PROTECTED
     *
     * @param visibility
     *            of the entity
     * @return IClassWriter
     */
    IClassWriter visibility(VisibilityEnum visibility);

    /**
     * Define the modifiers that you want in your entity.
     *
     * <p>
     * Ex: FINAL, ABSTRACT...
     *
     * @param modifiers
     *            of the entity
     * @return IClassWriter
     */
    IClassWriter modifiers(ModifierEnum... modifiers);

    /**
     * Define a super class that your generated entity will extends.
     *
     * @param superclass
     *            to be extended
     * @return ISuperClassWriter
     */
    ISuperClassWriter superclass(Class<?> superclass);

    /**
     * Define the interfaces that your generated entity will implements.
     *
     * @param classes
     *            of interfaces
     * @return IClassWriter
     */
    IClassWriter interfaces(Class<?>... classes);

    /**
     * Add a field to your entity.
     *
     * @param name
     *            of the field
     * @param type
     *            class type of the field
     * @return IFieldWriter
     */
    IFieldWriter field(String name, Class<?> type);

    /**
     * Add a method to your entity.
     *
     * @param name
     *            of the method to be added
     * @return IMethodWriter
     */
    IMethodWriter method(String name);

    /**
     * Add a method to your entity.
     *
     * @param method
     *            to be added
     * @return IMethodWriter
     */
    IMethodWriter method(IMethodReader method);

    /**
     * Build your entity.
     *
     * @return entity created
     */
    Class<?> build();
}