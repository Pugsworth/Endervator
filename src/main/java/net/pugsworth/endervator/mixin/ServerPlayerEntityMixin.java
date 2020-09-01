package net.pugsworth.endervator.mixin;

import java.util.stream.Stream;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pugsworth.endervator.EndervatorMod;
import net.pugsworth.endervator.Block.EndervatorBlock;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow
    private boolean inTeleportationState;

    public ServerPlayerEntityMixin(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    // @Inject(at = @At("TAIL"), method = "jump()V")
    @Override
    public void jump()
    {
        TryTeleportNextEndervator(this.getBlockPos().down(), Direction.UP);
        super.jump();
    }

    // @Inject(at = @At("TAIL"), method = "(B)setSneaking()V")
    @Override
    public void setSneaking(boolean sneaking)
    {
        super.setSneaking(sneaking);

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

                // Send particles packet
                Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, blockPosBelow);

                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(blockPosBelow);
                passedData.writeBlockPos(nextPos);
        
                watchingPlayers.forEach(player -> {
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, EndervatorMod.TELEPORT_PACKET, passedData);
                });
            }
        }
    }
}