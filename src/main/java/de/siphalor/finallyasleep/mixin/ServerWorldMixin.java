package de.siphalor.finallyasleep.mixin;

import de.siphalor.finallyasleep.config.Config;
import de.siphalor.finallyasleep.FinallyAsleep;
import de.siphalor.finallyasleep.util.IServerWorld;
import de.siphalor.finallyasleep.util.MessageUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements IServerWorld {
	@Shadow @Final private List<ServerPlayerEntity> players;

	@Shadow private boolean allPlayersSleeping;

	@Unique int lastNoPlayer = 0;
	@Unique int lastNoSleepers = 0;

	@Inject(method = "updatePlayersSleeping", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerWorld;allPlayersSleeping:Z", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void updatePlayersSleeping(CallbackInfo callbackInfo, int noSpectators, int noSleepers) {
		int noPlayers = players.size();
		if(noSleepers == 0)
			return;
		if(noPlayers == lastNoPlayer && noSleepers == lastNoSleepers)
			return;
		if(noPlayers > 1) {
			int noRequired = MathHelper.ceil((noPlayers - noSpectators) * Config.PLAYERS_SLEEPING_PERCENTAGE.value);

			allPlayersSleeping = noSleepers >= noRequired;

			if(allPlayersSleeping) {
				if(noSleepers - noRequired == 0)
					MessageUtil.send(players, FinallyAsleep.READY_FOR_SLEEP_MESSAGE);
			} else {
				MessageUtil.send(players, FinallyAsleep.WAITING_FOR_PLAYERS_MESSAGE,
						noSleepers,
						noRequired,
						noPlayers
				);
			}
		} else if(Config.MESSAGE_SINGLE_PLAYERS.value && noPlayers - noSpectators == 1 && noSleepers == 1) {
			MessageUtil.send(players.get(0), FinallyAsleep.READY_FOR_SLEEP_MESSAGE);
		}
	}

	@Inject(method = "method_23660", at = @At("TAIL"))
	private void onDaylightCycle(CallbackInfo callbackInfo) {
		MessageUtil.send(players, FinallyAsleep.AWAKE_MESSAGE);
	}

	@Override
	public boolean areAllPlayersAsleep() {
		return allPlayersSleeping;
	}
}
