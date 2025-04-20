package com.storagereminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
		name = "Storage Reminder",
		description = "Shows a flashing reminder when the logout menu is open",
		tags = {"logout", "reminder", "storage", "overlay", "items", "flash"}
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

	private boolean flashState = false;
	private int flashCount = 0;
	private boolean showReminder = false;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		showReminder = false;
		flashCount = 0;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		// Check if logout menu is open
		Widget logoutWindow = client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST);
		boolean isLogoutMenuOpen = (logoutWindow != null && !logoutWindow.isHidden());

		// Only start flashing when the menu first opens
		if (isLogoutMenuOpen && !showReminder)
		{
			showReminder = true;
			flashCount = 0;
		}
		else if (!isLogoutMenuOpen && showReminder)
		{
			showReminder = false;
			flashCount = 0;
		}

		// If we need to show the reminder, manage the flash state
		if (showReminder)
		{
			// Toggle flash state each tick for the flashing effect
			flashState = !flashState;

			// Count flashes if we want to limit the number of flashes
			if (config.maxFlashes() > 0 && flashCount >= config.maxFlashes() * 2)
			{
				flashState = true; // Keep visible after max flashes
			}
			else
			{
				flashCount++;
			}
		}
	}

	@Provides
	StorageReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(StorageReminderConfig.class);
	}

	// Add explicit getter methods to avoid issues with Lombok
	public boolean isShowReminder()
	{
		return showReminder;
	}

	public boolean isFlashState()
	{
		return flashState;
	}

	public int getFlashCount()
	{
		return flashCount;
	}
}