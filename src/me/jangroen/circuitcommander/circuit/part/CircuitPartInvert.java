package me.jangroen.circuitcommander.circuit.part;

import me.jangroen.circuitcommander.circuit.parse.CircuitLanguage;

public class CircuitPartInvert implements CircuitPart {
    private CircuitPart innerPart;

    public CircuitPartInvert(CircuitPart innerPart) {
        this.innerPart = innerPart;
    }

    @Override
    public boolean calculate() {
        return !innerPart.calculate();
    }

    @Override
    public String toString() {
        return "NOT(" + innerPart.toString() + ")";
    }

    @Override
    public String toCircuitString() {
        return CircuitLanguage.NOT_OPERATOR + innerPart.toCircuitString();
    }

    @Override
    public boolean equals(CircuitPart object) {
        return object instanceof CircuitPartInvert && innerPart.equals(((CircuitPartInvert) object).innerPart);
    }

    public CircuitPart getInnerPart() {
        return innerPart;
    }
}
