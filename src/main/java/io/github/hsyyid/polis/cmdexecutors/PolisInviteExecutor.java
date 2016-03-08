package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.PluginInfo;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.Invite;
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

import java.util.concurrent.TimeUnit;

public class PolisInviteExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Player p = ctx.<Player> getOne("player").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;
			String teamName = ConfigManager.getTeam(player.getUniqueId());

			if (teamName != null && !ConfigManager.getMembers(teamName).contains(player.getUniqueId().toString()))
			{
				final Invite invite = new Invite(teamName, p.getUniqueId().toString());
				Polis.invites.add(invite);
				p.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, teamName + " has invited you to join! You have 2 minutes to do ", TextColors.GRAY, "/polis join ", TextColors.RED, teamName, TextColors.GOLD, " to accept!"));

				Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();

				taskBuilder.execute(new Runnable()
				{
					public void run()
					{
						if (Polis.invites.contains(invite) && Polis.invites != null)
						{
							Polis.invites.remove(invite);
						}
					}
				}).delay(2, TimeUnit.MINUTES).name("Polis - remove invite").submit(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get().getInstance().get());

				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Successfully invited " + p.getName()));
			}
			else if (teamName != null)
			{
				src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not a leader of a Town! Please ask the leader of your town or an executive to invite " + p.getName()));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /p invite!"));
		}

		return CommandResult.success();
	}
}
