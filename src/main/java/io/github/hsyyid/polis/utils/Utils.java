package io.github.hsyyid.polis.utils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import io.github.hsyyid.polis.PluginInfo;
import io.github.hsyyid.polis.Polis;

public class Utils
{
	public static World pokemonWorld = null; //This is for Hiroku's server optimisation sshhh
	
	public static User getOfflinePlayer(String lastKnownName)
	{
		Optional<UserStorageService> optUserService = Sponge.getServiceManager().provide(UserStorageService.class);
		if (optUserService.isPresent())
		{
			Optional<User> optUser = optUserService.get().get(lastKnownName);
			if (optUser.isPresent())
				return optUser.get();
		}
		return null;
	}
	
	public static void startTaxService()
	{
		for (Object t : ConfigManager.getTeams())
		{
			final String teamName = String.valueOf(t);

			if (ConfigManager.areTaxesEnabled(teamName))
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

							UniqueAccount uniqueAccount = Polis.economyService.getOrCreateAccount(uuid).get();
							TransactionResult transactionResult = uniqueAccount.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(NamedCause.owner(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get())));

							if (Sponge.getServer().getPlayer(uuid).isPresent())
							{
								if (transactionResult.getResult() == ResultType.SUCCESS)
								{
									Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Paid Polis tax of " + ConfigManager.getTax(teamName)));
									ConfigManager.depositToTownBank(ConfigManager.getTax(teamName), teamName);
									Polis.economyService.getOrCreateAccount(teamName).get().deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(NamedCause.owner(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get())));
								}
								else
								{
									Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "Failed to pay Polis tax of " + ConfigManager.getTax(teamName)));
								}
							}
						}
					}
				}).name("Polis - Tax Collection for " + teamName).interval(ConfigManager.getTaxInterval(teamName), TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get().getInstance().get());
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
						UniqueAccount uniqueAccount = Polis.economyService.getOrCreateAccount(uuid).get();
						TransactionResult transactionResult = uniqueAccount.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(NamedCause.owner(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get())));

						if (Sponge.getServer().getPlayer(uuid).isPresent())
						{
							if (transactionResult.getResult() == ResultType.SUCCESS)
							{
								Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Paid Polis tax of " + ConfigManager.getTax(teamName)));
								ConfigManager.depositToTownBank(ConfigManager.getTax(teamName), teamName);
								Polis.economyService.getOrCreateAccount(teamName).get().deposit(Polis.economyService.getDefaultCurrency(), ConfigManager.getTax(teamName), Cause.of(NamedCause.owner(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get())));
							}
							else
							{
								Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "Failed to pay Polis tax of " + ConfigManager.getTax(teamName)));
							}
						}
					}
				}
			}).name("Polis - Tax Collection for " + team).interval(ConfigManager.getTaxInterval(team), TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get().getInstance().get());
		}
	}
	
	public static Text polisPrefix()
	{
		return Text.of(TextColors.GREEN, "[Polis] ");
	}
}
