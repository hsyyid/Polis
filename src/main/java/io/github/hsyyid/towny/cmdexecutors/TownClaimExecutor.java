package io.github.hsyyid.towny.cmdexecutors;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.towny.Towny;
import io.github.hsyyid.towny.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.ArrayList;
import java.util.Optional;

public class TownClaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			String playerTeamName = null;
			boolean playerIsAMember = false;

			for (String team : ConfigManager.getTeams())
			{
				ArrayList<String> uuids = ConfigManager.getMembers(team);

				if (uuids.contains(player.getUniqueId().toString()))
				{
					playerIsAMember = true;
					break;
				}
				else if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
				{
					playerTeamName = team;
					break;
				}
				else if (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()))
				{
					playerTeamName = team;
					break;
				}
			}

			if (playerTeamName != null)
			{
				Optional<Vector3i> optionalChunk = Towny.game.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());

				if (optionalChunk.isPresent())
				{
					Vector3i chunk = optionalChunk.get();
					
					if (!ConfigManager.isClaimed(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ()))
					{
						ConfigManager.claim(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
						player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.GOLD, "Successfully claimed this location!"));
					}
					else
					{
						player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is already claimed!"));
					}
				}
			}
			else if (playerIsAMember)
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader or an executive to claim!"));
			}
			else
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /townclaim!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /townclaim!"));
		}

		return CommandResult.success();
	}
}
