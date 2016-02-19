package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PolisRemoveAllyExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (ConfigManager.getTeams().contains(townName))
			{
				String playerTeamName = ConfigManager.getTeam(player.getUniqueId());
				boolean playerIsAMember = false;

				if (playerTeamName != null)
				{
					try
					{
						if (ConfigManager.getAllies(playerTeamName) != null && ConfigManager.getAllies(playerTeamName).contains(townName))
						{
							ConfigManager.removeAlly(playerTeamName, townName);
							ConfigManager.removeAlly(townName, playerTeamName);
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully removed town ", TextColors.GREEN, townName, TextColors.GOLD, " as an ally!"));
							return CommandResult.success();
						}
						else
						{
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not allies!"));
							return CommandResult.success();
						}
					}
					catch (NullPointerException e)
					{
						;
					}
				}
				else if (playerIsAMember)
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader to remove allies!"));
				}
				else
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
				}
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Town does not exist!"));
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /removeally!"));
		}

		return CommandResult.success();
	}
}
