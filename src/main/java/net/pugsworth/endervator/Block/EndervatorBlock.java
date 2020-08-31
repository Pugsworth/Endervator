package net.pugsworth.endervator.Block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Settings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pugsworth.endervator.EndervatorMod;

public class EndervatorBlock extends Block
{
    public EndervatorBlock(Settings settings)
    {
        super(settings);
    }

    public static int MAXIMUM_DISTANCE = 16;

    public static boolean isStandingOn(PlayerEntity entity, World world)
    {
        BlockPos pos = entity.getBlockPos().down();
        Block blockBelow = world.getBlockState(pos).getBlock();

        if (blockBelow.equals(EndervatorMod.ENDERVATOR_BLOCK)) {
            return true;
        }

        return false;
    }

    public static BlockPos FindEndervator(World world, BlockPos origin, Direction direction)
    {
        BlockPos newpos = origin;
        Block newblock;
        for (int i = 0; i < MAXIMUM_DISTANCE; i++)
        {
            newpos = newpos.add(direction.getVector());
            newblock = world.getBlockState(newpos).getBlock();

            if (newblock.equals(EndervatorMod.ENDERVATOR_BLOCK)) {
                return newpos;
            }
        }

        return null;
    }

    public static void TeleportEntityToEndervatorBlock(Entity entity, World world, BlockPos pos)
    {
        BlockPos newpos = pos.up();
        entity.teleport((double)newpos.getX() + 0.5, (double)newpos.getY(), (double)newpos.getZ() + 0.5);
        world.playSound(null, newpos.getX(), newpos.getY(), newpos.getZ(), SoundEvents.ENTITY_SHULKER_TELEPORT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        DoParticles(world, entity.getBlockPos(), newpos);
    }

    @Environment(EnvType.CLIENT)
    public static void DoParticles(World world, BlockPos origin, BlockPos destination)
    {
        int distance = origin.getManhattanDistance(destination);
        Direction direction = Direction.UP;

        if (origin.subtract(destination).getY() > 0) {
            direction = Direction.UP;
        }

        double vx = ((double)world.random.nextFloat() - 0.5D) * 0.5D;
        double vy = 1.0d;
        double vz = ((double)world.random.nextFloat() - 0.5D) * 0.5D;

        // 2 particles per block?
        for (int i = 0; i < (distance*2); i++) {
            world.addParticle(ParticleTypes.PORTAL, origin.getX(), origin.getY() + (0.5 * i), origin.getZ(), vx, vy, vz);
        }
        
    }
}