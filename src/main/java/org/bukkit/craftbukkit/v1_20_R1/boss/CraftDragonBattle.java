package org.bukkit.craftbukkit.v1_20_R1.boss;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.dimension.end.DragonRespawnAnimation;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftLocation;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;

public class CraftDragonBattle implements DragonBattle {

    private final EndDragonFight handle;

    public CraftDragonBattle(EndDragonFight handle) {
        this.handle = handle;
    }

    @Override
    public EnderDragon getEnderDragon() {
        Entity entity = handle.level.getEntity(handle.dragonUUID);
        return (entity != null) ? (EnderDragon) entity.getBukkitEntity() : null;
    }

    @Override
    public BossBar getBossBar() {
        return new CraftBossBar(handle.dragonEvent);
    }

    @Override
    public Location getEndPortalLocation() {
        if (handle.portalLocation == null) {
            return null;
        }

        return CraftLocation.toBukkit(this.handle.portalLocation, this.handle.level.getWorld());
    }

    @Override
    public boolean generateEndPortal(boolean withPortals) {
        if (handle.portalLocation != null || handle.findExitPortal() != null) {
            return false;
        }

        this.handle.spawnExitPortal(withPortals);
        return true;
    }

    @Override
    public boolean hasBeenPreviouslyKilled() {
        return handle.hasPreviouslyKilledDragon();
    }

    @Override
    public void initiateRespawn() {
        this.handle.tryRespawn();
    }

    @Override
    public boolean initiateRespawn(Collection<EnderCrystal> list) {
        if (hasBeenPreviouslyKilled() && getRespawnPhase() == RespawnPhase.NONE) {
            // Copy from EndDragonFight#tryRespawn for generate exit portal if not exists
            if (this.handle.portalLocation == null) {
                BlockPattern.BlockPatternMatch shapedetector_shapedetectorcollection = this.handle.findExitPortal();
                if (shapedetector_shapedetectorcollection == null) {
                    this.handle.spawnExitPortal(true);
                }
            }

            list = (list != null) ? new ArrayList<>(list) : Collections.emptyList();
            list.removeIf(enderCrystal -> {
                if (enderCrystal == null) {
                    return true;
                }

                World world = enderCrystal.getWorld();
                return !((CraftWorld) world).getHandle().equals(handle.level);
            });

            this.handle.respawnDragon(list.stream().map(enderCrystal -> ((CraftEnderCrystal) enderCrystal).getHandle()).collect(Collectors.toList()));
            return this.handle.respawnDragon$returnValue;
        }
        return false;
    }

    @Override
    public RespawnPhase getRespawnPhase() {
        return toBukkitRespawnPhase(handle.respawnStage);
    }

    @Override
    public boolean setRespawnPhase(RespawnPhase phase) {
        Preconditions.checkArgument(phase != null && phase != RespawnPhase.NONE, "Invalid respawn phase provided: %s", phase);

        if (handle.respawnStage == null) {
            return false;
        }

        this.handle.setRespawnStage(toNMSRespawnPhase(phase));
        return true;
    }

    @Override
    public void resetCrystals() {
        this.handle.resetSpikeCrystals();
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftDragonBattle && ((CraftDragonBattle) obj).handle == this.handle;
    }

    private RespawnPhase toBukkitRespawnPhase(DragonRespawnAnimation phase) {
        return (phase != null) ? RespawnPhase.values()[phase.ordinal()] : RespawnPhase.NONE;
    }

    private DragonRespawnAnimation toNMSRespawnPhase(RespawnPhase phase) {
        return (phase != RespawnPhase.NONE) ? DragonRespawnAnimation.values()[phase.ordinal()] : null;
    }
}
