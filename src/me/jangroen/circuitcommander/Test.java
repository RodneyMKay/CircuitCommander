package me.jangroen.circuitcommander;

import me.jangroen.circuitcommander.circuit.Circuit;
import me.jangroen.circuitcommander.circuit.OldCircuitParser;
import me.jangroen.circuitcommander.circuit.variable.VariableProvider;
import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.layout.LogicalLayout;

import java.io.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        Circuit circuit = OldCircuitParser.parse("(!A^B^C)v(A^!B^!C)v(A^!B^C)v(A^!B^C)v(A^B^C)");
        VariableProvider provider = circuit.getVariableProvider();

        do {
            System.out.println(provider.getValues().values().stream().map(v -> (v ? "1" : "0")).collect(Collectors.joining(";")) + ";" + (circuit.calculate() ? "1" : "0"));
        } while(circuit.getVariableProvider().countBinary());
    }

    private static void convert() throws IOException {
        Circuit circuit = OldCircuitParser.parse("(!A^B^C)v(A^!B^!C)v(A^!B^C)v(A^!B^C)v(A^B^C)");
        LogicalLayout logicalLayout = new LogicalLayout(circuit);
        LogicalCircuit logicalCircuit = logicalLayout.toLogicalCircuit();

        File file = new File("C:/Users/Jan/Desktop/test.CircuitProject");
        if(file.exists()) {
            if(!file.delete()) throw new IOException();
        }
        if(!file.createNewFile()) throw new IOException();
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        logicalCircuit.writeTo(fileOutputStream);
    }

    private static void compare() {
        // Create Scanner
        Scanner scanner = new Scanner(System.in);

        // Parse circuits
        System.out.println("Erste Schaltung:");

        System.out.println("Zweite Schaltung:");
        Circuit firstCircuit = OldCircuitParser.parse(scanner.nextLine());
        Circuit secondCircuit = OldCircuitParser.parse(scanner.nextLine());

        // Merge variable providers
        VariableProvider variableProvider = firstCircuit.getVariableProvider();
        secondCircuit.getVariableProvider().getVariables().forEach(variableProvider::registerVariable);
        secondCircuit.setVariableProvider(variableProvider);

        // Check if equal
        boolean success = true;
        do {
            if(firstCircuit.calculate() != secondCircuit.calculate()) success = false;
        } while(variableProvider.countBinary());

        // Output
        if(success) System.out.println("\nDie Schaltungen sind gleich!");
        else System.out.println("\nDie Schaltungen sind ungleich!");
    }
}
