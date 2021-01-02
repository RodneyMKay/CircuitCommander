package me.jangroen.circuitcommander.logiccircuit.parameter;

public enum PinSide {
    TOP("Top"),
    BOTTOM("Bottom"),
    RIGHT("Right"),
    LEFT("Left");

    public String name;

    PinSide(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
