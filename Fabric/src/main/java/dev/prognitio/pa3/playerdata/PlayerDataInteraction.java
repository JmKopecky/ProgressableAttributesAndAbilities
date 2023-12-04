package dev.prognitio.pa3.playerdata;

import dev.prognitio.pa3.AttributeType;
import dev.prognitio.pa3.FabricModMessages;
import dev.prognitio.pa3.mixin.PlayerDataStorageMixin;
import dev.prognitio.pa3.util.IPlayerAttrStorage;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerDataInteraction {

    public static void syncData(Player player, IPlayerAttrStorage data) {
        FriendlyByteBuf buf = PacketByteBufs.create().writeNbt(data.getPersistentData());
        ServerPlayNetworking.send((ServerPlayer) player, FabricModMessages.SYNC_DATA, buf);
    }

    public static boolean attemptLevelUpAttribute(Player player, String attr) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        AttributeType target;
        switch (attr) {
            case "fitness" -> {
                target = PlayerDataGetters.getFitness(data);
            }
            case "resilience" -> {
                target = PlayerDataGetters.getResilience(data);
            }
            case "nimbleness" -> {
                target = PlayerDataGetters.getNimbleness(data);
            }
            case "combat" -> {
                target = PlayerDataGetters.getCombat(data);
            }
            case "strategy" -> {
                target = PlayerDataGetters.getStrategy(data);
            }
            default -> {
                return false;
            }
        }
        int result = target.attemptLevelUp(PlayerDataGetters.getAvailablePoints(data));
        syncData(player, data);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public static void setAvailablePoints(Player player, int amount) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        CompoundTag tag = data.getPersistentData();
        tag.putInt("availablepoints", amount);
        syncData(player, data);
    }

    public static void setExperience(Player player, int amount) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        data.getPersistentData().putInt("experience", amount);
        syncData(player, data);
    }

    public static void setLevel(Player player, int level) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        data.getPersistentData().putInt("level", level);
        syncData(player, data);
    }

    public static void setAbilityCooldown(Player player, int amount) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        data.getPersistentData().putInt("cooldown", amount);
        syncData(player, data);
    }

    public static void setMaxCooldown(Player player, int amount) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        data.getPersistentData().putInt("maxcooldown", amount);
        syncData(player, data);
    }

    public static void triggerPassiveProc(Player player, String type) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        data.getPersistentData().putInt("passive" + type, 40);
        syncData(player, data);
    }

    public static void decreasePassiveTimers(Player player) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        if (PlayerDataGetters.getPassiveDodge(data) > 0) {
            data.getPersistentData().putInt("passivedodge", PlayerDataGetters.getPassiveDodge(data) - 1);
        }
        if (PlayerDataGetters.getPassiveParry(data) > 0) {
            data.getPersistentData().putInt("passiveparry", PlayerDataGetters.getPassiveParry(data) - 1);
        }
        if (PlayerDataGetters.getPassiveDoubleStrike(data) > 0) {
            data.getPersistentData().putInt("passivedoublestrike", PlayerDataGetters.getPassiveDoubleStrike(data) - 1);
        }
        syncData(player, data);
    }

    public static void attemptUnlockElite(Player player, String target) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        int availablePoints = PlayerDataGetters.getAvailablePoints(data);
        int result = PlayerDataGetters.getAbilityFromString(data, target).attemptUnlockElite(availablePoints);
        if (availablePoints - result <= availablePoints) {
            setAvailablePoints(player, availablePoints - result);
        }
        syncData(player, data);
    }

    public static int calculateLevelUpRequirement(IPlayerAttrStorage player) {
        int level = PlayerDataGetters.getLevel(player);
        //for each level, make it 2% harder to get another level
        int initialXPRequirement = 1000; //what should the base xp requirement be to go from level 0 to level 1
        float perLevelScaling = 1.02f; //how much the exp requirement should increase each level.
        return (int) (initialXPRequirement * (Math.pow(perLevelScaling, level)));
    }

    public static void setPrimaryAbil(Player player, String ability) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        data.getPersistentData().putString("primaryabil", ability);
        syncData(player, data);
    }

    public static void setSecondaryAbil(Player player, String ability) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        data.getPersistentData().putString("secondaryabil", ability);
        syncData(player, data);
    }

    public static void fireAbil(Player player, String type) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        if (PlayerDataGetters.getAbilityCooldown(data) <= 0) {
            String abil = (type.equals("primary")) ? PlayerDataGetters.getPrimaryAbility(data) : PlayerDataGetters.getSecondaryAbility(data);
            PlayerDataGetters.getAbilityFromString(data, abil).runAbility(player);
            data.getPersistentData().putString("lastusedabil", abil);
        }
        syncData(player, data);
    }

    public void addXP(int experience, Player player) {
        IPlayerAttrStorage data = ((IPlayerAttrStorage) player);
        //if experience is not enough to level up, add to experience. else, level up, zero out experience, add the amount extra after leveling up to experience
        int levelUpDistance = calculateLevelUpRequirement(data) - PlayerDataGetters.getXp(data);
        if (levelUpDistance > experience) {
            //add xp, don't level up
            setExperience(player, PlayerDataGetters.getXp(data) + experience);
        } else {
            setLevel(player, PlayerDataGetters.getLevel(data) + 1);
            setAvailablePoints(player, PlayerDataGetters.getAvailablePoints(data) + 1);
            setExperience(player, experience - levelUpDistance);
            while (PlayerDataGetters.getXp(data) > calculateLevelUpRequirement(data)) {
                setExperience(player, PlayerDataGetters.getXp(data) - calculateLevelUpRequirement(data));
                setLevel(player, PlayerDataGetters.getLevel(data) + 1);
                setAvailablePoints(player, PlayerDataGetters.getAvailablePoints(data) + 1);
            }
        }
        syncData(player, data);
    }
}
