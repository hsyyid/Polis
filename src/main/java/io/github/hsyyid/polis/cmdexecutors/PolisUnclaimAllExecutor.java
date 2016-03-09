package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;

public class PolisUnclaimAllExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName != null && !ConfigManager.getMembers(playerTeamName).contains(player.getUniqueId().toString()))
			{
				BigDecimal money = ConfigManager.getClaimCost().multiply(new BigDecimal(ConfigManager.getClaims(playerTeamName)));
				Polis.economyService.getOrCreateAccount(playerTeamName).get().deposit(Polis.economyService.getDefaultCurrency(), money, Cause.of(NamedCause.source(player)));
				ConfigManager.depositToTownBank(money, playerTeamName);
				ConfigManager.removeClaims(playerTeamName);
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully removed all claims!"));
			}
			else if (playerTeamName != null)
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader to remove all claims!"));
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis unclaimall!"));
		}

		return CommandResult.success();
	}
}
