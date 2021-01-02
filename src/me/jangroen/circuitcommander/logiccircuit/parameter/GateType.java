package me.jangroen.circuitcommander.logiccircuit.parameter;

import me.jangroen.circuitcommander.circuit.operation.CircuitOperation;

public enum GateType {
    NOT("00000000-0000-0000-0000-000000020101"),
    OR("00000000-0000-0000-0000-000000030200"),
//    NOR("00000000-0000-0000-0000-000000030201"),
    AND("00000000-0000-0000-0000-000000040200");
//    NAND("00000000-0000-0000-0000-000000040201"),
//    XOR("00000000-0000-0000-0000-000000050200"),
//    XNOR("00000000-0000-0000-0000-000000050201");

    private String uuid;

    GateType(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public static GateType byCircuitOpertation(CircuitOperation circuitOperation) {
        switch (circuitOperation) {
            case AND:
                return AND;
            case OR:
                return OR;
        }
        return null;
    }
}
