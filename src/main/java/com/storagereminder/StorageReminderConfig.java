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
			description = "The text to display in overlay box"
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
		return Color.RED;
	}

	@ConfigItem(
			keyName = "backgroundColor",
			name = "Background Color",
			description = "The color of the reminder background"
	)
	default Color backgroundColor()
	{
		return new Color(0, 0, 0, 150);  // Semi-transparent black
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