package hardware.IF;

import hardware.utility.BitUtility;

import java.util.BitSet;

public class InstructionMemory{

    public BitSet[] Memory;

    public InstructionMemory() {
        Memory = new BitSet[64];
        for(int i = 0; i < 64; i++) {
            Memory[i] = new BitSet(32);
        }

        //Memory[0] = BitUtility.IntToBin(0b00011000000000000000000000000111); jump 7
        //Memory[0] = BitUtility.IntToBin(0b00000000001000100001000000000000);
        //Memory[7] = BitUtility.IntToBin(0b00010100001000101111111111011111);
    }

    public void reinitInstructionMemory() {
        for(int i = 0; i < 64; i++) {
            Memory[i].clear();
        }
    }

    public BitSet getInstruction(Integer index) {
        return Memory[index];
    }

    public BitSet[] getInstructions() {
        return Memory;
    }

    public void setInstruction(int index, BitSet instruction) {
        Memory[index].clear();
        for(int j = 0; j < 32; j++) {
            Memory[index].set(j, instruction.get(j));
        }
    }

    public void printInstructionMemory() {
        System.out.println("INSTRUCTION MEMORY: ");
        for(int i = 0; i < 10; i++) {
            System.out.println(Memory[i]);
        }
    }
}
