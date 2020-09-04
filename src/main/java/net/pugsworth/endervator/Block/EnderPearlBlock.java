package net.pugsworth.endervator.Block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pugsworth.endervator.EndervatorMod;

public class EnderPearlBlock extends Block
{
    public static BooleanProperty ENABLED = Properties.ENABLED;

    public EnderPearlBlock(Settings block$Settings)
    {
        super(block$Settings);
        this.setDefaultState((BlockState)(this.getDefaultState()).with(ENABLED, true));
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory)
    {
        stateFactory.add(ENABLED);
    }

    @Override
    public int getLuminance(BlockState blockState) {
        return blockState.get(ENABLED) ? super.getLuminance(blockState) : 0;
    }

    @Override
    public void onScheduledTick(BlockState blockState, World world, BlockPos blockPos, Random random)
    {
        world.setBlockState(blockPos, blockState.with(ENABLED, true));
        super.onScheduledTick(blockState, world, blockPos, random);
    }

    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult)
    {
        if (blockState.get(ENABLED)) {
            TeleportRandomly(world, blockPos, playerEntity);
        }

        return super.activate(blockState, world, blockPos, playerEntity, hand, blockHitResult);
    }

    @Override
    public void onSteppedOn(World world, BlockPos blockPos, Entity entity) {
        BlockState blockState = world.getBlockState(blockPos);

        if (entity instanceof PlayerEntity && blockState.get(ENABLED)) {
            TeleportRandomly(world, blockPos, (PlayerEntity)entity);
        }
        super.onSteppedOn(world, blockPos, entity);
    }

    @Override
    public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity)
    {
        super.onEntityCollision(blockState, world, blockPos, entity);

        if (entity instanceof PlayerEntity)
        {
            EndervatorMod.logger.info("A Player has collided!");
            TeleportRandomly(world, blockPos, (PlayerEntity)entity);
        }
    }

    public void TeleportRandomly(World world, BlockPos blockPos, PlayerEntity playerEntity)
    {
        if (world.isClient)
            return;

        BlockPos.Mutable targetBlock = new BlockPos.Mutable(blockPos);
        BlockState blockState = world.getBlockState(targetBlock);

        for (int i = 0; i < 16; i++)
        {
            double rx = (world.random.nextDouble() - 0.5D) * 64.0D;
            double ry = world.getHeight();
            double rz = (world.random.nextDouble() - 0.5D) * 64.0D;

            targetBlock.setOffset((int)rx, (int)ry, (int)rz);

            BlockState targetBlockState = world.getBlockState(targetBlock);

            while (targetBlock.getY() > 0)
            {
                targetBlockState = world.getBlockState(targetBlock);

                if (validStandBlock(world, targetBlock, targetBlockState))
                {
                    break;
                }

                targetBlock.setOffset(Direction.DOWN);
                EndervatorMod.logger.info(String.format("(%s, %s, %s)", targetBlock.getX(), targetBlock.getY(), targetBlock.getZ()));
            }

            targetBlock.setOffset(Direction.UP);

            EndervatorMod.logger.info(String.format("Try #%s to teleport to position (%s, %s, %s)", i, targetBlock.getX(), targetBlock.getY(), targetBlock.getZ()));

            // TODO: Check for proper collision

            if (validStandBlock(world, targetBlock, targetBlockState))
            {
                playerEntity.teleport(targetBlock.getX() + 0.5, targetBlock.getY(), targetBlock.getZ() + 0.5);
            
                world.setBlockState(blockPos, blockState.with(ENABLED, false));
                world.getBlockTickScheduler().schedule(blockPos, blockState.getBlock(), 20);
                world.playSound(playerEntity, blockPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);

                return;
            }
        }
    }

    private boolean validStandBlock(World world, BlockPos blockPos, BlockState blockState)
    {
        boolean blocksMovement = blockState.getMaterial().blocksMovement();
        boolean isSolid = blockState.getMaterial().isSolid();
        boolean isLiquid = blockState.getMaterial().isLiquid();

        return isSolid;
    }

    private boolean doesPlayerFitIn(World world, BlockPos blockPos)
    {
        return false;
    }
    
}
