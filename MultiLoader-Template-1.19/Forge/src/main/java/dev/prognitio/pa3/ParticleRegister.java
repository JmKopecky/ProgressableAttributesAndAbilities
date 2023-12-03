package dev.prognitio.pa3;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegister {

    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Constants.MOD_ID);

    public static final RegistryObject<SimpleParticleType> DEFENSIVE_SHIELD_PARTICLE = PARTICLE_TYPES.register(
            "deflective_shield_particle", () -> new SimpleParticleType(true));
}
