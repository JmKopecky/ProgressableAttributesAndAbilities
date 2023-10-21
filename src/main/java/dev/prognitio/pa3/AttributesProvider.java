package dev.prognitio.pa3;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AttributesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<AttributesCompatabililty> ATTRIBUTES = CapabilityManager.get(new CapabilityToken<>() {});

    private AttributesCompatabililty attributeCapability = null;

    private final LazyOptional<AttributesCompatabililty> optional = LazyOptional.of(this::createAttributes);

    private AttributesCompatabililty createAttributes() {
        if (this.attributeCapability == null) {
            this.attributeCapability = new AttributesCompatabililty();
        }
        return this.attributeCapability;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ATTRIBUTES) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createAttributes().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createAttributes().loadNBTData(nbt);
    }
}
