package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.Utils;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PolisRemoveExecutiveExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String playerName = ctx.<String> getOne("player").get();
		User player = Utils.getOfflinePlayer(playerName);
		if (player == null)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Player not recognized"));
			return CommandResult.success();
		}
		
		if (src instanceof Player)
		{
			Player p = (Player) src;
			String foundTeam = ConfigManager.getTeam(player.getUniqueId());

			if (foundTeam != null && ConfigManager.getLeader(foundTeam).equals(p.getUniqueId().toString()))
			{
				if (ConfigManager.getExecutives(foundTeam).contains(player.getUniqueId().toString()))
				{
					ConfigManager.removeExecutive(foundTeam, player.getUniqueId().toString());
					ConfigManager.addTeamMember(foundTeam, player.getUniqueId().toString());
					p.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Successfully removed executive status from " + player.getName()));
					player.getCommandSource().get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "You have been demoted by the leader of " + foundTeam));
				}
				else
				{
					src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This person is not a executive of your town!"));
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not the leader of a town!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /removeexec!"));
		}

		return CommandResult.success();
	}
}
