package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;
import java.util.Set;

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

				Utils.startTeamTaxService(teamName);
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set tax to " + tax + "."));
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
