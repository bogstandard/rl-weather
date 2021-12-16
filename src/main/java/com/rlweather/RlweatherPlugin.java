package com.rlweather;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
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

	@Inject
	private ChatMessageManager chatMessageManager;

	// TIMEOUTS
	public int lastLightning = 10;

	// AUDIO
	public Sound sound = new Sound();

	// SOUND KEYS
	// used to manage the sounds in use
	protected String KEY_RAIN = "rain";
	protected String KEY_THUNDER = "thunder";

	// GENERAL FLAGS
	public boolean PLAYER_OUTSIDE = false;

	// FLAGS FOR RENDER
	// read by the render method to decide what to do each frame
	public boolean PERFORM_LIGHTNING = false; // also changed in render for the 1fr quickness
	public boolean PERFORM_RAIN = false;
	public boolean PERFORM_SNOW = false;

	// Location variables
	public WeatherAPI weatherAPI = new WeatherAPI();

	@Override
	protected void startUp() throws Exception
	{
		log.info("Weather started!");
		overlayManager.add(overlay);
		weatherAPI.setChatMessageManager(chatMessageManager);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Weather stopped!");
		sound.stopAll();
		overlayManager.remove(overlay);
	}

	private boolean isLightningEnabled() {
		if(config.locationEnabled()) {
			return config.lightningEnabled() && weatherAPI.isThundering();
		} else {
			return config.lightningEnabled();
		}
	}
	private boolean isRainEnabled() {
		if(config.locationEnabled()) {
			return config.rainEnabled() && weatherAPI.isRaining();
		} else {
			return config.rainEnabled();
		}
	}
	private boolean isSnowEnabled() {
		if(config.locationEnabled()) {
			return config.snowEnabled() && weatherAPI.isSnowing();
		} else {
			return config.snowEnabled();
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick) {
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return; // no player?? bow out early.
		}

		// UPDATE PLAYER STATUS
		PLAYER_OUTSIDE = player.getWorldLocation().getY() < Constants.OVERWORLD_MAX_Y;

		// RESET FLAGS
		PERFORM_LIGHTNING = false;
		PERFORM_RAIN = false;
		PERFORM_SNOW = false;

		// RETURN EARLY IF PLAYER NOT OUTSIDE
		if(!PLAYER_OUTSIDE) return;

		if(config.locationEnabled()) {
			weatherAPI.setApiKey(config.apiKey());
			weatherAPI.setLocation(config.location());
			weatherAPI.update();
		}

		// LIGHTNING
		if(isLightningEnabled()) {
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
		if(isRainEnabled()) {
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
		if(isSnowEnabled()) {
			// set flag to make snow
			PERFORM_SNOW = true;
		}

		// SOUNDS
		if(!config.soundsEnabled()) {
			// stop all sounds immediately if sounds disabled
			sound.stopAll();
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
