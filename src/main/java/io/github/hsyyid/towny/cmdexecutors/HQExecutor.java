package io.github.hsyyid.towny.cmdexecutors;

import io.github.hsyyid.towny.utils.ConfigManager;

import com.flowpowered.math.vector.Vector3d;
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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class HQExecutor implements CommandExecutor
{

	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			String townName = null;
			
			for (String team : ConfigManager.getTeams())
			{
				if (ConfigManager.getMembers(team).contains(player.getUniqueId().toString()) || ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
				{
					townName = team;
					break;
				}
			}

			if (townName != null)
			{
				if (ConfigManager.inConfig(townName))
				{
					if (player.getWorld().getName() == ConfigManager.getHQWorldName(townName))
					{
						Location<World> hq = new Location<World>(player.getWorld(), ConfigManager.getX(townName), ConfigManager.getY(townName), ConfigManager.getZ(townName));
						player.setLocation(hq);
						src.sendMessage(Texts.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Teleported to HQ"));
						return CommandResult.success();
					}
					else
					{
						Vector3d position = new Vector3d(ConfigManager.getX(townName), ConfigManager.getY(townName), ConfigManager.getZ(townName));
						player.transferToWorld(ConfigManager.getHQWorldName(townName), position);
						src.sendMessage(Texts.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Teleported to HQ"));
						return CommandResult.success();
					}
				}
				else
				{
					src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Your team has no HQ set!"));
				}
			}
			else
			{
				src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not in a team!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /hq!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /hq!"));
		}
		return CommandResult.success();
	}
}
