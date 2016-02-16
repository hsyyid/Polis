package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AdminAutoClaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String zone = ctx.<String> getOne("zone").get();

		if (!zone.equals("SafeZone") || !zone.equals("WarZone"))
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Zone name is not applicable."));
			return CommandResult.success();
		}

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (Polis.adminAutoClaim.containsKey(player.getUniqueId()))
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Admin Auto-Claim: ", TextColors.RED, "disabled."));
				Polis.adminAutoClaim.remove(player.getUniqueId());
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Admin Auto-Claim: ", TextColors.GREEN, "enabled."));
				Polis.adminAutoClaim.put(player.getUniqueId(), zone);
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis adminautoclaim!"));
		}

		return CommandResult.success();
	}
}
