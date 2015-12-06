package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.Invite;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.concurrent.TimeUnit;

public class InviteExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Game game = Polis.game;
		Player p = ctx.<Player> getOne("player").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			boolean isMember = false;
			String teamName = null;
			
			for (String team : ConfigManager.getTeams())
			{
				if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
				{
					teamName = team;
				}
				else if (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()))
				{
					isMember = true;
				}
				else if (ConfigManager.getMembers(team).contains(player.getUniqueId().toString()))
				{
					isMember = true;
				}
			}

			if (isMember)
			{
				src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not a leader of a Town! Please ask the leader of your town to invite " + p.getName()));
			}
			else if (teamName != null)
			{
				final Invite invite = new Invite(teamName, p.getUniqueId().toString());
				Polis.invites.add(invite);
				p.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, teamName + " has invited you to join! You have 2 minutes to do /polis join " + teamName + " to accept!"));

				Scheduler scheduler = game.getScheduler();
				Task.Builder taskBuilder = scheduler.createTaskBuilder();

				taskBuilder.execute(new Runnable()
				{
					public void run()
					{
						if (Polis.invites.contains(invite) && Polis.invites != null)
						{
							Polis.invites.remove(invite);
						}
					}
				}).delay(2, TimeUnit.MINUTES).name("Polis - remove invite").submit(game.getPluginManager().getPlugin("Polis").get().getInstance().get());

				src.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Successfully Invited " + p.getName()));
			}

		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /invite!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /invite!"));
		}

		return CommandResult.success();
	}
}
