package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.UUIDFetcher;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.UUID;

public class PolisInfoExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (ConfigManager.getTeams().contains(townName))
		{
			src.sendMessage(Text.of(TextColors.GOLD, "Town: " + townName));

			try
			{
				src.sendMessage(Text.of(TextColors.BLUE, "The leader of ", TextColors.RED, townName, TextColors.BLUE, " is " + UUIDFetcher.getName(UUID.fromString(ConfigManager.getLeader(townName))).orElse(ConfigManager.getLeader(townName))));
			}
			catch (IllegalArgumentException e)
			{
				src.sendMessage(Text.of(TextColors.BLUE, "There is no leader of ", TextColors.RED, townName, TextColors.BLUE, "."));
			}

			src.sendMessage(Text.of(TextColors.DARK_PURPLE, "Total Balance: ", TextColors.LIGHT_PURPLE, ConfigManager.getBalance(townName)));

			try
			{
				src.sendMessage(Text.of(TextColors.DARK_GRAY, "Executives: ", TextColors.GRAY, ConfigManager.getExecutiveNames(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.DARK_GRAY, "Executives: ", TextColors.GRAY, "[]"));
			}

			try
			{
				src.sendMessage(Text.of(TextColors.DARK_BLUE, "Members: ", TextColors.BLUE, ConfigManager.getMemberNames(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.GRAY, "Members: ", TextColors.BLUE, "[]"));
			}

			try
			{
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Allies: ", TextColors.GREEN, ConfigManager.getAllies(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Allies: ", TextColors.GREEN, "[]"));
			}

			try
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Enemies: ", TextColors.RED, ConfigManager.getEnemies(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Enemies: ", TextColors.RED, "[]"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Polis specified does not exist!"));
		}

		return CommandResult.success();
	}
}
