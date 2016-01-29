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
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;

public class TownDepositExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		double amount = ctx.<Double> getOne("amount").get();
		BigDecimal deposit = new BigDecimal(amount);

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (ConfigManager.getTeam(player.getUniqueId()) != null)
			{
				String townName = ConfigManager.getTeam(player.getUniqueId());
				UniqueAccount playerAccount = null;
				TransactionResult transactionResult = null;

				if (Polis.economyService.getAccount(player.getUniqueId()).isPresent())
					playerAccount = Polis.economyService.getAccount(player.getUniqueId()).get();
				else
					playerAccount = Polis.economyService.createAccount(player.getUniqueId()).get();

				if (Polis.economyService.getAccount(townName).isPresent())
					transactionResult = playerAccount.transfer(Polis.economyService.getAccount(townName).get(), Polis.economyService.getDefaultCurrency(), deposit, Cause.of(player));
				else
					transactionResult = playerAccount.transfer(Polis.economyService.createVirtualAccount(townName).get(), Polis.economyService.getDefaultCurrency(), deposit, Cause.of(player));

				if (transactionResult.getResult() == ResultType.SUCCESS)
				{
					ConfigManager.depositToTownBank(deposit, townName);
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully deposited " + deposit + " into " + townName + "'s account."));
				}
				else if (transactionResult.getResult() == ResultType.ACCOUNT_NO_SPACE)
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Could not deposit " + deposit + " into " + townName + "'s account, as it has no room."));
				}
				else
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Could not deposit " + deposit + " into " + townName + "'s account."));
				}
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis deposit!"));
		}

		return CommandResult.success();
	}
}
