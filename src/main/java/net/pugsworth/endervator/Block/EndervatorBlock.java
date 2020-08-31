package net.pugsworth.endervator.Block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block.Settings;
import net.minecraft.client.particle.RedDustParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pugsworth.endervator.EndervatorMod;

public class EndervatorBlock extends Block 
{
    public static int MAXIMUM_DISTANCE = 16;
    public static BooleanProperty POWERED = Properties.POWERED;

    public EndervatorBlock(Settings settings)
    {
        super(settings);
        this.setDefaultState((BlockState)(this.getDefaultState()).with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory)
    {
        stateFactory.add(POWERED);
    }

    public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean boolean_1)
    {
        if (!world.isClient)
        {
            boolean powered = world.isReceivingRedstonePower(blockPos);
            world.setBlockState(blockPos, blockState.with(POWERED, powered), 3);
        }
     }

    @Override
    public int getLuminance(BlockState blockState_1) {
        return blockState_1.get(POWERED) ? 0 : super.getLuminance(blockState_1);
    }

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
        if (world.getBlockState(origin).get(POWERED)) {
            return null;
        }

        BlockPos newpos = origin;
        BlockState newblockstate;
        Block newblock;
        for (int i = 0; i < MAXIMUM_DISTANCE; i++)
        {
            newpos = newpos.add(direction.getVector());
            newblockstate = world.getBlockState(newpos);
            newblock = newblockstate.getBlock();

            if (newblock.equals(EndervatorMod.ENDERVATOR_BLOCK) && !newblockstate.get(POWERED)) {
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
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random)
    {
        if (blockState.get(POWERED)) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            double rx = random.nextDouble() * 0.25;
            double ry = random.nextDouble() * 0.25;
            double rz = random.nextDouble() * 0.25;
            double vx = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double vy = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double vz = ((double)random.nextFloat() - 0.5D) * 0.5D;

            world.addParticle(ParticleTypes.PORTAL, blockPos.getX() + 0.5, blockPos.getY() + 0.5 + ry, blockPos.getZ() + 0.5, vx, vy, vz);
        }
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