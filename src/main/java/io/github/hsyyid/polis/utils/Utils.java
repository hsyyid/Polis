package io.github.hsyyid.polis.utils;

import io.github.hsyyid.polis.Polis;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utils
{
	public static void startTaxService()
	{
		for (String team : ConfigManager.getTeams())
		{
			final String teamName = team;

			if (ConfigManager.areTaxesEnabled(team))
			{
				Sponge.getScheduler().createTaskBuilder().execute(() -> {
					ArrayList<String> townMembers = ConfigManager.getMembers(teamName);
					townMembers.addAll(ConfigManager.getExecutives(teamName));
					townMembers.add(ConfigManager.getLeader(teamName));

					for (String memberUuid : townMembers)
					{
						if (!memberUuid.equals(""))
						{
							UUID uuid = UUID.fromString(memberUuid);

							if (Polis.economyService.getAccount(uuid).isPresent())
							{
								UniqueAccount uniqueAccount = Polis.economyService.getAccount(uuid).get();
								TransactionResult transactionResult = uniqueAccount.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(Sponge.getPluginManager().getPlugin("Polis").get()));

								if (Sponge.getServer().getPlayer(uuid).isPresent())
								{
									if (transactionResult.getResult() == ResultType.SUCCESS)
									{
										Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Paid Polis tax of " + ConfigManager.getTax(teamName)));
										ConfigManager.depositToTownBank(ConfigManager.getTax(teamName), teamName);

										if (Polis.economyService.getAccount(teamName).isPresent())
											Polis.economyService.getAccount(teamName).get().deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(Sponge.getPluginManager().getPlugin("Polis").get()));
										else
											Polis.economyService.createVirtualAccount(teamName).get().deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(Sponge.getPluginManager().getPlugin("Polis").get()));
									}
									else
										Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "Failed to pay Polis tax of " + ConfigManager.getTax(teamName)));
								}
							}
						}
					}
				}).name("Polis - Tax Collection for " + team).interval(ConfigManager.getTaxInterval(team), TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin("Polis").get().getInstance().get());
			}
		}
	}

	public static void startTeamTaxService(String team)
	{
		final String teamName = team;

		if (ConfigManager.areTaxesEnabled(team))
		{
			Sponge.getScheduler().createTaskBuilder().execute(() -> {
				ArrayList<String> townMembers = ConfigManager.getMembers(teamName);
				townMembers.addAll(ConfigManager.getExecutives(teamName));
				townMembers.add(ConfigManager.getLeader(teamName));

				for (String memberUuid : townMembers)
				{
					if (!memberUuid.equals(""))
					{
						UUID uuid = UUID.fromString(memberUuid);

						if (Polis.economyService.getAccount(uuid).isPresent())
						{
							UniqueAccount uniqueAccount = Polis.economyService.getAccount(uuid).get();
							TransactionResult transactionResult = uniqueAccount.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(Sponge.getPluginManager().getPlugin("Polis").get()));

							if (Sponge.getServer().getPlayer(uuid).isPresent())
							{
								if (transactionResult.getResult() == ResultType.SUCCESS)
								{
									Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Paid Polis tax of " + ConfigManager.getTax(teamName)));
									ConfigManager.depositToTownBank(ConfigManager.getTax(teamName), teamName);

									if (Polis.economyService.getAccount(teamName).isPresent())
										Polis.economyService.getAccount(teamName).get().deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(Sponge.getPluginManager().getPlugin("Polis").get()));
									else
										Polis.economyService.createVirtualAccount(teamName).get().deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(Sponge.getPluginManager().getPlugin("Polis").get()));
								}
								else
									Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "Failed to pay Polis tax of " + ConfigManager.getTax(teamName)));
							}
						}
					}
				}
			}).name("Polis - Tax Collection for " + team).interval(ConfigManager.getTaxInterval(team), TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin("Polis").get().getInstance().get());
		}
	}
}
