package io.github.hsyyid.polis.cmdexecutors;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class PolisClaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName != null && !ConfigManager.getMembers(playerTeamName).contains(player.getUniqueId()))
			{
				Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());

				if (optionalChunk.isPresent())
				{
					Vector3i chunk = optionalChunk.get();

					if (ConfigManager.isClaimed(player.getLocation()).equals("false") && !ConfigManager.isClaimed(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ()))
					{
						if (ConfigManager.getClaims(playerTeamName) < ConfigManager.getClaimCap())
						{
							if (ConfigManager.getBalance(playerTeamName).compareTo(ConfigManager.getClaimCost()) >= 0)
							{
								TransactionResult transactionResult = null;
								Account account = Polis.economyService.getOrCreateAccount(playerTeamName).get();
								transactionResult = account.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getClaimCost(), Cause.of(NamedCause.source(player)));

								if (transactionResult.getResult() == ResultType.SUCCESS)
								{
									ConfigManager.claim(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), true);
									ConfigManager.withdrawFromTownBank(ConfigManager.getClaimCost(), playerTeamName);
									player.sendMessage(Text.builder().append(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location for " + ConfigManager.getClaimCost() + " "))
										.append(Polis.economyService.getDefaultCurrency().getPluralDisplayName()).build());
								}
								else if (transactionResult.getResult() == ResultType.ACCOUNT_NO_FUNDS)
								{
									player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Not enough funds! Deposit funds or setup taxes!"));
								}
								else
								{
									player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "An error occured while trying to withdraw from your Polis' bank."));
								}
							}
							else
							{
								player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Your Polis does not have enough funds to claim this land! Deposit funds soon!"));
							}
						}
						else
						{
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You already have the maximum number of claims!"));
						}
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is already claimed!"));
					}
				}
			}
			else if (playerTeamName != null)
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader or an executive to claim!"));
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis claim!"));
		}

		return CommandResult.success();
	}
}
