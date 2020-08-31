package net.pugsworth.endervator.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pugsworth.endervator.Block.EndervatorBlock;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world_1, GameProfile gameProfile_1) {
        super(world_1, gameProfile_1);
        // TODO Auto-generated constructor stub
    }

    // @Inject(at = @At("TAIL"), method = "jump()V")
    @Override
    public void jump()
    {
        TryTeleportNextEndervator(this.getBlockPos().down(), Direction.UP);
    }

    // @Inject(at = @At("TAIL"), method = "(B)setSneaking()V")
    @Override
    public void setSneaking(boolean sneaking)
    {
        if (sneaking == true) {
            TryTeleportNextEndervator(this.getBlockPos().down(), Direction.DOWN);
        }
    }

    private void TryTeleportNextEndervator(BlockPos origin, Direction direction)
    {
        if (EndervatorBlock.isStandingOn((PlayerEntity)this, this.world))
        {
            BlockPos blockPosBelow = this.getBlockPos().down();
            BlockPos nextPos = EndervatorBlock.FindEndervator(this.world, blockPosBelow, direction);

            if (nextPos != null) {
                EndervatorBlock.TeleportEntityToEndervatorBlock(this, this.world, nextPos);
            }
        }
    }
}