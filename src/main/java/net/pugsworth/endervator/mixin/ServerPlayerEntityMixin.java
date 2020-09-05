package net.pugsworth.endervator.mixin;

import java.util.stream.Stream;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.pugsworth.endervator.EndervatorMod;
import net.pugsworth.endervator.Block.EndervatorBlock;
import net.pugsworth.endervator.utils.ExperienceUtils;

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
        if (EndervatorBlock.isStandingOn((PlayerEntity)this, this.world))
        {
            TryTeleportNextEndervator(this.getBlockPos().down(), Direction.UP);
            return;
        }
        
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

            if (nextPos != null)
            {
                int distance = blockPosBelow.getManhattanDistance(nextPos);
                boolean canTeleport = this.isCreative();

                if (EndervatorMod.CONFIG.fuel.useXP)
                {
                    int xpNeeded = (int)Math.round(EndervatorMod.CONFIG.fuel.xpAmount * (EndervatorMod.CONFIG.fuel.xpAmountAbsolute ? 1 : distance));

                    if (ExperienceUtils.getExperienceInt(this) >= xpNeeded)
                    {
                        canTeleport = true;
                        this.addExperience(-xpNeeded);
                    }
                }
                else if (EndervatorMod.CONFIG.fuel.useDamage)
                {
                    float dmgNeeded = EndervatorMod.CONFIG.fuel.damagePerBlock * distance;

                    if (this.getHealth() >= dmgNeeded)
                    {
                        canTeleport = true;
                        this.damage(DamageSource.MAGIC, dmgNeeded);
                    }
                }
                else if (EndervatorMod.CONFIG.fuel.useItem)
                {
                    // TODO: do item fuels
                }
                else {
                    canTeleport = true; // free if all settings off
                }

                if (!canTeleport)
                    return;

                this.setPosition(blockPosBelow.getX() + 0.5, blockPosBelow.getY(), blockPosBelow.getZ() + 0.5);
                EndervatorBlock.TeleportEntityToEndervatorBlock(this, this.world, nextPos);

                if (EndervatorMod.CONFIG.damage.doesDamage)
                {
                    float dmg = distance * (float)EndervatorMod.CONFIG.damage.damagePerBlock;
                    damage(DamageSource.MAGIC, dmg);
                }

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