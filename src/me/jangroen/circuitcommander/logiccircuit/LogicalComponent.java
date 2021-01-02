package me.jangroen.circuitcommander.logiccircuit;

import java.util.UUID;

public abstract class LogicalComponent {
    protected UUID uuid;

    public LogicalComponent(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
