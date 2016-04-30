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

public class PolisAllowBlockExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName != null && ConfigManager.getLeader(playerTeamName).equals(player.getUniqueId().toString()))
			{
				Polis.allowBlocks.put(player.getUniqueId(), playerTeamName);
				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Right click a block to toggle its usability."));
			}
			else
			{
				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "You are not the leader of a Polis!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "You must be a player to allow blocks!"));
		}

		return CommandResult.success();
	}
}
