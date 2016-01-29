package io.github.hsyyid.polis.cmdexecutors;

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
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SetTaxExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		double tax = ctx.<Double> getOne("tax").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;
			String teamName = ConfigManager.getTeam(player.getUniqueId());

			if (teamName != null && ConfigManager.getLeader(teamName).equals(player.getUniqueId().toString()))
			{
				ConfigManager.setTax(teamName, new BigDecimal(tax));
				Set<Task> tasks = Sponge.getScheduler().getTasksByName("Polis - Tax Collection for " + teamName);
				
				for (Task task : tasks)
				{
					task.cancel();
				}
				
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
				}).name("Polis - Tax Collection for " + teamName).interval(ConfigManager.getTaxInterval(teamName), TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin("Polis").get().getInstance().get());
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You aren't the leader of a town!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis settax!"));
		}

		return CommandResult.success();
	}
}
