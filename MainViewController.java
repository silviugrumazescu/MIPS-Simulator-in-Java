import hardware.MIPS;
import hardware.utility.BitUtility;
import hardware.utility.InstructionParser;
import ui.MainView;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.BitSet;

public class MainViewController {

    private MIPS mips;
    protected MainView mainView;

    public MainViewController() {
        mainView = new MainView(new InitializeSimulationButtonActionListener(),
                new SetupButtonActionListener(),
                new StepButtonActionListener(),
                new SeeMemoryButtonActionListener(),
                new BackButtonActionListener());

        mainView.editMemoryTableModel.addTableModelListener(new EditMemoryTableModelListener());
        mainView.editRegistersTableModel.addTableModelListener(new EditRegisterTableModelListener());

        mips = new MIPS(mainView);

        mainView.updateInitializationView(mips.getMemoryData(), mips.getRegisterMemoryData());
    }

    private class InitializeSimulationButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mips.reinit();

            String[] instructions = mainView.instructionsView.instructionTextArea.getText().split("\\r?\\n");

            for(int i = 0; i < instructions.length; i++) {
                //BitSet newInstruction = BitUtility.StringToBin(instructions[i]);
                BitSet newInstruction = InstructionParser.parse(instructions[i]);
                mips.setInstruction(i, newInstruction);
            }

            mips.compute();

            updateSimulationView();

            CardLayout cardLayout = mainView.cardLayout;
            cardLayout.show(mainView.mainPanel, "simulationView");
        }
    }

    public void updateSimulationView() {
        mainView.updateRegisterTable("registerIFID", mips.getRegisterData("registerIFID"));
        mainView.updateRegisterTable("registerIDEX", mips.getRegisterData("registerIDEX"));
        mainView.updateRegisterTable("registerEXMEM", mips.getRegisterData("registerEXMEM"));
        mainView.updateRegisterTable("registerMEMWB", mips.getRegisterData("registerMEMWB"));

        mainView.updateMemoryTable(mips.getMemoryData());
        mainView.updateRegisterMemoryTable(mips.getRegisterMemoryData());

        mainView.updateInstructionsTable(mips.getInstructionsData());

        mainView.updateProgramCounter(mips.getProgramCounter());
    }

    private class SetupButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.updateInitializationView(mips.getMemoryData(), mips.getRegisterMemoryData());
            mainView.cardLayout.show(mainView.mainPanel, "instructionsView");
        }
    }
    private class StepButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mips.step();
            updateSimulationView();
        }
    }
    private class SeeMemoryButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.cardLayout.show(mainView.mainPanel, "memoryView");
        }
    }
    private class BackButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.cardLayout.show(mainView.mainPanel, "simulationView");
        }
    }

    public class EditMemoryTableModelListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            if(e.getType() == TableModelEvent.UPDATE) {
                for(int i = 0; i < mainView.editMemoryTableModel.getRowCount(); i++) {
                    mips.setMemory(i, BitUtility.IntToBin(Integer.parseInt((String)mainView.editMemoryTableModel.getValueAt(i, 1))));
                }
            }
        }
    }

    public class EditRegisterTableModelListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            if(e.getType() == TableModelEvent.UPDATE) {
                for(int i = 0; i < mainView.editRegistersTableModel.getRowCount(); i++) {
                    mips.setRegister(i, BitUtility.IntToBin(Integer.parseInt((String)mainView.editRegistersTableModel.getValueAt(i, 1))));
                }
            }

        }
    }
}
