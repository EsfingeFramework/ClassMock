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
     * The default type is the same type of the generated entity.
     *
     * @param name
     *            of the field
     * @return IFieldWriter
     */
    IFieldWriter field(String name);

    /**
     * Define a way to parse a field with all properties and annotations from a String.
     * You must remember this:
     * <p>
     * - Semicolon is Optional
     * <p>
     * - Is better specify all class type with their respective packages,
     * if you don't it will try to find your class type for you.
     * So, it is possible to load a class type with the <b>same name</b> but in a <b>different package</b>.
     * <p>
     * <code>
     * Ex:<br>
     * java.lang.String myField; <i>// the default visibility is private</i><br><br>
     * Ex:<br>
     * final static String myField; <i>// with modifiers (static and final)</i><br><br>
     * Ex:<br>
     * public String myField; <i>// with visibility (public)</i><br><br>
     * Ex:<br>
     * private static final String myField; <i>// with visibility and modifiers</i><br><br>
     * Ex:<br>
     * &#64;Id <i>// with annotation</i><br>
     * &#64;Column(name = \"MY_COLUMN_NAME\", nullable = false) <i>// annotation and properties</i><br>
     * private static final String myField; <i>// with visibility and modifiers</i>
     * </code>
     *
     * @param fieldSignature
     *            the signature of your field to parse.
     * @return IFieldWriter
     */
    IFieldWriter fieldByParse(String fieldSignature);

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
     * Define a way to parse a method with all properties and annotations from a String.
     * You must remember this:
     * <p>
     * - parentheses and braces are Optional
     * <p>
     * - Is better specify all class type with their respective packages,
     * if you don't it will try to find your class type for you.
     * So, it is possible to load a class type with the <b>same name</b> but in a <b>different package</b>.
     * <p>
     * <code>
     * Ex:<br>
     * java.lang.String getMyMethod(){}; <i>// the default visibility is public</i><br><br>
     * Ex:<br>
     * final static String getMyMethod; <i>// with modifiers (static and final)</i><br><br>
     * Ex:<br>
     * public String getMyMethod; <i>// with visibility (public)</i><br><br>
     * Ex:<br>
     * private static final String getMyMethod; <i>// with visibility and modifiers</i><br><br>
     * Ex:<br>
     * &#64;Id <i>// with annotation</i><br>
     * &#64;Column(name = \"MY_COLUMN_NAME\", nullable = false) <i>// annotation</i><br>
     * private static final String getMyMethod; <i>// with visibility and modifiers</i><br><br>
     * Ex:<br>
     * public String findById(@NotNull String code) throws NullPointerException; <i>// with parameter and exception</i>
     * </code>
     *
     * @param methodSignature
     *            the signature of your method to parse.
     * @return IMethodWriter
     */
    IMethodWriter methodByParse(String methodSignature);

    /**
     * Build your entity.
     *
     * @return entity created
     */
    Class<?> build();

    /**
     * Define a clone of all defined for your entity,
     * but with a new name because the old one is already in use.
     *
     * @param name
     *            of your new entity
     * @return a new instance of IClassWriter
     */
    IClassWriter clone(String name);
}