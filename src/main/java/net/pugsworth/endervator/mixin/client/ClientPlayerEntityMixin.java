package net.pugsworth.endervator.mixin.client;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
	@Shadow
	public Input input;

	@Inject(at = @At("TAIL"), method = "tickMovement()V")
	public void tickMovement(CallbackInfo callbackInfo)
	{
		boolean isJumping = this.input.jumping;
		boolean isSneaking = this.input.sneaking;

		if (isJumping || isSneaking)
		{
			System.out.println("jumping or sneaking!");
		}
	}
}
