package me.jangroen.circuitcommander.logiccircuit.layout;

import me.jangroen.circuitcommander.circuit.operation.CircuitOperation;
import me.jangroen.circuitcommander.circuit.part.*;
import me.jangroen.circuitcommander.circuit.part.operation.CircuitPartOperation;
import me.jangroen.circuitcommander.circuit.variable.Variable;
import me.jangroen.circuitcommander.logiccircuit.LogicalCircuit;
import me.jangroen.circuitcommander.logiccircuit.entities.*;
import me.jangroen.circuitcommander.logiccircuit.parameter.GateType;
import me.jangroen.circuitcommander.logiccircuit.parameter.PinSide;

import java.util.ArrayList;
import java.util.List;

class LogicalLayoutPart {
    // Part contents
    private int inversions;
    private Variable variable;
    private CircuitOperation operation;
    private LogicalLayoutPart firstChild;
    private LogicalLayoutPart secondChild;
    private boolean value;

    // Part attributes
    private int xSize;
    private int ySize;
    private int yCenter;

    // Contains lamp
    private boolean containsLamp;

    LogicalLayoutPart(CircuitPart circuitPart) {
        this.inversions = 0;
        this.xSize = 0;
        this.ySize = 0;
        this.yCenter = 0;
        getByCircuitPart(circuitPart);
    }

    private void getByCircuitPart(CircuitPart circuitPart) {
        if(circuitPart instanceof CircuitPartInvert) {
            inversions++;
            getByCircuitPart(((CircuitPartInvert) circuitPart).getInnerPart());
            xSize += 4;
            if(ySize < 5) ySize = 5;
            if(yCenter < 2) yCenter = 2;
        } else if(circuitPart instanceof CircuitPartValue) {
            value = circuitPart.calculate();
            xSize += 2;
            if(ySize < 3) ySize = 3;
            if(yCenter < 1) yCenter = 1;
        } else if(circuitPart instanceof CircuitPartVariable) {
            variable = ((CircuitPartVariable) circuitPart).getVariable();
            if(ySize < 1) ySize = 1;
        } else if(circuitPart instanceof CircuitPartOperation) {
            operation = ((CircuitPartOperation) circuitPart).getOperation();
            firstChild = new LogicalLayoutPart(((CircuitPartOperation) circuitPart).getFirstChild());
            secondChild = new LogicalLayoutPart(((CircuitPartOperation) circuitPart).getSecondChild());
            xSize += 5 + (firstChild.xSize < secondChild.xSize ? secondChild.xSize : firstChild.xSize);
            int y1 = firstChild.ySize < 3 ? 3 : firstChild.ySize;
            int y2 = secondChild.ySize < 2 ? 2 : secondChild.ySize;
            ySize = y1 + y2;
            if(yCenter < y1 - 1) yCenter = y1 - 1;
        } else {
            throw new IllegalArgumentException("Invalid CircuitPart type found: " + circuitPart.getClass().getSimpleName() + " (" + circuitPart.toCircuitString() + ")");
        }
    }

    List<LogicalEntity> toEntities(LogicalCircuit logicalCircuit, LogicalLayout logicalLayout, int xPos, int yPos) {
        List<LogicalEntity> entities = new ArrayList<>();

        int x = xPos + xSize;
        int cableHeight = yPos + yCenter;

        if(containsLamp) {
            entities.add(new LogicalWire(logicalCircuit, x, cableHeight, x + 1, cableHeight));
            entities.add(new LogicalLamp(logicalCircuit, x + 1, cableHeight - 1));
        }

        while(inversions > 0) {
            x -= 4;
            entities.add(new LogicalWire(logicalCircuit, x, cableHeight, x + 1, cableHeight));
            entities.add(new LogicalGate(logicalCircuit, x + 1, cableHeight - 2, GateType.NOT));
            inversions--;
        }

        if(operation != null) {
            GateType gateType = GateType.byCircuitOpertation(operation);
            if(gateType == null) throw new IllegalArgumentException("CircuitOperation has no GateType counterpart: " + operation.name());
            x -= 5;
            entities.add(new LogicalGate(logicalCircuit, x + 2, cableHeight - 2, gateType));

            int y1 = cableHeight - firstChild.ySize + 1 + firstChild.yCenter;
            if(firstChild.ySize - firstChild.yCenter < 2) y1--;
            entities.add(new LogicalWire(logicalCircuit, x + 1, cableHeight + 1, x + 2, cableHeight + 1));
            entities.add(new LogicalWire(logicalCircuit, x + 1, y1, x + 1, cableHeight - 1));
            entities.add(new LogicalWire(logicalCircuit, x, y1, x + 1, y1));
            entities.addAll(firstChild.toEntities(logicalCircuit, logicalLayout, x - firstChild.xSize, y1 - firstChild.yCenter));

            int y2 = cableHeight + 1 + secondChild.yCenter;
            entities.add(new LogicalWire(logicalCircuit, x + 1, cableHeight - 1, x + 2, cableHeight - 1));
            entities.add(new LogicalWire(logicalCircuit, x + 1, y2, x + 1, cableHeight + 1));
            entities.add(new LogicalWire(logicalCircuit, x, y2, x + 1, y2));
            entities.addAll(secondChild.toEntities(logicalCircuit, logicalLayout, x - secondChild.xSize, cableHeight + 1));
        } else if(variable != null) {
            logicalLayout.addVariableConnection(variable, x, cableHeight);
        } else {
            entities.add(new LogicalConstant(logicalCircuit, x - 2, cableHeight - 1, value, PinSide.RIGHT));
        }

        return entities;
    }

    void setContainsLamp() {
        this.containsLamp = true;
    }
}
