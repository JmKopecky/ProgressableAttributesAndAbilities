package dev.prognitio.pa3.entity;

import dev.prognitio.pa3.pa3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegister {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, pa3.MODID);

    public static final RegistryObject<EntityType<IncendiaryLanceProjectile>> INCENDIARY_LANCE = ENTITY_TYPES.register(
            "incendiary_lance", () -> EntityType.Builder.of((EntityType.EntityFactory<IncendiaryLanceProjectile>) IncendiaryLanceProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f).build("incendiary_lance"));

    public static final RegistryObject<EntityType<ChainLightningProjectile>> CHAIN_LIGHTNING = ENTITY_TYPES.register(
            "chain_lightning", () -> EntityType.Builder.of((EntityType.EntityFactory<ChainLightningProjectile>) ChainLightningProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("chain_lightning"));
}
