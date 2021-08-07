package com.rlweather;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.Color;

@ConfigGroup("Weather")
public interface RlweatherConfig extends Config
{
	@ConfigItem(
		keyName = "greeting",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello";
	}

	@ConfigItem(
			keyName = "greenscreencolor",
			name = "Greenscreen Color",
			description = "The color of the greenscreen"
	)
	default Color greenscreenColor()
	{
		return new Color(41, 244, 24);
	}

	@ConfigItem(
			keyName = "color",
			name = "Rain Color",
			description = "The color of the rain"
	)
	default Color rainColor()
	{
		return new Color(0, 0, 244);
	}

	// TODO make this work
	@ConfigItem(
			keyName = "thickness",
			name = "Rain Thickness",
			description = "The thickness of the rain"
	)
	default Integer rainThickness()
	{
		return 3;
	}
}
