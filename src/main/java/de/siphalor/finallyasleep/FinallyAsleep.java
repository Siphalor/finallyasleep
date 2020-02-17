package de.siphalor.finallyasleep;

import de.siphalor.finallyasleep.config.Config;
import de.siphalor.finallyasleep.util.IServerPlayerEntity;
import de.siphalor.finallyasleep.util.MessageUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FinallyAsleep implements ModInitializer {
	public static final String MOD_ID = "finallyasleep";

	public static final Identifier MOD_PRESENT_C2S_PACKET = new Identifier(MOD_ID, "present");

	public static final MessageUtil.Message WAITING_FOR_PLAYERS_MESSAGE = new MessageUtil.Message(MOD_ID + ".chat.waiting_for_players", "%1$s players are waiting to fall asleep (%2$s needed)");
	public static final MessageUtil.Message PLAYER_CANCELED_SLEEP = new MessageUtil.Message(MOD_ID + ".chat.player_canceled", "%1$s woke up to early");
	public static final MessageUtil.Message READY_FOR_SLEEP_MESSAGE = new MessageUtil.Message(MOD_ID + ".chat.ready", "Nice work! We're ready to sleep now.");
	public static final MessageUtil.Message AWAKE_MESSAGE = new MessageUtil.Message(MOD_ID + ".chat.awake", "Good morning everyone. Seize the day!");

	@Override
	public void onInitialize() {
		Config.initialize();

		ServerSidePacketRegistry.INSTANCE.register(MOD_PRESENT_C2S_PACKET, (packetContext, packetByteBuf) -> {
			PlayerEntity playerEntity = packetContext.getPlayer();
			if(playerEntity instanceof IServerPlayerEntity) {
				((IServerPlayerEntity) playerEntity).setHasFinallyAsleep(true);
			}
		});
	}
}
