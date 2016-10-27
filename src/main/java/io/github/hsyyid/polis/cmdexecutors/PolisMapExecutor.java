package io.github.hsyyid.polis.cmdexecutors;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Lists;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PolisMapExecutor implements CommandExecutor
{
	private final char[] mapChars = "\\/#?$%=&^ABCDEFGHJKLMNOPQRSTUVWXYZ1234567890abcdeghjmnopqrsuvwxyz".toCharArray();

	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			for (Text text : this.getMap(player, player.getLocation(), 48, 8))
			{
				player.sendMessage(text);
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must be an in-game player to use /polis map"));
		}

		return CommandResult.success();
	}

	public List<Text> getMap(Player observer, Location<World> center, int width, int height)
	{
		Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(center.getBlockPosition());
		List<Text> ret = Lists.newArrayList();

		if (optionalChunk.isPresent())
		{
			Vector3i chunk = optionalChunk.get();
			String playerPolis = ConfigManager.getTeam(observer.getUniqueId());

			if (playerPolis == null)
				playerPolis = "";

			ret.add(Text.of(TextColors.GOLD, "(" + chunk.getX() + "," + chunk.getZ() + ") " + playerPolis));

			int halfWidth = width / 2;
			int halfHeight = height / 2;
			width = halfWidth * 2 + 1;
			height = halfHeight * 2 + 1;

			Vector3i topLeftPs = chunk.add(-halfWidth, 0, -halfHeight);
			height--;

			Map<String, Character> polisList = new HashMap<>();
			int chrIdx = 0;

			for (int dz = 0; dz < height; dz++)
			{
				Text.Builder row = Text.builder();

				for (int dx = 0; dx < width; dx++)
				{
					if (dx == halfWidth && dz == halfHeight)
					{
						row.append(Text.of(TextColors.AQUA, "+"));
						continue;
					}

					Vector3i chunkAtLocation = topLeftPs.add(dx, 0, dz);
					String polisAtLocation = ConfigManager.isClaimed(new Location<World>(center.getExtent(), chunkAtLocation.getX(), chunkAtLocation.getY(), chunkAtLocation.getZ()));

					if (polisAtLocation == null)
					{
						row.append(Text.of(TextColors.GRAY, "-"));
					}
					else
					{
						if (!polisList.containsKey(polisAtLocation))
							polisList.put(polisAtLocation, mapChars[chrIdx++]);

						char polisChar = polisList.get(polisAtLocation);
						row.append(Text.of(ConfigManager.getColorTo(playerPolis, polisAtLocation), "" + polisChar));
					}
				}

				ret.add(row.build());
			}

			Text.Builder row = Text.builder();

			for (String polis : polisList.keySet())
			{
				row.append(Text.of(ConfigManager.getColorTo(playerPolis, polis), polisList.get(polis) + ": " + polis, " "));
			}

			ret.add(row.build());
		}

		return ret;
	}
}
