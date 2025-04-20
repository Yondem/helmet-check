package com.storagereminder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.FontManager;

public class StorageReminderOverlay extends Overlay
{
    private final Client client;
    private final StorageReminderConfig config;
    private final StorageReminderPlugin plugin;

    private static final int PADDING = 10;
    private static final int MIN_WIDTH = 200;

    @Inject
    public StorageReminderOverlay(Client client, StorageReminderConfig config, StorageReminderPlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;

        setPriority(OverlayPriority.HIGH);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.enableReminder() || !plugin.isShowReminder())
        {
            return null;
        }

        // Only show on flashing frames if we're still flashing
        if (!plugin.isFlashState() && plugin.getFlashCount() < config.maxFlashes() * 2)
        {
            return null;
        }

        // Set a larger font for better visibility
        graphics.setFont(FontManager.getRunescapeBoldFont());

        String reminderText = config.reminderText();

        // Calculate dimensions
        FontMetrics fm = graphics.getFontMetrics();
        int textWidth = fm.stringWidth(reminderText);
        int textHeight = fm.getHeight();

        int boxWidth = Math.max(textWidth + PADDING * 2, MIN_WIDTH);
        int boxHeight = textHeight + PADDING * 2;

        // Calculate center position
        int canvasWidth = client.getCanvasWidth();
        int canvasHeight = client.getCanvasHeight();

        int x = (canvasWidth - boxWidth) / 2;
        int y = (canvasHeight - boxHeight) / 2;

        // Draw background with border
        graphics.setColor(config.backgroundColor());
        graphics.fillRect(x, y, boxWidth, boxHeight);

        // Draw a thicker border that stands out
        graphics.setColor(config.borderColor());
        graphics.setStroke(new BasicStroke(2));
        graphics.drawRect(x, y, boxWidth, boxHeight);

        // Draw text centered in the box
        graphics.setColor(config.textColor());
        graphics.drawString(
                reminderText,
                x + (boxWidth - textWidth) / 2,
                y + (boxHeight + textHeight) / 2 - fm.getDescent()
        );

        return null;  // Overlay position is determined dynamically
    }
}