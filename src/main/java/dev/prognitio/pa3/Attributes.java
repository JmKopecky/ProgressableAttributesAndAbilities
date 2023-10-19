package dev.prognitio.pa3;

import net.minecraft.nbt.CompoundTag;

public class Attributes {

    private float VARIABLE = 0f;

    public void copyFrom(Attributes source) {
        this.VARIABLE = source.VARIABLE;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("variable", this.VARIABLE);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.VARIABLE = nbt.getFloat("variable");
    }
}
