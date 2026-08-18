package net.ron.zam.api.television;

import net.minecraft.util.StringRepresentable;

public enum PowerState implements StringRepresentable {
    OFF("off"),
    DIRECT("direct"),
    INDIRECT("indirect");

    private final String name;

    PowerState(String name) { this.name = name; }

    @Override
    public String getSerializedName() { return name; }

    public boolean isOn() {
        return this != OFF;
    }

    public static PowerState fromSignal(boolean directlyPowered, boolean anyPower) {
        if (directlyPowered) return DIRECT;
        if (anyPower) return INDIRECT;
        return OFF;
    }
}
