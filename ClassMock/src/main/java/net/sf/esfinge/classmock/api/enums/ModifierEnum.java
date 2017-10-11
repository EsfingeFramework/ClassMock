package net.sf.esfinge.classmock.api.enums;

import org.objectweb.asm.Opcodes;

public enum ModifierEnum {

    /** ENUM */
    ENUM(Opcodes.ACC_ENUM), // class(?) field inner

    /** SUPER */
    SUPER(Opcodes.ACC_SUPER), // class

    /** INTERFACE */
    INTERFACE(Opcodes.ACC_INTERFACE), // class

    /** ABSTRACT */
    ABSTRACT(Opcodes.ACC_ABSTRACT), // class, method

    /** STATIC */
    STATIC(Opcodes.ACC_STATIC), // field, method

    /** FINAL */
    FINAL(Opcodes.ACC_FINAL), // class, field, method, parameter

    /** SYNCHRONIZED */
    SYNCHRONIZED(Opcodes.ACC_SYNCHRONIZED), // method

    /** VOLATILE */
    VOLATILE(Opcodes.ACC_VOLATILE), // field

    /** BRIDGE */
    BRIDGE(Opcodes.ACC_BRIDGE), // method

    /** VARARGS */
    VARARGS(Opcodes.ACC_VARARGS), // method

    /** TRANSIENT */
    TRANSIENT(Opcodes.ACC_TRANSIENT), // field

    /** NATIVE */
    NATIVE(Opcodes.ACC_NATIVE), // method

    /** STRICT */
    STRICT(Opcodes.ACC_STRICT), // method

    /** SYNTHETIC */
    SYNTHETIC(Opcodes.ACC_SYNTHETIC), // class, field, method, parameter

    /** MANDATED */
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