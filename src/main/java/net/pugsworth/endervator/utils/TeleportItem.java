package net.pugsworth.endervator.utils;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TeleportItem {
    private World world;
    private UUID uuid;
    private Vec3d targetPos;

    public TeleportItem(World world, Entity entity, Vec3d toPos) {
        this.world = world;
        this.uuid = entity.getUuid();
        this.targetPos = toPos;
    }

    public TeleportItem(World world, UUID uuid, Vec3d toPos)
    {
        this.world = world;
        this.uuid = uuid;
        this.targetPos = toPos;
    }

    public World getWorld() {
        return this.world;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Entity getEntity() {
        return world.getPlayerByUuid(this.uuid);
    }

    public Vec3d getTargetPosition() {
        return this.targetPos;
    }
}
