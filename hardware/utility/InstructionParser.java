package hardware.utility;

import java.util.BitSet;

public class InstructionParser {

    public static final String OPCODE_R = "000000";
    public static final String OPCODE_LW = "000001";
    public static final String OPCODE_SW = "000010";
    public static final String OPCODE_ADDI = "000101";
    public static final String OPCODE_J = "000110";
    public static final String OPCODE_BEQ = "000011";
    public static final String OPCODE_BGEZ = "000100";

    public static final String FUNC_ADD = "000000";
    public static final String FUNC_SUB = "000001";
    public static final String FUNC_AND = "000010";
    public static final String FUNC_OR = "000011";
    public static final String FUNC_XOR = "000100";
    public static final String FUNC_SLL = "000101";
    public static final String FUNC_SRL = "000110";
    public static final String FUNC_SLT = "000111";

    public static BitSet parse(String instruction) {
        String opcode = OPCODE_R, r1 = "00000", r2 = "00000", r3 = "00000", immediate = "0000000000000000", func = "00000", sa = "00000", jaddress = "0000000000000000";
        String zeroes = "00000000000000000000000000000000";

        String[] parts = instruction.split(" ");

        switch (parts[0].toLowerCase()) {
            case "add":
                opcode = OPCODE_R;
                func = FUNC_ADD;
                break;
            case "sub":
                opcode = OPCODE_R;
                func = FUNC_SUB;
                break;
            case "and":
                opcode = OPCODE_R;
                func = FUNC_AND;
                break;
            case "or":
                opcode = OPCODE_R;
                func = FUNC_OR;
                break;
            case "xor":
                opcode = OPCODE_R;
                func = FUNC_XOR;
                break;
            case "sll":
                opcode = OPCODE_R;
                func = FUNC_SLL;
                break;
            case "srl":
                opcode = OPCODE_R;
                func = FUNC_SRL;
                break;
            case "slt":
                opcode = OPCODE_R;
                func = FUNC_SLT;
                break;
            case "lw":
                opcode = OPCODE_LW;
                break;
            case "sw":
                opcode = OPCODE_SW;
                break;
            case "addi":
                opcode = OPCODE_ADDI;
                break;
            case "j":
                opcode = OPCODE_J;
                break;
            case "beq":
                opcode = OPCODE_BEQ;
                break;
            case "bgez":
                opcode = OPCODE_BGEZ;

        }

        switch (opcode) {
            case OPCODE_R:
                r1 = Integer.toBinaryString(Integer.parseInt(parts[1]));
                r1 = zeroes.substring(0, 5-r1.length()).concat(r1);

                r2 = Integer.toBinaryString(Integer.parseInt(parts[2]));
                r2 = zeroes.substring(0, 5-r2.length()).concat(r2);

                r3 = Integer.toBinaryString(Integer.parseInt(parts[3]));
                r3 = zeroes.substring(0, 5-r3.length()).concat(r3);

                if (func == FUNC_SLL || func == FUNC_SRL) {
                    return BitUtility.StringToBin(opcode + "00000" + r1 + r2 + r3 + func);
                }

                return BitUtility.StringToBin(opcode + r1 + r2 + r3 + sa + func);

            case OPCODE_LW: case OPCODE_SW: case OPCODE_ADDI: case OPCODE_BEQ: case OPCODE_BGEZ:
                r1 = Integer.toBinaryString(Integer.parseInt(parts[1]));
                System.out.println("r1: " + r1);
                r1 = zeroes.substring(0, 5-r1.length()).concat(r1);

                r2 = Integer.toBinaryString(Integer.parseInt(parts[2]));
                r2 = zeroes.substring(0, 5-r2.length()).concat(r2);
                System.out.println("r2: " + r2);

                immediate = Integer.toBinaryString(Integer.parseInt(parts[3]));
                if(immediate.length() > 16) {
                    immediate = immediate.substring(immediate.length()-16);
                } else { // fill with zeroes
                    immediate = zeroes.substring(0, 16-immediate.length()).concat(immediate);
                }
                System.out.println("GENERATED I: " + opcode + r1 + r2 + immediate);
                return BitUtility.StringToBin(opcode + r1 + r2 + immediate);

            case OPCODE_J:
                jaddress = Integer.toBinaryString(Integer.parseInt(parts[1]));

                if(jaddress.length() > 26) {
                    jaddress = jaddress.substring(jaddress.length()-26);
                } else {
                    jaddress = zeroes.substring(0, 26-jaddress.length()).concat(jaddress);
                }
                System.out.println("GENERATED J: " + opcode + jaddress);
                return BitUtility.StringToBin(opcode + jaddress);
        }

        return new BitSet();
    }

}
