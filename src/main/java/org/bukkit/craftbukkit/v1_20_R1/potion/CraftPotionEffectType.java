package org.bukkit.craftbukkit.v1_20_R1.potion;

import com.google.common.base.Preconditions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_20_R1.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;

public class CraftPotionEffectType extends PotionEffectType {
    private final MobEffect handle;

    public CraftPotionEffectType(MobEffect handle) {
        super(BuiltInRegistries.MOB_EFFECT.getId(handle) + 1, CraftNamespacedKey.fromMinecraft(BuiltInRegistries.MOB_EFFECT.getKey(handle)));
        this.handle = handle;
    }

    @Override
    public double getDurationModifier() {
        return 1.0D;
    }

    public MobEffect getHandle() {
        return handle;
    }

    @Override
    public String getName() {
        switch (getId()) {
        case 1:
            return "SPEED";
        case 2:
            return "SLOW";
        case 3:
            return "FAST_DIGGING";
        case 4:
            return "SLOW_DIGGING";
        case 5:
            return "INCREASE_DAMAGE";
        case 6:
            return "HEAL";
        case 7:
            return "HARM";
        case 8:
            return "JUMP";
        case 9:
            return "CONFUSION";
        case 10:
            return "REGENERATION";
        case 11:
            return "DAMAGE_RESISTANCE";
        case 12:
            return "FIRE_RESISTANCE";
        case 13:
            return "WATER_BREATHING";
        case 14:
            return "INVISIBILITY";
        case 15:
            return "BLINDNESS";
        case 16:
            return "NIGHT_VISION";
        case 17:
            return "HUNGER";
        case 18:
            return "WEAKNESS";
        case 19:
            return "POISON";
        case 20:
            return "WITHER";
        case 21:
            return "HEALTH_BOOST";
        case 22:
            return "ABSORPTION";
        case 23:
            return "SATURATION";
        case 24:
            return "GLOWING";
        case 25:
            return "LEVITATION";
        case 26:
            return "LUCK";
        case 27:
            return "UNLUCK";
        case 28:
            return "SLOW_FALLING";
        case 29:
            return "CONDUIT_POWER";
        case 30:
            return "DOLPHINS_GRACE";
        case 31:
            return "BAD_OMEN";
        case 32:
            return "HERO_OF_THE_VILLAGE";
        case 33:
            return "DARKNESS";
        default:
            return "UNKNOWN_EFFECT_TYPE_" + getId();
        }
    }

    @Override
    public boolean isInstant() {
        return handle.isInstantenous();
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(handle.getColor());
    }

    public static PotionEffectType minecraftToBukkit(MobEffect minecraft) {
        Preconditions.checkArgument(minecraft != null);

        Registry<MobEffect> registry = CraftRegistry.getMinecraftRegistry(Registries.MOB_EFFECT);
        PotionEffectType bukkit = PotionEffectType.getByKey(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static MobEffect bukkitToMinecraft(PotionEffectType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.MOB_EFFECT)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }
}
