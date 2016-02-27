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
 * Handles the messages.conf file
 */
public class MessageConfig implements Configurable
{
	private static MessageConfig config = new MessageConfig();

	private MessageConfig()
	{
		;
	}

	public static MessageConfig getConfig()
	{
		return config;
	}

	private Path configFile = Paths.get(Polis.getPolis().getConfigDir() + "/messages.conf");
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
		get().getNode("polis", "messages").setComment("Contains all Polis messages. @t represents the player's team name.");
		get().getNode("polis", "messages", "claims", "unclaimed", "notification").setValue("&6Now entering unclaimed land.").setComment("This is the message displayed when a player enters an unclaimed chunk.");
		get().getNode("polis", "messages", "claims", "claimed", "notification").setValue("&6Now entering the land of: &8@t").setComment("This is the message displayed when a player enters a claimed chunk.");
		get().getNode("polis", "prefix", "leader").setValue("&a*&8[&6@t&8] ").setComment("This is the prefix for a Polis leader.");
		get().getNode("polis", "prefix", "executive").setValue("&a+&8[&6@t&8] ").setComment("This is the prefix for a Polis executive.");
		get().getNode("polis", "prefix", "member").setValue("&8[&6@t&8] ").setComment("This is the prefix for a Polis member.");
	}

	@Override
	public CommentedConfigurationNode get()
	{
		return configNode;
	}
}
