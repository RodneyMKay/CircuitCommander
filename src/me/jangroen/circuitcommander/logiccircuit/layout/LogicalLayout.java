package me.jangroen.circuitcommander.logiccircuit.layout;

import me.jangroen.circuitcommander.circuit.Circuit;
import me.jangroen.circuitcommander.circuit.variable.Variable;
import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.entities.LogicalButton;
import me.jangroen.circuitcommander.logiccircuit.entities.LogicalWire;
import me.jangroen.circuitcommander.logiccircuit.parameter.PinSide;

import java.util.Map;
import java.util.TreeMap;

public class LogicalLayout {
    private Map<Variable, Map<Integer, Integer>> variables;
    private LogicalLayoutPart layoutPart;
    private Circuit circuit;

    public LogicalLayout(Circuit circuit) {
        this.variables = new TreeMap<>();
        this.layoutPart = new LogicalLayoutPart(circuit.getMainPart());
        this.circuit = circuit;
    }

    public LogicalCircuit toLogicalCircuit() {
        LogicalCircuit logicalCircuit = new LogicalCircuit(circuit.toCircuitString());

        layoutPart.setContainsLamp();
        int xOffset = circuit.getVariableProvider().getVariables().size() * 3;
        logicalCircuit.addAllEntities(layoutPart.toEntities(logicalCircuit, this, xOffset, 4));

        int xPos = 1;
        for(Map.Entry<Variable, Map<Integer, Integer>> entry : variables.entrySet()) {
            logicalCircuit.addEntity(new LogicalButton(logicalCircuit, xPos, 1, entry.getKey().name(), PinSide.BOTTOM));
            int yPos = 3;
            xPos++;
            for(Map.Entry<Integer, Integer> positions : entry.getValue().entrySet()) {
                logicalCircuit.addEntity(new LogicalWire(logicalCircuit, xPos, yPos, xPos, positions.getKey()));
                yPos = positions.getKey();
                logicalCircuit.addEntity(new LogicalWire(logicalCircuit, xPos, yPos, positions.getValue(), yPos));
            }
            xPos += 2;
        }

        return logicalCircuit;
    }

    void addVariableConnection(Variable variable, int x, int y) {
        variables.computeIfAbsent(variable, k -> new TreeMap<>()).put(y, x);
    }
}
