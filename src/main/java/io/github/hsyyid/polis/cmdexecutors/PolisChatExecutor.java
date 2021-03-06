package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PolisChatExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (Polis.polisChat.contains(player.getUniqueId()))
			{
				Polis.polisChat.remove(player.getUniqueId());
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Toggled Polis chat " , TextColors.RED, "off."));
			}
			else if (ConfigManager.getTeam(player.getUniqueId()) != null)
			{
				Polis.polisChat.add(player.getUniqueId());
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Toggled Polis chat " , TextColors.GREEN, "on."));
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not in a team."));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis chat!"));
		}

		return CommandResult.success();
	}
}
