package net.pugsworth.endervator.Block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.pugsworth.endervator.EndervatorMod;
import net.pugsworth.endervator.utils.TeleportUtils;

public class EndervatorBlock extends Block 
{
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
    public int getLuminance(BlockState blockState) {
        return blockState.get(POWERED) ? 0 : super.getLuminance(blockState);
    }

    public static boolean isStandingOn(PlayerEntity entity, World world)
    {
        BlockPos pos = entity.getBlockPos().down();
        Block blockBelow = world.getBlockState(pos).getBlock();

        if (blockBelow.equals(ModBlocks.ENDERVATOR_BLOCK)) {
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
        for (int i = 0; i < EndervatorMod.CONFIG.maximumDistance; i++)
        {
            newpos = newpos.add(direction.getVector());
            newblockstate = world.getBlockState(newpos);
            newblock = newblockstate.getBlock();

            if (newblock.equals(ModBlocks.ENDERVATOR_BLOCK) && !newblockstate.get(POWERED)) {
                return newpos;
            }
        }

        return null;
    }

    public static void TeleportEntityToEndervatorBlock(PlayerEntity playerEntity, World world, BlockPos pos)
    {
        BlockPos newBlockPos = pos.up();
        Vec3d newPos = new Vec3d(newBlockPos.getX() + 0.5d, newBlockPos.getY(), newBlockPos.getZ() + 0.5d);
        TeleportUtils.enqueueTeleport(world, playerEntity, newPos);

        PlaySound(world, newBlockPos, EndervatorMod.CONFIG.soundName);
    }

    public static void PlaySound(World world, BlockPos pos, String id)
    {
        SoundEvent sound = new SoundEvent(new Identifier(id));
        PlaySound(world, pos, sound);
    }

    public static void PlaySound(World world, BlockPos pos, SoundEvent sound)
    {
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random)
    {
        if (blockState.get(POWERED) || !EndervatorMod.CONFIG.client.useParticles) {
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
}