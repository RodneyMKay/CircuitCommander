package me.jangroen.circuitcommander.gui;

import me.jangroen.circuitcommander.circuit.Circuit;
import me.jangroen.circuitcommander.circuit.OldCircuitParser;
import me.jangroen.circuitcommander.circuit.parse.ParseException;
import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.layout.LogicalLayout;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class MenuConvert extends JPanel {
    private static File lastOpened;
    private JTextField circuit;
    private Circuit effectiveCircuit;
    private JFileChooser fileChooser;

    MenuConvert() {
        openCircuitInput();
    }

    private void openCircuitInput() {
        effectiveCircuit = null;
        removeAll();
        setLayout(new GridLayout(5, 1));

        add(new JPanel());

        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelPanel.add(new JLabel("Schaltung zum Konvertieren:"));
        add(labelPanel);

        JPanel circuitPanel = new JPanel(new GridBagLayout());
        circuit = new JTextField();
        circuit.addActionListener(e -> convert());
        circuit.setPreferredSize(new Dimension(450, 20));
        circuitPanel.add(circuit);
        add(circuitPanel);

        JPanel runPanel = new JPanel(new GridBagLayout());
        JButton startButton = new JButton("Konvertieren!");
        startButton.addActionListener(e -> convert());
        runPanel.add(startButton);
        add(runPanel);

        add(new JPanel());

        circuit.requestFocus();
    }

    private void convert() {
        try {
            effectiveCircuit = OldCircuitParser.parse(circuit.getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Einlesen der Schaltung: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        if(effectiveCircuit != null) {
            openFileChooser();
        }
    }

    private void openFileChooser() {
        removeAll();
        setLayout(new GridBagLayout());
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(MenuConvert.lastOpened);
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".CircuitProject");
            }
            @Override
            public String getDescription() {
                return "CircuitProject Dateien";
            }
        });
        fileChooser.addActionListener(e -> {
            if(e.getActionCommand().equals("CancelSelection")) {
                openCircuitInput();
            } else if(e.getActionCommand().equals("ApproveSelection")) {
                runExport();
            }
        });
        add(fileChooser);
        updateUI();
    }

    private void runExport() {
        try {
            effectiveCircuit = OldCircuitParser.parse(circuit.getText());
            File file = fileChooser.getSelectedFile();
            MenuConvert.lastOpened = file;
            if(file.exists()) {
                int confirm = JOptionPane.showConfirmDialog(null, "Möchten Sie die Datei überschreiben?", "Achtung", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(confirm == JOptionPane.YES_OPTION) {
                    if(!file.delete()) throw new IOException("File cannot be deleted!");
                } else {
                    return;
                }
            }
            if(!file.createNewFile()) throw new IOException("File cannot be created!");

            LogicalLayout logicalLayout = new LogicalLayout(effectiveCircuit);
            LogicalCircuit logicalCircuit = logicalLayout.toLogicalCircuit();
            try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                logicalCircuit.writeTo(fileOutputStream);
            }

            Desktop.getDesktop().open(file);

            openCircuitInput();
//            JOptionPane.showMessageDialog(null, "Schaltung exportiert!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            updateUI();
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(null, "Error (" + e.getClass().getSimpleName() + ") : " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
