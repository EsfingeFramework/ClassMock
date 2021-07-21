package net.sf.esfinge.classmock.api.enums;

import org.objectweb.asm.Opcodes;

/**
 * Class responsible to inform the access level for classes, fields and methods.
 */
public enum VisibilityEnum {

    /** PUBLIC */
    PUBLIC(Opcodes.ACC_PUBLIC),

    /** PRIVATE */
    PRIVATE(Opcodes.ACC_PRIVATE),

    /** PROTECTED */
    PROTECTED(Opcodes.ACC_PROTECTED);

    private int opCodes;

    VisibilityEnum(final int opCodes) {

        this.opCodes = opCodes;
    }

    /**
     * @return code from ASM
     */
    public int getOpCodes() {

        return this.opCodes;
    }
}