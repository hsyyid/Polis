package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class TownInfoExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;
			
			if (ConfigManager.getTeams().contains(townName))
			{
				player.sendMessage(Texts.of(TextColors.GOLD, "Town: " + townName));
				player.sendMessage(Texts.of(TextColors.BLUE, "The leader of ", TextColors.RED, townName, TextColors.BLUE, " is " + ConfigManager.getLeader(townName)));

				try
				{
					player.sendMessage(Texts.of(TextColors.DARK_GRAY, "Executives: ", TextColors.GRAY, ConfigManager.getExecutives(townName).toString()));
				}
				catch (NullPointerException e)
				{
					player.sendMessage(Texts.of(TextColors.DARK_GRAY, "Executives: ", TextColors.GRAY, "[]"));
				}

				try
				{
					player.sendMessage(Texts.of(TextColors.DARK_BLUE, "Members: ", TextColors.BLUE, ConfigManager.getMembers(townName).toString()));
				}
				catch (NullPointerException e)
				{
					player.sendMessage(Texts.of(TextColors.GRAY, "Members: ", TextColors.BLUE, "[]"));
				}

				try
				{
					player.sendMessage(Texts.of(TextColors.DARK_GREEN, "Allies: ", TextColors.GREEN, ConfigManager.getAllies(townName).toString()));
				}
				catch (NullPointerException e)
				{
					player.sendMessage(Texts.of(TextColors.DARK_GREEN, "Allies: ", TextColors.GREEN, "[]"));
				}

				try
				{
					player.sendMessage(Texts.of(TextColors.DARK_RED, "Enemies: ", TextColors.RED, ConfigManager.getEnemies(townName).toString()));
				}
				catch (NullPointerException e)
				{
					player.sendMessage(Texts.of(TextColors.DARK_RED, "Enemies: ", TextColors.RED, "[]"));
				}
			}
			else
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Town does not exist!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /towninfo!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /towninfo!"));
		}

		return CommandResult.success();
	}
}
