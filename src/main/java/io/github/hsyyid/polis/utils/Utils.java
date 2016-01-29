package io.github.hsyyid.polis.utils;

import io.github.hsyyid.polis.Polis;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.UniqueAccount;

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
					for (String memberUuid : ConfigManager.getMembers(teamName))
					{
						UUID uuid = UUID.fromString(memberUuid);
						
						if (Polis.economyService.getAccount(uuid).isPresent())
						{
							UniqueAccount uniqueAccount = Polis.economyService.getAccount(uuid).get();
							uniqueAccount.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(Sponge.getPluginManager().getPlugin("Polis").get()));
						}
					}
				}).name("Polis - Tax Collection for " + team).interval(ConfigManager.getTaxInterval(team), TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin("Polis").get().getInstance().get());
			}
		}
	}
}
