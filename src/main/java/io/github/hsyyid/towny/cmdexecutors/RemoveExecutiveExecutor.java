package io.github.hsyyid.towny.cmdexecutors;

import io.github.hsyyid.towny.utils.ConfigManager;

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

import java.util.ArrayList;

public class RemoveExecutiveExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Player player = ctx.<Player> getOne("player").get();
		
		if (src instanceof Player)
		{
			Player p = (Player) src;
			
			for (String team : ConfigManager.getTeams())
			{
				ArrayList<String> uuids = ConfigManager.getMembers(team);
				String leader = ConfigManager.getLeader(team);
				
				if (leader.equals(p.getUniqueId().toString()))
				{
					if (uuids.contains(player.getUniqueId().toString()))
					{
						ConfigManager.removeExecutive(team, player.getUniqueId().toString());
						p.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.YELLOW, "Successfully remove executive status from " + player.getName()));
						player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.YELLOW, "You have been demoted by the leader of " + team));
						return CommandResult.success();
					}
					else
					{
						src.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This person is not a member of your town!"));
					}
				}
				else
				{
					src.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not a leader of this town!"));
				}
			}
			src.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not in a town!"));
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
