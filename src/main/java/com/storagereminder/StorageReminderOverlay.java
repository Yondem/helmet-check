package com.storagereminder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class StorageReminderOverlay extends Overlay
{
    private final Client client;
    private final StorageReminderConfig config;

    @Inject
    public StorageReminderOverlay(Client client, StorageReminderConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.enableReminder())
        {
            return null;
        }

        // Check if the logout menu is open
        Widget logoutWindow = client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST);
        if (logoutWindow == null || logoutWindow.isHidden())
        {
            // Also check the logout button as an alternative way to detect
            Widget logoutButton = client.getWidget(WidgetInfo.LOGOUT_BUTTON);
            if (logoutButton == null || logoutButton.isHidden())
            {
                return null;
            }
        }
    }
}