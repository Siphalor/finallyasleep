package de.siphalor.finallyasleep.config;

import de.siphalor.tweed.config.entry.AbstractValueEntry;
import de.siphalor.tweed.data.DataContainer;
import de.siphalor.tweed.data.DataValue;
import net.minecraft.util.Formatting;
import net.minecraft.util.PacketByteBuf;

import java.util.Locale;

public class FormattingEntry extends AbstractValueEntry<Formatting, FormattingEntry> {
	/**
	 * Constructs a new entry
	 *
	 * @param defaultValue The default value to use
	 */
	public FormattingEntry(Formatting defaultValue) {
		super(defaultValue);
	}

	@Override
	public Formatting readValue(DataValue dataValue) {
		return Formatting.byName(dataValue.asString().toLowerCase(Locale.ROOT));
	}

	@Override
	public Formatting readValue(PacketByteBuf buf) {
		return Formatting.byColorIndex(buf.readInt());
	}

	@Override
	public <Key> void writeValue(DataContainer<?, Key> parent, Key name, Formatting value) {
		parent.set(name, value.getName());
	}

	@Override
	public void writeValue(Formatting value, PacketByteBuf buf) {
		buf.writeInt(value.getColorIndex());
	}
}
