package net.sf.esfinge.classmock.api.enums;

import org.objectweb.asm.Opcodes;

public enum JavaEnum {

    /** V1_1 */
    V1_1(Opcodes.V1_1),

    /** V1_2 */
    V1_2(Opcodes.V1_2),

    /** V1_3 */
    V1_3(Opcodes.V1_3),

    /** V1_4 */
    V1_4(Opcodes.V1_4),

    /** V1_5 */
    V1_5(Opcodes.V1_5),

    /** V1_6 */
    V1_6(Opcodes.V1_6),

    /** V1_7 */
    V1_7(Opcodes.V1_7),

    /** V1_8 */
    V1_8(Opcodes.V1_8);

    private int opCodes;

    private JavaEnum(final int opCodes) {

        this.opCodes = opCodes;
    }

    /**
     * @return code from ASM
     */
    public int getOpCodes() {

        return this.opCodes;
    }
}