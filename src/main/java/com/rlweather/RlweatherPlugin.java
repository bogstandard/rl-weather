package com.rlweather;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Random;


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

	// TIMEOUTS
	public int lastLightning = 10;

	// AUDIO
	public Sound sound = new Sound();

	// SOUND KEYS
	// used to manage the sounds in use
	protected String KEY_RAIN = "rain";
	protected String KEY_THUNDER = "thunder";

	// FLAGS FOR RENDER
	// read by the render method to decide what to do each frame
	public boolean PERFORM_LIGHTNING = false; // also changed in render for the 1fr quickness
	public boolean PERFORM_RAIN = false;
	public boolean PERFORM_SNOW = false;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Weather started!");
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Weather stopped!");
		sound.stopAll();
		overlayManager.remove(overlay);
	}


	@Subscribe
	public void onGameTick(GameTick gameTick) {

		// RESET FLAGS
		PERFORM_LIGHTNING = false;
		PERFORM_RAIN = false;
		PERFORM_SNOW = false;

		// LIGHTNING
		if(config.lightningEnabled()) {
			if(lastLightning <= 0) {
				Random r = new Random();
				if(r.nextInt(20) == 0) { // 1/20 chance of lightning when it hits
					// set flag to flash lightning
					PERFORM_LIGHTNING = true;

					// reset lightning timer
					lastLightning = config.lightningFrequency();

					// play audio
					if(config.soundsEnabled()) {
						sound.thunder(KEY_THUNDER);
					}
				}
			}
		}
		// always count down regardless
		if(lastLightning > 0) { lastLightning--; }
		else if(lastLightning < 0) { lastLightning = 0; }

		// RAIN
		if(config.rainEnabled()) {
			// set flag to make rain
			PERFORM_RAIN = true;
			// if not already raining, begin rain sound
			if(!sound.isPlaying(KEY_RAIN) && config.soundsEnabled()) {
				sound.rain(KEY_RAIN);
			}
		}
		else {
			sound.stop(KEY_RAIN);
		}

		// SNOW
		if(config.snowEnabled()) {
			// set flag to make snow
			PERFORM_SNOW = true;
		}

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGIN_SCREEN
			|| gameStateChanged.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR
			|| gameStateChanged.getGameState() == GameState.LOGGING_IN
			|| gameStateChanged.getGameState() == GameState.HOPPING
			|| gameStateChanged.getGameState() == GameState.CONNECTION_LOST) {
			sound.stopAll();
		}
	}

	@Provides
	RlweatherConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RlweatherConfig.class);
	}
}
