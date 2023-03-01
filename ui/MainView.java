package ui;

import hardware.utility.BitUtility;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class MainView {

    public JFrame mainFrame;
    public JPanel mainPanel;
    public CardLayout cardLayout;
    public InstructionsView instructionsView;
    public MemoryView memoryView;
    public SimulationView simulationView;

    DefaultTableModel registerIFIDtableModel, registerIDEXtableModel, registerEXMEMtableModel, registerMEMWBtableModel;
    DefaultTableModel memoryTableModel, registerMemoryTableModel, instructionsTableModel;
    public DefaultTableModel editMemoryTableModel, editRegistersTableModel;

    public Integer highlightedInstructionIndex;

    public MainView(ActionListener initializeSimulationButtonActionListener,
                    ActionListener setupButtonActionListener,
                    ActionListener stepButtonActionListener,
                    ActionListener seeMemoryButtonActionListener,
                    ActionListener backButtonActionListener) {
        mainFrame = new JFrame("Mips Pipeline Simulator");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        instructionsView = new InstructionsView();
        memoryView = new MemoryView();
        simulationView = new SimulationView();

        initializeTables();

        // adding action listeners
        instructionsView.initializeSimulationButton.addActionListener(initializeSimulationButtonActionListener);

        simulationView.setupButton.addActionListener(setupButtonActionListener);
        simulationView.stepButton.addActionListener(stepButtonActionListener);
        simulationView.seeMemoryButton.addActionListener(seeMemoryButtonActionListener);

        memoryView.backToSimulationButton.addActionListener(backButtonActionListener);

        mainPanel.add(instructionsView.mainPanel, "instructionsView");
        mainPanel.add(simulationView.mainPanel, "simulationView");
        mainPanel.add(memoryView.mainPanel, "memoryView");

        cardLayout.show(mainPanel, "instructionsView");

        mainFrame.add(mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void initializeTables() {
        registerIFIDtableModel = new DefaultTableModel();
        registerIFIDtableModel.setColumnIdentifiers(new String[]{"Signal", "Value"});
        simulationView.registerIFIDtable.setModel(registerIFIDtableModel);

        registerIDEXtableModel = new DefaultTableModel();
        registerIDEXtableModel.setColumnIdentifiers(new String[]{"Signal", "Value"});
        simulationView.registerIDEXtable.setModel(registerIDEXtableModel);

        registerEXMEMtableModel = new DefaultTableModel();
        registerEXMEMtableModel.setColumnIdentifiers(new String[]{"Signal", "Value"});
        simulationView.registerEXMEMtable.setModel(registerEXMEMtableModel);

        registerMEMWBtableModel = new DefaultTableModel();
        registerMEMWBtableModel.setColumnIdentifiers(new String[]{"Signal", "Value"});
        simulationView.registerMEMWBtable.setModel(registerMEMWBtableModel);

        memoryTableModel = new DefaultTableModel();
        memoryTableModel.setColumnIdentifiers(new String[]{"Address", "Data"});
        memoryView.memoryDataTable.setModel(memoryTableModel);

        registerMemoryTableModel = new DefaultTableModel();
        registerMemoryTableModel.setColumnIdentifiers(new String[]{"Address", "Data"});
        memoryView.registerDataTable.setModel(registerMemoryTableModel);

        instructionsTableModel = new DefaultTableModel();
        instructionsTableModel.setColumnIdentifiers(new String[]{"Index", "Instruction"});
        simulationView.instructionsTable.setModel(instructionsTableModel);
        simulationView.instructionsTable.setDefaultRenderer(Object.class, new HighlightInstructionCellRenderer());

        editMemoryTableModel = new DefaultTableModel();
        editMemoryTableModel.setColumnIdentifiers(new String[]{"Index", "Value"});
        instructionsView.memoryTable.setModel(editMemoryTableModel);

        editRegistersTableModel= new DefaultTableModel();
        editRegistersTableModel.setColumnIdentifiers(new String[]{"Index", "Value"});
        instructionsView.registersTable.setModel(editRegistersTableModel);

    }

    public void updateRegisterTable(String register, HashMap<String, BitSet> values) {
        DefaultTableModel toBeUpdated;

        switch(register) {
            case "registerIFID":
                toBeUpdated = registerIFIDtableModel;
                break;
            case "registerIDEX":
                toBeUpdated = registerIDEXtableModel;
                break;
            case "registerEXMEM":
                toBeUpdated = registerEXMEMtableModel;
                break;
            case "registerMEMWB":
                toBeUpdated = registerMEMWBtableModel;
                break;
            default:
                toBeUpdated = new DefaultTableModel();
        }

        toBeUpdated.setRowCount(0);

        for(HashMap.Entry<String, BitSet> set : values.entrySet()) {
            toBeUpdated.addRow(new String[]{set.getKey(), String.format("0x%08X", BitUtility.BinToIntSigned(set.getValue()))});
        }

        toBeUpdated.fireTableDataChanged();
    }

    public void updateMemoryTable(BitSet[] values) {
        memoryTableModel.setRowCount(0);
        for(int i = 0; i < values.length; i++) {
            memoryTableModel.addRow(new String[]{Integer.toString(i), String.format("0x%08X", BitUtility.BinToIntSigned(values[i]))});
        }
        memoryTableModel.fireTableDataChanged();
    }

    public void updateRegisterMemoryTable(BitSet[] values) {
        registerMemoryTableModel.setRowCount(0);
        for(int i = 0; i < values.length; i++) {
            registerMemoryTableModel.addRow(new String[]{Integer.toString(i), String.format("0x%08X", BitUtility.BinToIntSigned(values[i]))});
        }
        registerMemoryTableModel.fireTableDataChanged();
    }

    public void updateInstructionsTable(BitSet[] values) {
        instructionsTableModel.setRowCount(0);
        for(int i = 0; i < values.length; i++) {
            instructionsTableModel.addRow(new String[]{Integer.toString(i), String.format("0x%08X", BitUtility.BinToIntSigned(values[i]))});
        }
        instructionsTableModel.fireTableDataChanged();
    }

    public void updateProgramCounter(Integer programCounter) {
        simulationView.stepLabel.setText(Integer.toString(programCounter));
        this.highlightedInstructionIndex = programCounter;
        simulationView.instructionsTable.repaint();
    }

    public void updateInitializationView(BitSet[] memory, BitSet[] registers) {
        editMemoryTableModel.setRowCount(0);
        for(int i = 0; i < memory.length; i++) {
            editMemoryTableModel.addRow(new String[]{Integer.toString(i),  Integer.toString(BitUtility.BinToIntSigned(memory[i]))});
        }
        editMemoryTableModel.fireTableDataChanged();

        editRegistersTableModel.setRowCount(0);
        for(int i = 0; i < registers.length; i++) {
            editRegistersTableModel.addRow(new String[]{Integer.toString(i), Integer.toString(BitUtility.BinToIntSigned(registers[i]))});
        }
        editRegistersTableModel.fireTableDataChanged();
    }

    public class HighlightInstructionCellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            if(row == highlightedInstructionIndex) {
                cell.setBackground(Color.ORANGE);
            }
            else {
                cell.setBackground(Color.WHITE);
            }
            return cell;
        }
    }



}
