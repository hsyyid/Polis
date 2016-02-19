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

public class PolisKickMemberExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Player player = ctx.<Player> getOne("player").get();

		if (src instanceof Player)
		{
			Player p = (Player) src;
			String playerTownName = ConfigManager.getTeam(player.getUniqueId());
			String pTownName = ConfigManager.getTeam(p.getUniqueId());

			if (pTownName != null)
			{
				if ((ConfigManager.getExecutives(pTownName).contains(p.getUniqueId()) || ConfigManager.getLeader(pTownName).equals(p.getUniqueId().toString()))
					&& pTownName.equals(playerTownName))
				{
					ConfigManager.removeMember(playerTownName, player.getUniqueId().toString());
					p.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Successfully kicked out player " + player.getName()));
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "You have been kicked out of town " + playerTownName));
					return CommandResult.success();
				}
				else if (pTownName.equals(playerTownName))
				{
					src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not a leader of this town!"));
				}
				else
				{
					src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This person is not a member of your town!"));
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not in a town!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /kickmember!"));
		}

		return CommandResult.success();
	}
}
