package me.jangroen.circuitcommander.gui;

import me.jangroen.circuitcommander.circuit.Circuit;
import me.jangroen.circuitcommander.circuit.OldCircuitParser;
import me.jangroen.circuitcommander.circuit.parse.ParseException;
import me.jangroen.circuitcommander.circuit.variable.VariableProvider;

import javax.swing.*;
import java.awt.*;

class MenuCompare extends JPanel {
    private JTextField firstCircuit;
    private JTextField secondCircuit;
    private JLabel label;

    MenuCompare() {
        setLayout(new GridLayout(6,1));

        JPanel comparePanel = new JPanel(new GridBagLayout());
        comparePanel.add(new JLabel("Erste Schaltung:"));
        add(comparePanel);

        JPanel firstCircuitPanel = new JPanel(new GridBagLayout());
        firstCircuit = new JTextField();
        firstCircuit.requestFocus();
        firstCircuit.addActionListener(e -> compare());
        firstCircuit.setPreferredSize(new Dimension(450, 20));

        firstCircuitPanel.add(firstCircuit);
        add(firstCircuitPanel);

        JPanel toPanel = new JPanel(new GridBagLayout());
        toPanel.add(new JLabel("Zweite Schaltung:"));
        add(toPanel);

        JPanel secondCircuitPanel = new JPanel(new GridBagLayout());
        secondCircuit = new JTextField();
        secondCircuit.addActionListener(e -> compare());
        secondCircuit.setPreferredSize(new Dimension(450, 20));
        secondCircuitPanel.add(secondCircuit);
        add(secondCircuitPanel);

        JPanel lastPanel = new JPanel(new GridBagLayout());
        JButton button = new JButton("Vergleichen!");
        button.addActionListener(e -> compare());
        lastPanel.add(button);
        add(lastPanel);

        JPanel panel = new JPanel(new GridBagLayout());
        label = new JLabel();
        panel.add(label);
        add(panel);
    }

    public void requestFocus() {
        firstCircuit.requestFocus();
    }

    private void compare() {
        // Get first circuit
        Circuit firstCircuit;
        try {
            firstCircuit = OldCircuitParser.parse(this.firstCircuit.getText());
        } catch (ParseException e) {
            label.setText("Fehler beim Einlesen der ersten Schaltung: " + e.getMessage());
            return;
        }

        // Get second circuit
        Circuit secondCircuit;
        try {
            secondCircuit = OldCircuitParser.parse(this.secondCircuit.getText());
        } catch (ParseException e) {
            label.setText("Fehler beim Einlesen der zweiten Schaltung: " + e.getMessage());
            return;
        }

        // Merge variable providers
        VariableProvider variableProvider = firstCircuit.getVariableProvider();
        secondCircuit.getVariableProvider().getVariables().forEach(variableProvider::registerVariable);
        secondCircuit.setVariableProvider(variableProvider);

        // Check if equal
        boolean success = true;
        do {
            if(firstCircuit.calculate() != secondCircuit.calculate()) success = false;
        } while(variableProvider.countBinary());

        if(success) label.setText("Die Schaltungen sind gleich!");
        else label.setText("Die Schaltungen sind ungleich!");
    }
}
