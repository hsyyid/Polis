package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PolisLeaveExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			String playerTownName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTownName != null)
			{
				if (ConfigManager.getLeader(playerTownName).equals(player.getUniqueId().toString()))
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "You are the leader of town " + playerTownName + " to leave, you must appoint a new leader by doing /setleader"));
				}
				else if (ConfigManager.getExecutives(playerTownName).contains(player.getUniqueId()))
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Left town."));
					ConfigManager.removeExecutive(playerTownName, player.getUniqueId().toString());
				}
				else
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Left town."));
					ConfigManager.removeMember(playerTownName, player.getUniqueId().toString());
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not in a town!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /jointeam!"));
		}

		return CommandResult.success();
	}
}
