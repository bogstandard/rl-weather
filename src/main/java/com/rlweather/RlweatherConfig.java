package com.rlweather;

import net.runelite.client.config.*;

import java.awt.Color;

@ConfigGroup("Weather")
public interface RlweatherConfig extends Config
{

	@ConfigSection(name = "Sound Effects", description = "Sound effects", position = 1, closedByDefault = false)
	String SoundEffectsSettings = "SoundEffectsSettings";

	// SOUND EFFECTS
	@ConfigItem(
			position = 1,
			keyName = "soundsenabled",
			name = "Weather Sounds Enabled (beta)",
			description = "Make the noises?",
			section = SoundEffectsSettings
	)
	default boolean soundsEnabled()
	{
		return false;
	}
	
	@Range(
		min = 0,
		max = 100
	)
	@ConfigItem(
			position = 2,
			keyName = "soundvolume",
			name = "Sound Volume",
			description = "Set volume level for sounds",
			section = SoundEffectsSettings
	)
	default int soundVolume()
	{
		return 25;
	}

	@ConfigSection(name = "Visual Effects", description = "Visual effects", position = 2, closedByDefault = false)
	String VisualEffectsSettings = "VisualEffectsSettings";

	@ConfigItem(
			position = 3,
			keyName = "depthenabled",
			name = "Depth Enabled",
			description = "Simulate depth on the particles?",
			section = VisualEffectsSettings
	)
	default boolean depthEnabled()
	{
		return false;
	}

	@ConfigSection(name = "Rain & Storm Effects", description = "Rain & storm effects", position = 3, closedByDefault = false)
	String RainStormEffectsSettings = "RainStormEffectsSettings";

	// LIGHTNING
	@ConfigItem(
			position = 4,
			keyName = "lightningenabled",
			name = "Lightning Enabled",
			description = "Is it really horrendous out? (Lightning Flashes)",
			section = RainStormEffectsSettings
	)
	default boolean lightningEnabled()
	{
		return false;
	}
	
	@ConfigItem(
			position = 5,
			keyName = "thunderenabled",
			name = "Thunder Enabled",
			description = "Is it really horrendous out? (Thunder Sounds)",
			section = RainStormEffectsSettings
	)
	default boolean thunderEnabled()
	{
		return false;
	}

	@ConfigItem(
			position = 6,
			keyName = "lightningfrequency",
			name = "Lightning Frequency (WARNING BRIGHT FLASHES)",
			description = "The rough frequency of the lightning in ticks",
			section = RainStormEffectsSettings
	)
	default int lightningFrequency()
	{
		return 100;
	}

	@ConfigItem(
			position = 7,
			keyName = "lightningcolor",
			name = "Lightning Color",
			description = "The color of the lightning",
			section = RainStormEffectsSettings
	)
	default Color lightningColor()
	{
		return new Color(255, 255, 253);
	}


	// RAIN
	@ConfigItem(
			position = 8,
			keyName = "rainenabled",
			name = "Rain Enabled",
			description = "Is it raining?",
			section = RainStormEffectsSettings
	)
	default boolean rainEnabled()
	{
		return true;
	}

	@ConfigItem(
			position = 9,
			keyName = "raincolor",
			name = "Rain Color",
			description = "The color of the rain",
			section = RainStormEffectsSettings
	)
	default Color rainColor()
	{
		return new Color(136, 151, 240);
	}

	@ConfigItem(
			position = 10,
			keyName = "rainthickness",
			name = "Rain Thickness",
			description = "The thickness of the rain",
			section = RainStormEffectsSettings
	)
	default int rainThickness()
	{
		return 1;
	}

	@ConfigItem(
			position = 11,
			keyName = "rainlength",
			name = "Rain Length",
			description = "The length of the rain",
			section = RainStormEffectsSettings
	)
	default int rainLength()
	{
		return 15;
	}

	@ConfigItem(
			position = 12,
			keyName = "rainwind",
			name = "Rain Wind Speed",
			description = "The wind affecting the rain",
			section = RainStormEffectsSettings
	)
	default int rainWind()
	{
		return 2;
	}

	@ConfigItem(
			position = 13,
			keyName = "raingravity",
			name = "Rain Gravity",
			description = "The speed of the rain",
			section = RainStormEffectsSettings
	)
	default int rainGravity()
	{
		return 8;
	}

	@ConfigItem(
			position = 14,
			keyName = "raindiv",
			name = "Rain Dither",
			description = "The dither of the rain (zig-zagging)",
			section = RainStormEffectsSettings
	)
	default int rainDiv()
	{
		return 4;
	}


	@ConfigSection(name = "Wintry Effects", description = "Wintry effects", position = 4, closedByDefault = false)
	String WintryEffectsSettings = "WintryEffectsSettings";

	// SNOW
	@ConfigItem(
			position = 15,
			keyName = "snowenabled",
			name = "Snow Enabled",
			description = "Is it snowing?",
			section = WintryEffectsSettings
	)
	default boolean snowEnabled()
	{
		return false;
	}

	@ConfigItem(
			position = 16,
			keyName = "snowcolor",
			name = "Snow Color",
			description = "The color of the snow",
			section = WintryEffectsSettings
	)
	default Color snowColor()
	{
		return new Color(255, 255, 255);
	}

	@ConfigItem(
			position = 17,
			keyName = "snowthickness",
			name = "Snow Thickness",
			description = "The thickness of the snow",
			section = WintryEffectsSettings
	)
	default int snowThickness()
	{
		return 3;
	}

	@ConfigItem(
			position = 18,
			keyName = "snowwind",
			name = "Snow Wind Speed",
			description = "The wind affecting the snow",
			section = WintryEffectsSettings
	)
	default int snowWind()
	{
		return 2;
	}

	@ConfigItem(
			position = 19,
			keyName = "snowgravity",
			name = "Snow Gravity",
			description = "The speed of the snow",
			section = WintryEffectsSettings
	)
	default int snowGravity()
	{
		return 3;
	}

	@ConfigItem(
			position = 20,
			keyName = "snowdiv",
			name = "Snow Dither",
			description = "The dither of the snow (zig-zagging)",
			section = WintryEffectsSettings
	)
	default int snowDiv()
	{
		return 3;
	}

	@ConfigSection(name = "Location Matching", description = "Location matching", position = 5, closedByDefault = false)
	String LocationMatchingSettings = "LocationMatchingSettings";

	// Location Weather
	@ConfigItem(
			position = 21,
			keyName = "locationenabled",
			name = "Real World Location Matching",
			description = "Match the weather to a real world location?",
			section = LocationMatchingSettings
	)
	default boolean locationEnabled()
	{
		return false;
	}

	@ConfigItem(
			position = 22,
			keyName = "location",
			name = "Real World Location",
			description = "City name or Area name, <br>eg. London, UK <br>Changes take effect on click elsewhere",
			section = LocationMatchingSettings
	)
	default String location()
	{
		return "";
	}

	@ConfigItem(
			position = 23,
			keyName = "apiKey",
			name = "OpenWeatherMap API Key",
			description = "Required for locational weather. <br>Create free key at openweathermap.org/api",
			section = LocationMatchingSettings
	)
	default String apiKey()
	{
		return "";
	}
}
