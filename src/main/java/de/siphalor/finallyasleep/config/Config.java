package de.siphalor.finallyasleep.config;

import de.siphalor.finallyasleep.FinallyAsleep;
import de.siphalor.tweed.config.ConfigEnvironment;
import de.siphalor.tweed.config.ConfigFile;
import de.siphalor.tweed.config.ConfigScope;
import de.siphalor.tweed.config.TweedRegistry;
import de.siphalor.tweed.config.entry.BooleanEntry;
import de.siphalor.tweed.config.entry.FloatEntry;
import de.siphalor.tweed.config.entry.ListEntry;
import net.minecraft.util.Formatting;

import java.util.Collections;

public class Config {
	public static final ConfigFile FILE = TweedRegistry.registerConfigFile(FinallyAsleep.MOD_ID)
			.setEnvironment(ConfigEnvironment.SERVER)
			.setScope(ConfigScope.SMALLEST);

	public static final FloatEntry PLAYERS_SLEEPING_PERCENTAGE = FILE.register("playersSleepingPercentage", new FloatEntry(.5F))
			.setComment("Sets the percentage of players sleeping that is minimally required to skip the night")
	;
	public static final BooleanEntry MESSAGE_SINGLE_PLAYERS = FILE.register("messageSinglePlayers", new BooleanEntry(false))
			.setComment("Sets whether to send chat messages when only a single player is online")
	;
	public static final ListEntry<FormattingEntry> MESSAGE_FORMATTING = FILE.register(
			"messageFormatting",
			new ListEntry<>(Collections.singletonList(new FormattingEntry(Formatting.GOLD)), () -> new FormattingEntry(Formatting.WHITE))
			.setComment("An array of formattings that are used for displaying the chat messages.\n" +
					"Possible values are the Minecraft color names or \"bold\", \"italic\" etc.")
	);

	public static void initialize() {

	}
}
