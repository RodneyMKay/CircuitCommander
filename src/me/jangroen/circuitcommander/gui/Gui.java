package me.jangroen.circuitcommander.gui;

import javax.swing.*;

public class Gui extends JFrame {
    private JTabbedPane menuPane;

    private Gui() {
        super("CircuitCommander");
        setSize(640, 440);
        setResizable(false);
        menuPane = new JTabbedPane();
        add(menuPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addMenuItem(String title, JPanel item) {
        menuPane.addTab(title, item);
    }

    public static void openNewGui() {
        Gui gui = new Gui();

        MenuCompare menuCompare = new MenuCompare();
        gui.addMenuItem("Vergleichen", menuCompare);
        gui.addMenuItem("Zu LogicCircuit konvertieren", new MenuConvert());
        menuCompare.requestFocus();
    }
}
