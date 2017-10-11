package net.sf.esfinge.classmock.api.enums;

import org.objectweb.asm.Opcodes;

public enum VisibilityEnum {

    /** PUBLIC */
    PUBLIC(Opcodes.ACC_PUBLIC),

    /** PRIVATE */
    PRIVATE(Opcodes.ACC_PRIVATE),

    /** PROTECTED */
    PROTECTED(Opcodes.ACC_PROTECTED);

    private int opCodes;

    private VisibilityEnum(final int opCodes) {

        this.opCodes = opCodes;
    }

    /**
     * @return code from ASM
     */
    public int getOpCodes() {

        return this.opCodes;
    }
}