package hardware.utility;

public final class InstructionsRepo {
    public static final byte R = 0b000000;
    public static final byte LW = 0b000001;
    public static final byte SW = 0b000010;
    public static final byte BEQ = 0b000011;
    public static final byte BGEZ = 0b000100;
    public static final byte ADDI = 0b000101;
    public static final byte J = 0b000110;

    public static final byte ADD = 0b000000;
    public static final byte SUB = 0b000001;
    public static final byte AND = 0b000010;
    public static final byte OR = 0b000011;
    public static final byte XOR = 0b000100;
    public static final byte SLL = 0b000101;
    public static final byte SRL = 0b000110;
    public static final byte SLT = 0b000111;
}
