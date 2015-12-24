package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class ToggleAdminBypassExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (Polis.adminBypassMode.contains(player.getUniqueId()))
			{
				Polis.adminBypassMode.remove(player.getUniqueId());
				player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GRAY, "Toggled admin-byass mode ", TextColors.GOLD, "off."));
			}
			else
			{
				Polis.adminBypassMode.add(player.getUniqueId());
				player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GRAY, "Toggled admin-byass mode ", TextColors.GOLD, "on."));
			}
		}
		else
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must be a player to use /polis toggleadminbypass"));
		}

		return CommandResult.success();
	}
}
