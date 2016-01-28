package io.github.hsyyid.polis.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

/**
 * Utility class to get all the config classes from one place
 */
public final class Configs
{
	private Configs()
	{
		;
	}

	public static CommentedConfigurationNode getConfig(Configurable config)
	{
		return config.get();
	}

	public static void saveConfig(Configurable config)
	{
		config.save();
	}

	public static void setValue(Configurable config, Object[] nodePath, Object value)
	{
		config.get().getNode(nodePath).setValue(value);
		config.save();
	}

	public static void removeChild(Configurable config, Object[] nodePath, Object child)
	{
		config.get().getNode(nodePath).removeChild(child);
		config.save();
	}
}
