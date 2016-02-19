package io.github.hsyyid.polis.cmdexecutors;

import com.flowpowered.math.vector.Vector3d;
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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class PolisHQExecutor implements CommandExecutor
{

	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			String townName = ConfigManager.getTeam(player.getUniqueId());

			if (townName != null)
			{
				if (ConfigManager.inConfig(townName))
				{
					if (player.getWorld().getName() == ConfigManager.getHQWorldName(townName))
					{
						Location<World> hq = new Location<World>(player.getWorld(), ConfigManager.getHQX(townName), ConfigManager.getHQY(townName), ConfigManager.getHQZ(townName));
						player.setLocation(hq);
						src.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Teleported to HQ"));
						return CommandResult.success();
					}
					else
					{
						Vector3d position = new Vector3d(ConfigManager.getHQX(townName), ConfigManager.getHQY(townName), ConfigManager.getHQZ(townName));
						player.transferToWorld(ConfigManager.getHQWorldName(townName), position);
						src.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Teleported to HQ"));
						return CommandResult.success();
					}
				}
				else
				{
					src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Your team has no HQ set!"));
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not in a team!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /hq!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /hq!"));
		}
		return CommandResult.success();
	}
}
