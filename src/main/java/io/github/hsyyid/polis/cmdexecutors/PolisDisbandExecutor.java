package io.github.hsyyid.polis.cmdexecutors;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.hsyyid.polis.utils.ConfigManager;

public class PolisDisbandExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Optional<String> optionalTownName = ctx.<String> getOne("town name");
		
		if (optionalTownName.isPresent() && (src instanceof ConsoleSource || ((Player)src).hasPermission("polis.disband.admin")))
		{
			if (ConfigManager.getTeams().contains(optionalTownName.get()))
			{
				ConfigManager.removeTeam(optionalTownName.get(), true);
				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully deleted " + optionalTownName.get() + "."));
			}
			else
			{
				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "No such town!")); 
			}
		}
		else
		{
			if (src instanceof Player)
			{
				Player player = (Player) src;
				String playerTeamName = ConfigManager.getTeam(player.getUniqueId());
				
				if (playerTeamName != null && ConfigManager.getLeader(playerTeamName).equals(player.getUniqueId().toString()))
				{
					ConfigManager.removeTeam(playerTeamName, true);
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully deleted town."));
				}
				else if (playerTeamName != null)
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader to delete your town!"));
				}
				else
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
				}
			}
			else if (src instanceof ConsoleSource)
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player or specify a town to disband!"));
			}
			else if (src instanceof CommandBlockSource)
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis disband!"));
			}
		}

		return CommandResult.success();
	}
}
