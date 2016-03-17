package io.github.hsyyid.polis.cmdexecutors;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Sets;
import io.github.hsyyid.polis.Polis;
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

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class AdminClaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String zone = ctx.<String> getOne("zone").get();
		Optional<Integer> radius = ctx.<Integer> getOne("radius");

		if (!(zone.equals("SafeZone") || zone.equals("WarZone")))
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Zone name is not applicable."));
			return CommandResult.success();
		}

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (!ConfigManager.getTeams().contains(zone))
			{
				ConfigManager.addTeam(zone, "");
			}

			if (!radius.isPresent())
			{
				Text message = this.claimChunk(zone, player.getLocation()) ? Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location!") : Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This location is already claimed!");
				player.sendMessage(message);
			}
			else
			{
				Text message = this.claimChunksAroundLocation(player.getUniqueId(), zone, player.getLocation(), radius.get()) ? Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed chunks in radius!") : Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Claimed chunks, however some chunks in radius contain other claims. Toggle admin-bypass and re-execute this command to force-claim these locations.");
				player.sendMessage(message);
			}
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /adminclaim!"));
		}

		return CommandResult.success();
	}

	private boolean claimChunk(String zone, Location<World> location)
	{
		Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(location.getBlockPosition());

		if (optionalChunk.isPresent())
		{
			Vector3i chunk = optionalChunk.get();

			if (ConfigManager.isClaimed(location).equals("false"))
			{
				ConfigManager.claim(zone, location.getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
				return true;
			}
		}

		return false;
	}

	private boolean claimChunksAroundLocation(UUID uuid, String zone, Location<World> location, int radius)
	{
		Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(location.getBlockPosition());

		if (optionalChunk.isPresent())
		{
			Set<Vector3i> chunksToClaim = Sets.newHashSet();
			Vector3i centerChunk = optionalChunk.get();
			Vector3i topLeftPs = centerChunk.add(-radius, 0, -radius);
			
			for (int dz = 0; dz < radius * 2; dz++)
			{
				for (int dx = 0; dx < radius * 2; dx++)
				{
					Vector3i chunkAtLocation = topLeftPs.add(dx, 0, dz);
					chunksToClaim.add(chunkAtLocation);
				}
			}
			
			boolean successful = true;

			for (Vector3i chunk : chunksToClaim)
			{
				if (Sponge.getServer().getChunkLayout().isValidChunk(chunk))
				{
					if (ConfigManager.isClaimed(location).equals("false"))
					{
						ConfigManager.claim(zone, location.getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
					}
					else if (Polis.adminBypassMode.contains(uuid))
					{
						ConfigManager.unclaim(ConfigManager.isClaimed(location), location.getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
						ConfigManager.claim(zone, location.getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
					}
					else
					{
						successful = false;
					}
				}
			}

			return successful;
		}

		return false;
	}
}
