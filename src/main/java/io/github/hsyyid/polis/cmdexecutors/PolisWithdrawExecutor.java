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
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;

public class PolisWithdrawExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		double amount = ctx.<Double> getOne("amount").get();
		BigDecimal withdrawAmt = new BigDecimal(amount);

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (ConfigManager.getTeam(player.getUniqueId()) != null && ConfigManager.getLeader(ConfigManager.getTeam(player.getUniqueId())).equals(player.getUniqueId().toString()))
			{
				String townName = ConfigManager.getTeam(player.getUniqueId());
				UniqueAccount playerAccount = Polis.economyService.getOrCreateAccount(player.getUniqueId()).get();
				TransactionResult transactionResult = Polis.economyService.getOrCreateAccount(townName).get().transfer(playerAccount, Polis.economyService.getDefaultCurrency(), withdrawAmt, Cause.of(NamedCause.source(player)));

				if (transactionResult.getResult() == ResultType.SUCCESS)
				{
					ConfigManager.withdrawFromTownBank(withdrawAmt, townName);
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully withdrew " + withdrawAmt + " from " + townName + "'s account."));
				}
				else if (transactionResult.getResult() == ResultType.ACCOUNT_NO_SPACE)
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Could not deposit " + withdrawAmt + " into " + townName + "'s account, as it has no room."));
				}
				else
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Could not deposit " + withdrawAmt + " into " + townName + "'s account."));
				}
			}
			else if (ConfigManager.getTeam(player.getUniqueId()) != null)
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Only your Polis leader can withdraw money."));
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not in a Polis."));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis withdraw!"));
		}

		return CommandResult.success();
	}
}
