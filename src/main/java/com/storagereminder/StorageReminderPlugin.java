package com.storagereminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.InterfaceID;
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

		// Check if logout menu is open by looking for the logout button in the interface
		boolean isLogoutMenuOpen = isLogoutInterfaceOpen();

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

	/**
	 * Checks if the logout interface is currently open
	 * @return true if the logout interface is open
	 */
	private boolean isLogoutInterfaceOpen()
	{
		// Check for the specific logout panel widget
		Widget logoutPanel = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_LOGOUT_TAB);
		if (logoutPanel != null && !logoutPanel.isHidden())
		{
			return true;
		}

		// Alternative check for the "Click here to logout" button
		Widget logoutButton = client.getWidget(WidgetInfo.LOGOUT_BUTTON);
		if (logoutButton != null && !logoutButton.isHidden())
		{
			return true;
		}

		return false;
	}

	@Provides
	StorageReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(StorageReminderConfig.class);
	}

	// Getter methods
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