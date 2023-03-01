package hardware;

import hardware.EX.EX;
import hardware.ID.ID;
import hardware.IF.IF;
import hardware.MEM.MEM;
import hardware.WB.WB;
import hardware.interfaces.AsyncComponent;
import hardware.interfaces.SyncComponent;
import hardware.registers.RegisterEXMEM;
import hardware.registers.RegisterIDEX;
import hardware.registers.RegisterIFID;
import hardware.registers.RegisterMEMWB;
import ui.MainView;

import java.util.BitSet;
import java.util.HashMap;

public class MIPS implements AsyncComponent, SyncComponent {

    private MainView mainView;

    private ControlUnit controlUnit;

    private IF instructionFetch;
    private ID instructionDecode;
    private EX executeOperation;
    private MEM memory;
    private WB writeBack;

    private RegisterIFID registerIFID;
    private RegisterIDEX registerIDEX;
    private RegisterEXMEM registerEXMEM;
    private RegisterMEMWB registerMEMWB;

    public MIPS(MainView mainView) {
        initialiseComponents(mainView);

        //this.compute();
    }

    private void initialiseComponents(MainView mainView) {
        registerIFID = new RegisterIFID(mainView);
        registerIDEX = new RegisterIDEX(mainView);
        registerEXMEM = new RegisterEXMEM(mainView);
        registerMEMWB = new RegisterMEMWB(mainView);

        controlUnit = new ControlUnit(registerIFID.get(RegisterIFID.INSTRUCTION));

        writeBack = new WB(registerMEMWB);
        memory = new MEM(registerEXMEM, registerMEMWB, mainView);
        executeOperation = new EX(registerIDEX, registerEXMEM);
        instructionDecode = new ID(registerIFID, registerMEMWB, registerIDEX, writeBack.writeData, controlUnit.ControlSignals);
        instructionFetch = new IF(registerIFID, registerEXMEM, controlUnit.ControlSignals, memory.PCSrc);
    }

    @Override
    public void compute() {
        controlUnit.compute();
        writeBack.compute();
        memory.compute();
        executeOperation.compute();
        instructionDecode.compute();
        instructionFetch.compute();
    }

    @Override
    public void clock() {
        instructionFetch.clock();
        instructionDecode.clock();
        memory.clock();

        registerIFID.clock();
        registerIDEX.clock();
        registerEXMEM.clock();
        registerMEMWB.clock();
    }

    public void step() {
        clock();
        compute();
    }

    public void printRegisters() {
        registerIFID.print();
        registerIDEX.print();
        registerEXMEM.print();
        registerMEMWB.print();
    }

    public void printRegFile() {
        instructionDecode.printRegFile();
    }

    public void printMemory() {
        memory.printMemory();
    }

    public BitSet[] getMemoryData() {
        return memory.getMemoryData();
    }

    public BitSet[] getRegisterMemoryData() {
        return instructionDecode.getRegisterMemoryData();
    }

    public BitSet[] getInstructionsData() { return instructionFetch.getInstructions(); }

    public HashMap<String, BitSet> getRegisterData(String registerName){
        switch(registerName) {
            case "registerIFID":
                return registerIFID.getRegisterData();
            case "registerIDEX":
                return registerIDEX.getRegisterData();
            case "registerEXMEM":
                return registerEXMEM.getRegisterData();
            case "registerMEMWB":
                return registerMEMWB.getRegisterData();
            default:
                return null;
        }
    }

    public void reinit() {
        instructionFetch.reinitInstructionMemory();
        registerIFID.reinitRegister();
        registerIDEX.reinitRegister();
        registerEXMEM.reinitRegister();
        registerMEMWB.reinitRegister();
    }

    public void setInstruction(int index, BitSet instruction) {
        instructionFetch.setInstruction(index, instruction);
    }

    public Integer getProgramCounter() {
        return instructionFetch.getProgramCounter();
    }

    public void setRegister(Integer index, BitSet value) {
        instructionDecode.setRegister(index, value);
    }

    public void setMemory(Integer index, BitSet value) {
        memory.setMemory(index, value);
    }
}
