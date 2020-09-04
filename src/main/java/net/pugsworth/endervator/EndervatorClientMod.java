package net.pugsworth.endervator;

import org.apache.logging.log4j.Level;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EndervatorClientMod implements ClientModInitializer
{

    @Override
    public void onInitializeClient()
    {
        ClientSidePacketRegistry.INSTANCE.register(EndervatorMod.TELEPORT_PACKET, 
        (packetContext, attachedData) -> {
            BlockPos origin = attachedData.readBlockPos();
            BlockPos destination = attachedData.readBlockPos();

            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                World world = player.world;

                DoParticles(world, origin, destination);
            });
        });
    }

    public static void DoParticles(World world, BlockPos origin, BlockPos destination)
    {
        if (!EndervatorMod.CONFIG.client.useParticleStream)
            return;
            
        int distance = origin.getManhattanDistance(destination);
        Direction direction = Direction.DOWN;

        if (destination.subtract(origin).getY() > 0) {
            direction = Direction.UP;
        }

        double vx = ((double)world.random.nextFloat() - 0.5D) * 0.5D;
        double vy = 2.0d;
        double vz = ((double)world.random.nextFloat() - 0.5D) * 0.5D;

        EndervatorMod.logger.log(Level.DEBUG, vx);

        // 2 particles per block?
        for (int i = 0; i < (distance*2); i++) {
            for (int j = 0; j < 3; j++) {
                double rx = ((double)world.random.nextFloat() - 0.5D) * 0.5D;
                double rz = ((double)world.random.nextFloat() - 0.5D) * 0.5D;

                if (direction == Direction.UP)
                {
                    world.addParticle(ParticleTypes.PORTAL, origin.getX() + 0.5 + rx, origin.getY() + (0.5 * i), origin.getZ() + 0.5 + rz, 0.0, -vy, 0.0);
                }
                else
                {
                    world.addParticle(ParticleTypes.PORTAL, destination.getX() + 0.5 + rx, destination.getY() + (0.5 * i), destination.getZ() + 0.5 + rz, 0.0, vy, 0.0);
                }
            }
        }
        
    }

}