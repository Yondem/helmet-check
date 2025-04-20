package com.storagereminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "Storage Reminder",
		description = "Shows a reminder when the logout menu is open",
		tags = {"logout", "reminder", "storage", "overlay", "items"}
)
@Slf4j
public class StorageReminderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private StorageReminderConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private StorageReminderOverlay overlay;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
	}

	@Provides
	StorageReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(StorageReminderConfig.class);
	}
}