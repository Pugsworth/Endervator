package net.pugsworth.endervator.utils;

import java.util.LinkedList;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TeleportUtils {
    private static final LinkedList<TeleportItem> teleportQueue = new LinkedList<>();

    public static void tick(MinecraftServer server)
    {
        if (!teleportQueue.isEmpty()) {
            TeleportItem item = teleportQueue.pop();
            teleport(item);
        }
    }

    public static void teleport(TeleportItem item)
    {
        Vec3d pos = item.getTargetPosition();
        item.getEntity().requestTeleport(pos.getX(), pos.getY(), pos.getZ());
    }

    public static void enqueueTeleport(World world, Entity entity, Vec3d toPos)
    {
        TeleportItem item = new TeleportItem(world, entity, toPos);

        teleportQueue.push(item);
    }
}
