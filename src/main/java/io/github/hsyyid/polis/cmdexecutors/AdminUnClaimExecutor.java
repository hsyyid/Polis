package io.github.hsyyid.polis.cmdexecutors;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class AdminUnClaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			
			if(!ConfigManager.getTeams().contains("SafeZone"))
			{
				ConfigManager.addTeam("SafeZone", "");
			}
			
			Optional<Vector3i> optionalChunk = Polis.game.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());

			if (optionalChunk.isPresent())
			{
				Vector3i chunk = optionalChunk.get();

				if (ConfigManager.isClaimed(player.getLocation()).equals("SafeZone"))
				{
					ConfigManager.unclaim("SafeZone", player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully un-claimed this location!"));
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is not claimed by SafeZone!"));
				}
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
