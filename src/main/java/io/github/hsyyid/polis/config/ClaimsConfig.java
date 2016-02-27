package io.github.hsyyid.polis.config;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the claims.conf file
 */
public class ClaimsConfig implements Configurable
{
	private static ClaimsConfig config = new ClaimsConfig();

	private ClaimsConfig()
	{
		;
	}

	public static ClaimsConfig getConfig()
	{
		return config;
	}

	private Path configFile = Paths.get(Polis.getPolis().getConfigDir().resolve("data") + "/claims.conf");
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
		get().getNode("claims").setComment("This stores all the claims of the towns.");
		ConfigManager.transferClaimsConfig();
	}

	@Override
	public CommentedConfigurationNode get()
	{
		return configNode;
	}
}
