package io.github.hsyyid.polis.config;

import io.github.hsyyid.polis.Polis;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the config.conf file
 */
public class Config implements Configurable
{
	private static Config config = new Config();

	private Config()
	{
		;
	}

	public static Config getConfig()
	{
		return config;
	}

	private Path configFile = Paths.get(Polis.getPolis().getConfigDir() + "/config.conf");
	private ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();
	private CommentedConfigurationNode configNode;

	@Override
	public void setup()
	{
		if (!Files.exists(configFile))
		{
			try
			{
				Files.createFile(configFile);
				load();
				populate();
				save();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			load();
		}
	}

	@Override
	public void load()
	{
		try
		{
			configNode = configLoader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void save()
	{
		try
		{
			configLoader.save(configNode);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void populate()
	{
		get().getNode("polis").setComment("Contains all Polis related settings.");
		get().getNode("polis", "name", "maxlength").setValue(30).setComment("This determines the maximum amount of characters a Polis's name can be.");
		get().getNode("polis", "name", "minlength").setValue(3).setComment("This determines the minimum amount of characters a Polis's name can be.");
		get().getNode("polis", "prefix", "display").setValue(true).setComment("Allows/denies displaying Polis prefixes.");
		get().getNode("polis", "claims", "cost").setValue(100.00).setComment("The amount of currency it costs per claim of chunk.");
		get().getNode("polis", "claims", "cap").setValue(50).setComment("The maximum number of claims a Polis may have.");
	}

	@Override
	public CommentedConfigurationNode get()
	{
		return configNode;
	}
}
