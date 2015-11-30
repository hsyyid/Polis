package io.github.hsyyid.polis.cmdexecutors;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
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

				if (!ConfigManager.isClaimed(player.getLocation()).equals("false"))
				{
					ConfigManager.unclaim("SafeZone", player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully un-claimed this location!"));
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is not claimed!"));
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
