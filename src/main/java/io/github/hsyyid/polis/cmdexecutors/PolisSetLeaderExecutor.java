package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.Utils;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PolisSetLeaderExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String playerName = ctx.<String> getOne("player").get();
		User p = Utils.getOfflinePlayer(playerName);
		if (p == null)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Player not recognized"));
			return CommandResult.success();
		}
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;
			
			if (ConfigManager.getTeams().contains(townName))
			{
				if (ConfigManager.getLeader(townName).equals(player.getUniqueId().toString()) || player.hasPermission("polis.setleader.admin"))
				{
					try
					{
						if (ConfigManager.getMembers(townName).contains(p.getUniqueId().toString()))
						{
							ConfigManager.addTeamMember(townName, ConfigManager.getLeader(townName));
							ConfigManager.setTeamLeader(townName, p.getUniqueId().toString());
							
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Set " + townName + " leader to " + p.getName()));
						}
						else
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Player is not a member of this town!"));
					}
					catch (NullPointerException e)
					{
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Failed to set town leader!"));
					}
				}
				else
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You aren't the leader of this town!"));
			}
			else
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Town does not exist!"));
		}
		else if (src instanceof ConsoleSource || src instanceof CommandBlockSource)
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /setleader!"));

		return CommandResult.success();
	}
}
