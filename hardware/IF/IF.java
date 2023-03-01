package hardware.IF;

import hardware.interfaces.AsyncComponent;
import hardware.interfaces.SyncComponent;
import hardware.registers.RegisterEXMEM;
import hardware.registers.RegisterIFID;
import hardware.utility.BitUtility;
import hardware.utility.ControlRepo;

import java.util.BitSet;

public class IF implements AsyncComponent, SyncComponent {

    private BitSet ControlSignals;
    private RegisterIFID registerIFID;
    private RegisterEXMEM registerEXMEM;
    private BitSet PCSrc;

    private Integer programCounter = 0;
    private InstructionMemory instructionMemory;

    public IF(RegisterIFID registerIFID, RegisterEXMEM registerEXMEM, BitSet ControlSignals, BitSet PCSrc) {
        this.registerIFID = registerIFID;
        this.registerEXMEM = registerEXMEM;
        this.ControlSignals = ControlSignals;
        this.PCSrc = PCSrc;

        instructionMemory = new InstructionMemory();
        programCounter = 0;
    }

    @Override
    public void compute() {

        registerIFID.store(RegisterIFID.PCp1, BitUtility.IntToBin(programCounter+1));
        registerIFID.store(RegisterIFID.INSTRUCTION, instructionMemory.getInstruction(programCounter));

    }

    @Override
    public void clock() {
        if (ControlSignals.get(ControlRepo.JUMP) == true) {
            System.out.println("shout jump");
            BitSet instructionImmediate = registerIFID.get(RegisterIFID.INSTRUCTION).get(0,26);
            programCounter = BitUtility.BinToIntSigned(instructionImmediate);
        }
        else if (PCSrc.get(0) == true) {
            BitSet branchAddress = registerEXMEM.get(RegisterEXMEM.BranchAddress);
            programCounter = BitUtility.BinToIntSigned(branchAddress);
        }
        else {
            programCounter++;
        }

        instructionMemory.printInstructionMemory();
    }

    public void reinitInstructionMemory() {
        instructionMemory.reinitInstructionMemory();
        programCounter = 0;
    }

    public void setInstruction(int index, BitSet instruction) {
        instructionMemory.setInstruction(index, instruction);
    }

    public BitSet[] getInstructions() {
        return instructionMemory.getInstructions();
    }

    public Integer getProgramCounter() {
        return programCounter;
    }
}
