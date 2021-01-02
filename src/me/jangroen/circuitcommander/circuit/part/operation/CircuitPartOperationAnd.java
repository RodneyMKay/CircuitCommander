package me.jangroen.circuitcommander.circuit.part.operation;

import me.jangroen.circuitcommander.circuit.part.CircuitPart;

public class CircuitPartOperationAnd extends CircuitPartOperation {
    public CircuitPartOperationAnd(CircuitPart[] parts) {
        super(parts);
    }

    @Override
    public boolean calculate() {
        for (CircuitPart part : getParts()) {
            if (!part.calculate()) return false;
        }
        return true;
    }

    @Override
    public CircuitOperation getCircuitOperation() {
        return CircuitOperation.AND;
    }

    @Override
    public boolean equals(CircuitPart object) {
        return false;
    }
}
