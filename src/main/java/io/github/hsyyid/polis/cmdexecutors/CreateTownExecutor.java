package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CreateTownExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (townName.equalsIgnoreCase("SafeZone"))
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You may not use the name SafeZone."));
				return CommandResult.success();
			}

			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName == null)
			{
				try
				{
					if (!ConfigManager.getTeams().contains(townName))
					{
						ConfigManager.addTeam(townName, player.getUniqueId().toString());
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Created town " + townName));
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "There is already a town with that name!"));
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
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /addtown!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /addtown!"));
		}

		return CommandResult.success();
	}
}
