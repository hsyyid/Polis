package io.github.hsyyid.polis.cmdexecutors;

import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.UUIDFetcher;

public class PolisInfoExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		int numExecs = ConfigManager.getExecutives(townName).size();
		int numMembs = ConfigManager.getMembers(townName).size();
		int numAlly = ConfigManager.getAllies(townName).size() - 1; //- safezone
		int numEnemy = ConfigManager.getEnemies(townName).size() - 1; //- warzone
		
		if (ConfigManager.getTeams().contains(townName))
		{
			src.sendMessage(Text.of(TextColors.LIGHT_PURPLE, TextStyles.BOLD, townName, TextStyles.RESET, TextColors.GRAY, " [", ConfigManager.getClaims(townName) + "/" + ConfigManager.getClaimCapForSize(townName) + "]"));

			try
			{
				src.sendMessage(Text.of(TextColors.AQUA, "Leader: " + UUIDFetcher.getName(UUID.fromString(ConfigManager.getLeader(townName))).orElse(ConfigManager.getLeader(townName))));
			}
			catch (IllegalArgumentException e)
			{
				src.sendMessage(Text.of(TextColors.BLUE, "There is no leader of ", TextColors.RED, townName, TextColors.BLUE, "."));
			}
			
			try
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "Executives [" + numExecs + "] : ", TextColors.GRAY, ConfigManager.getExecutiveNames(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.YELLOW, "Executives [0] : ", TextColors.GRAY, "[]"));
			}

			try
			{
				src.sendMessage(Text.of(TextColors.BLUE, "Members [" + numMembs + "] : ", TextColors.BLUE, ConfigManager.getMemberNames(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.BLUE, "Members [0] : ", TextColors.BLUE, "[]"));
			}
			
			src.sendMessage(Text.of(TextColors.GOLD, "Balance: ", TextColors.GOLD, ConfigManager.getBalance(townName)));
			
			try
			{
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Allies [" + numAlly + "] : ", TextColors.GREEN, ConfigManager.getAllies(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Allies [0] : ", TextColors.GREEN, "[]"));
			}

			try
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Enemies [" + numEnemy + "] : ", TextColors.RED, ConfigManager.getEnemies(townName).toString()));
			}
			catch (NullPointerException e)
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Enemies [0] : ", TextColors.RED, "[]"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Polis specified does not exist!"));
		}

		return CommandResult.success();
	}
}
