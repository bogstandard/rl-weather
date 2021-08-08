package com.rlweather;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;


@Slf4j
@PluginDescriptor(
	name = "Weather"
)
public class RlweatherPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private RlweatherConfig config;

	@Inject
	private RlweatherOverlay overlay;

	@Inject
	private OverlayManager overlayManager;


	@Override
	protected void startUp() throws Exception
	{
		log.info("Rlweather started!");
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Rlweather stopped!");
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		// TODO use this snippet later
		/*
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Rlweather says " + config.greeting(), null);
		}
		*/
	}

	@Provides
	RlweatherConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RlweatherConfig.class);
	}
}
