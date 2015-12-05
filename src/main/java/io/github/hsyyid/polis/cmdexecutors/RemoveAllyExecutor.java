package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;

public class RemoveAllyExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (ConfigManager.getTeams().contains(townName))
			{
				String playerTeamName = null;
				boolean playerIsAMember = false;

				for (String team : ConfigManager.getTeams())
				{
					ArrayList<String> uuids = ConfigManager.getMembers(team);
					if (uuids.contains(player.getUniqueId().toString()))
					{
						playerIsAMember = true;
						break;
					}
					else if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
					{
						playerTeamName = team;
						break;
					}
				}

				if (playerTeamName != null)
				{
					try
					{
						if (ConfigManager.getAllies(playerTeamName) != null && ConfigManager.getAllies(playerTeamName).contains(townName))
						{
							ConfigManager.removeAlly(playerTeamName, townName);
							ConfigManager.removeAlly(townName, playerTeamName);
							player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully removed town ", TextColors.GREEN, townName, TextColors.GOLD, " as an ally!"));
							return CommandResult.success();
						}
						else
						{
							player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not allies!"));
							return CommandResult.success();
						}
					}
					catch (NullPointerException e)
					{
						;
					}
				}
				else if (playerIsAMember)
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader to remove allies!"));
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
				}
			}
			else
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Town does not exist!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /removeally!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /removeally!"));
		}

		return CommandResult.success();
	}
}
