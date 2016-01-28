package io.github.hsyyid.polis.cmdexecutors;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class TownUnclaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName != null && !ConfigManager.getMembers(playerTeamName).contains(player.getUniqueId().toString()))
			{
				Optional<Vector3i> optionalChunk = Polis.game.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());

				if (optionalChunk.isPresent())
				{
					Vector3i chunk = optionalChunk.get();

					if (ConfigManager.isClaimed(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ()))
					{
						TransactionResult transactionResult = null;
						Account account = Polis.economyService.getAccount("Polis " + playerTeamName).orElse(null);

						if (account != null)
							transactionResult = account.deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getClaimCost(), Cause.of(player));
						else
						{
							account = Polis.economyService.createVirtualAccount("Polis " + playerTeamName).get();
							account.deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getBalance(playerTeamName), Cause.of(player));
							transactionResult = account.deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getClaimCost(), Cause.of(player));
						}

						if (transactionResult.getResult() == ResultType.SUCCESS)
						{
							ConfigManager.unclaim(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
							ConfigManager.depositToTownBank(ConfigManager.getClaimCost(), playerTeamName);
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully unclaimed this location!"));
						}
						else if (transactionResult.getResult() == ResultType.ACCOUNT_NO_FUNDS)
						{
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Not enough funds!"));
						}
						else
						{
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "An error occured while trying to withdraw from your Polis' bank."));
						}
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is not claimed!"));
					}
				}
			}
			else if (playerTeamName != null)
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader or an executive to unclaim!"));
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /townunclaim!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /townunclaim!"));
		}

		return CommandResult.success();
	}
}
