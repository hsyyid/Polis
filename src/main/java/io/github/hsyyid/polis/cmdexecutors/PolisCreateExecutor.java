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

public class PolisCreateExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (townName.equalsIgnoreCase("SafeZone") || townName.equalsIgnoreCase("WarZone"))
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You may not use this name."));
				return CommandResult.success();
			}

			if (townName.length() > ConfigManager.getMaxNameLength())
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Your name is too long, the max amount of characters is ", ConfigManager.getMaxNameLength()));
				return CommandResult.success();
			}

			if (townName.length() < ConfigManager.getMinNameLength())
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Your name is too short, the required amount of characters is ", ConfigManager.getMinNameLength()));
				return CommandResult.success();
			}

			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName == null)
			{
				try
				{
					if (!ConfigManager.getTeams().contains(townName))
					{
						UniqueAccount account = Polis.economyService.getOrCreateAccount(player.getUniqueId()).get();
						TransactionResult result = account.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getPolisCreateCost(), Cause.of(NamedCause.source(player)));

						if (result.getResult() == ResultType.SUCCESS)
						{
							ConfigManager.addTeam(townName, player.getUniqueId().toString());
							player.sendMessage(Text.builder().append(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Created Polis " + townName + " for " + ConfigManager.getPolisCreateCost() + " ")).append(Polis.economyService.getDefaultCurrency().getPluralDisplayName()).build());
						}
						else
						{
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Could not create Polis " + townName + "! You do not have enough money to make a Polis."));
						}
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "There is already a Polis with that name!"));
					}
				}
				catch (NullPointerException e)
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Failed to create Town!"));
				}
			}
			else
			{
				player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are already in a town. You must leave or disband your town first."));
			}

		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis create!"));
		}

		return CommandResult.success();
	}
}
