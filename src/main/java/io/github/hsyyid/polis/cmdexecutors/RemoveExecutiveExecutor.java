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

public class RemoveExecutiveExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Player player = ctx.<Player> getOne("player").get();

		if (src instanceof Player)
		{
			Player p = (Player) src;
			String foundTeam = null;

			for (String team : ConfigManager.getTeams())
			{
				if (ConfigManager.getLeader(team).equals(p.getUniqueId().toString()))
				{
					foundTeam = team;
					break;
				}
			}

			if (foundTeam != null)
			{
				if (ConfigManager.getExecutives(foundTeam).contains(player.getUniqueId().toString()))
				{
					ConfigManager.removeExecutive(foundTeam, player.getUniqueId().toString());
					p.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Successfully remove executive status from " + player.getName()));
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "You have been demoted by the leader of " + foundTeam));
					return CommandResult.success();
				}
				else
				{
					src.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This person is not a executive of your town!"));
				}
			}
			else
			{
				src.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not the leader of  a town!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /removeexec!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /removeexec!"));
		}

		return CommandResult.success();
	}
}
