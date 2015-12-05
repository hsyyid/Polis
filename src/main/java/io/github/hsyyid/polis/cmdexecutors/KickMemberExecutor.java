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

public class KickMemberExecutor implements CommandExecutor
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
						ConfigManager.removeMember(team, player.getUniqueId().toString());
						p.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Successfully kicked out player " + player.getName()));
						player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "You have been kicked out of town " + team));
						return CommandResult.success();
					}
					else
					{
						src.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This person is not a member of your town!"));
					}
				}
				else
				{
					src.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not a leader of this town!"));
				}
			}
			src.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not in a town!"));
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /kickmember!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /kickmember!"));
		}

		return CommandResult.success();
	}
}
