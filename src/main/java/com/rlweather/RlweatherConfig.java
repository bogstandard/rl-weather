package com.rlweather;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.Color;

@ConfigGroup("Weather")
public interface RlweatherConfig extends Config
{
	// SOUND EFFECTS
	@ConfigItem(
			position = 1,
			keyName = "soundsenabled",
			name = "Weather Sounds Enabled (beta)",
			description = "Make the noises?"
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
			description = "Set volume level for sounds"
	)
	default int soundVolume()
	{
		return 25;
	}

	@ConfigItem(
			position = 3,
			keyName = "depthenabled",
			name = "Depth Enabled",
			description = "Simulate depth on the particles?"
	)
	default boolean depthEnabled()
	{
		return false;
	}

	// LIGHTNING
	@ConfigItem(
			position = 4,
			keyName = "lightningenabled",
			name = "Lightning Enabled",
			description = "Is it really horrendous out? (Lightning Flashes)"
	)
	default boolean lightningEnabled()
	{
		return false;
	}
	
	@ConfigItem(
			position = 5,
			keyName = "thunderenabled",
			name = "Thunder Enabled",
			description = "Is it really horrendous out? (Thunder Sounds)"
	)
	default boolean thunderEnabled()
	{
		return false;
	}

	@ConfigItem(
			position = 6,
			keyName = "lightningfrequency",
			name = "Lightning Frequency (WARNING BRIGHT FLASHES)",
			description = "The rough frequency of the lightning in ticks"
	)
	default int lightningFrequency()
	{
		return 100;
	}

	@ConfigItem(
			position = 7,
			keyName = "lightningcolor",
			name = "Lightning Color",
			description = "The color of the lightning"
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
			description = "Is it raining?"
	)
	default boolean rainEnabled()
	{
		return true;
	}

	@ConfigItem(
			position = 9,
			keyName = "raincolor",
			name = "Rain Color",
			description = "The color of the rain"
	)
	default Color rainColor()
	{
		return new Color(136, 151, 240);
	}

	@ConfigItem(
			position = 10,
			keyName = "rainthickness",
			name = "Rain Thickness",
			description = "The thickness of the rain"
	)
	default int rainThickness()
	{
		return 1;
	}

	@ConfigItem(
			position = 11,
			keyName = "rainlength",
			name = "Rain Length",
			description = "The length of the rain"
	)
	default int rainLength()
	{
		return 15;
	}

	@ConfigItem(
			position = 12,
			keyName = "rainwind",
			name = "Rain Wind Speed",
			description = "The wind affecting the rain"
	)
	default int rainWind()
	{
		return 2;
	}

	@ConfigItem(
			position = 13,
			keyName = "raingravity",
			name = "Rain Gravity",
			description = "The speed of the rain"
	)
	default int rainGravity()
	{
		return 8;
	}

	@ConfigItem(
			position = 14,
			keyName = "raindiv",
			name = "Rain Dither",
			description = "The dither of the rain (zig-zagging)"
	)
	default int rainDiv()
	{
		return 4;
	}


	// SNOW
	@ConfigItem(
			position = 15,
			keyName = "snowenabled",
			name = "Snow Enabled",
			description = "Is it snowing?"
	)
	default boolean snowEnabled()
	{
		return false;
	}

	@ConfigItem(
			position = 16,
			keyName = "snowcolor",
			name = "Snow Color",
			description = "The color of the snow"
	)
	default Color snowColor()
	{
		return new Color(255, 255, 255);
	}

	@ConfigItem(
			position = 17,
			keyName = "snowthickness",
			name = "Snow Thickness",
			description = "The thickness of the snow"
	)
	default int snowThickness()
	{
		return 3;
	}

	@ConfigItem(
			position = 18,
			keyName = "snowwind",
			name = "Snow Wind Speed",
			description = "The wind affecting the snow"
	)
	default int snowWind()
	{
		return 2;
	}

	@ConfigItem(
			position = 19,
			keyName = "snowgravity",
			name = "Snow Gravity",
			description = "The speed of the snow"
	)
	default int snowGravity()
	{
		return 3;
	}

	@ConfigItem(
			position = 20,
			keyName = "snowdiv",
			name = "Snow Dither",
			description = "The dither of the snow (zig-zagging)"
	)
	default int snowDiv()
	{
		return 3;
	}

	// Location Weather
	@ConfigItem(
			position = 21,
			keyName = "locationenabled",
			name = "Real World Location Matching",
			description = "Match the weather to a real world location?"
	)
	default boolean locationEnabled()
	{
		return false;
	}

	@ConfigItem(
			position = 22,
			keyName = "location",
			name = "Real World Location",
			description = "City name or Area name, <br>eg. London, UK <br>Changes take effect on click elsewhere"
	)
	default String location()
	{
		return "";
	}

	@ConfigItem(
			position = 23,
			keyName = "apiKey",
			name = "OpenWeatherMap API Key",
			description = "Required for locational weather. <br>Create free key at openweathermap.org/api"
	)
	default String apiKey()
	{
		return "";
	}
}
