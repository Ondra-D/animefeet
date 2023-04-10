package com.ondrad.animefeet;

import com.google.inject.Provides;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.swing.Timer;

import java.io.IOException;

@Slf4j
@PluginDescriptor(
		name = "AnimeFeet",
		description = "Displays anime feet",
		tags = {"anime", "feet", "overlay", "nsfw"}
)
public class animeFeetPlugin extends Plugin {

	@Inject
	private animeFeetConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private animeFeetOverlay overlay;
	private Timer timer;


	@Override
	protected void startUp(){

		overlayManager.add(overlay);

		int delaySeconds = config.delaySeconds();
		int delayMillis = delaySeconds * 1000;

		timer = new Timer(delayMillis, e -> {
			try {
				overlay.GETRequest();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
		timer.start();

	}


	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		timer.stop();
	}


	@Provides
	animeFeetConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(animeFeetConfig.class);
	}
}
