package com.storagereminder;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.Color;

@ConfigGroup("storagereminder")
public interface StorageReminderConfig extends Config
{
	@ConfigItem(
			keyName = "reminderText",
			name = "Reminder Text",
			description = "The text to display in the reminder"
	)
	default String reminderText()
	{
		return "Don't forget your items in storage!";
	}

	@ConfigItem(
			keyName = "textColor",
			name = "Text Color",
			description = "The color of the reminder text"
	)
	default Color textColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
			keyName = "backgroundColor",
			name = "Background Color",
			description = "The color of the reminder background"
	)
	default Color backgroundColor()
	{
		return new Color(255, 0, 0, 150);  // Semi-transparent black
	}

	@ConfigItem(
			keyName = "borderColor",
			name = "Border Color",
			description = "The color of the reminder border"
	)
	default Color borderColor()
	{
		return Color.BLACK;
	}

	@ConfigItem(
			keyName = "maxFlashes",
			name = "Max Flashes",
			description = "Maximum number of flashes (0 = flash indefinitely while menu open)"
	)
	default int maxFlashes()
	{
		return 5;
	}

	@ConfigItem(
			keyName = "enableReminder",
			name = "Enable Reminder",
			description = "Enable the storage reminder"
	)
	default boolean enableReminder()
	{
		return true;
	}
}