package net.sf.esfinge.classmock.api.enums;

import org.objectweb.asm.Opcodes;

/**
 * Class responsible to inform the modifiers for classes, fields and methods.
 */
public enum ModifierEnum {

    /** ANNOTATION to class */
    ANNOTATION(Opcodes.ACC_ANNOTATION), // class

    /** ENUM to class(?) field inner */
    ENUM(Opcodes.ACC_ENUM), // class(?) field inner

    /** SUPER to class */
    SUPER(Opcodes.ACC_SUPER), // class

    /** INTERFACE to class */
    INTERFACE(Opcodes.ACC_INTERFACE), // class

    /** ABSTRACT to class and method */
    ABSTRACT(Opcodes.ACC_ABSTRACT), // class, method

    /** STATIC to field and method */
    STATIC(Opcodes.ACC_STATIC), // field, method

    /** FINAL to class, field, method andparameter */
    FINAL(Opcodes.ACC_FINAL), // class, field, method, parameter

    /** SYNCHRONIZED to method */
    SYNCHRONIZED(Opcodes.ACC_SYNCHRONIZED), // method

    /** VOLATILE to field */
    VOLATILE(Opcodes.ACC_VOLATILE), // field

    /** BRIDGE to method */
    BRIDGE(Opcodes.ACC_BRIDGE), // method

    /** VARARGS to method */
    VARARGS(Opcodes.ACC_VARARGS), // method

    /** TRANSIENT to field */
    TRANSIENT(Opcodes.ACC_TRANSIENT), // field

    /** NATIVE to method */
    NATIVE(Opcodes.ACC_NATIVE), // method

    /** STRICT to method */
    STRICT(Opcodes.ACC_STRICT), // method

    /** SYNTHETIC to class, field, method and parameter */
    SYNTHETIC(Opcodes.ACC_SYNTHETIC), // class, field, method, parameter

    /** MANDATED to parameter */
    MANDATED(Opcodes.ACC_MANDATED); // parameter

    private int opCodes;

    private ModifierEnum(final int opCodes) {

        this.opCodes = opCodes;
    }

    /**
     * @return code from ASM
     */
    public int getOpCodes() {

        return this.opCodes;
    }
}