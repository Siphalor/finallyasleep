package de.siphalor.finallyasleep.util;

import de.siphalor.finallyasleep.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Collection;

public class MessageUtil {
	public static void send(ServerPlayerEntity playerEntity, Message message, Object... args) {
		if(playerEntity instanceof IServerPlayerEntity && ((IServerPlayerEntity) playerEntity).hasFinallyAsleep()) {
			playerEntity.sendMessage(new TranslatableText(message.id, args).formatted(getFormatting()));
		} else {
			playerEntity.sendMessage(new LiteralText(String.format(message.literal, args)).formatted(getFormatting()));
		}
	}

	public static void send(Collection<ServerPlayerEntity> playerEntities, Message message, Object... args) {
		Text translatableText = new TranslatableText(message.id, args).formatted(getFormatting());
		Text literalText = new LiteralText(String.format(message.literal, args)).formatted(getFormatting());

		for(PlayerEntity playerEntity : playerEntities) {
			if(playerEntity instanceof IServerPlayerEntity && ((IServerPlayerEntity) playerEntity).hasFinallyAsleep()) {
				playerEntity.sendMessage(translatableText);
			} else {
				playerEntity.sendMessage(literalText);
			}
		}
	}

	public static Formatting[] getFormatting() {
		return Config.MESSAGE_FORMATTING.valueList.stream().map(formattingEntry -> formattingEntry.value).toArray(Formatting[]::new);
	}

	public static class Message {
		private final String id;
		private final String literal;

		public Message(String id, String literal) {
			this.id = id;
			this.literal = literal;
		}
	}
}
