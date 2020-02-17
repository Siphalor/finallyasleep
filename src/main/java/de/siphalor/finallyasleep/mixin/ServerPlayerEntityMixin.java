package de.siphalor.finallyasleep.mixin;

import com.mojang.authlib.GameProfile;
import de.siphalor.finallyasleep.FinallyAsleep;
import de.siphalor.finallyasleep.config.Config;
import de.siphalor.finallyasleep.util.IServerPlayerEntity;
import de.siphalor.finallyasleep.util.IServerWorld;
import de.siphalor.finallyasleep.util.MessageUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements IServerPlayerEntity {
	@Shadow @Final public MinecraftServer server;
	@Shadow public ServerPlayNetworkHandler networkHandler;
	@Unique
	private boolean finallyAsleepPresent = false;

	public ServerPlayerEntityMixin(World world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	public boolean isSleepingLongEnough() {
		if(world instanceof IServerWorld && ((IServerWorld) world).areAllPlayersAsleep()) {
			return !isSleeping() || getSleepTimer() >= 100;
		} else {
			return super.isSleepingLongEnough();
		}
	}

	@Override
	public boolean hasFinallyAsleep() {
		return finallyAsleepPresent;
	}

	@Override
	public void setHasFinallyAsleep(boolean present) {
		finallyAsleepPresent = present;
	}

	@Inject(method = "wakeUp", at = @At("TAIL"))
	public void onWokeUp(boolean a, boolean hasSlept, CallbackInfo callbackInfo) {
		if(!Config.MESSAGE_SINGLE_PLAYERS.value && server.getPlayerManager().getPlayerList().stream().filter(serverPlayerEntity -> !serverPlayerEntity.isSpectator()).count() <= 1)
			return;
		if(networkHandler == null)
			return;
		if(hasSlept) {
			MessageUtil.send((ServerPlayerEntity)(Object) this, FinallyAsleep.PLAYER_CANCELED_SLEEP, getName().asString());
		}
	}
}
