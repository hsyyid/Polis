package io.github.hsyyid.polis.cmdexecutors;

import java.awt.Color;
import java.util.Optional;
import java.util.Set;

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

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Sets;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.config.ClaimsConfig;
import io.github.hsyyid.polis.utils.ConfigManager;

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
				claimChunksAroundLocation(player, zone, player.getLocation(), radius.get());
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
				ConfigManager.claim(zone, location.getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), true);
				return true;
			}
		}

		return false;
	}

	private void claimChunksAroundLocation(Player player, String zone, Location<World> location, int radius)
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
			int claimed = 0;
			for (Vector3i chunk : chunksToClaim)
			{
				if (Sponge.getServer().getChunkLayout().isValidChunk(chunk))
				{
					Location<World> thisLocation = new Location<World>(location.getExtent(), Sponge.getServer().getChunkLayout().toWorld(chunk).get());
					
					if (ConfigManager.isClaimed(thisLocation).equals("false"))
					{
						ConfigManager.claim(zone, location.getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), false);
						claimed++;
					}
					else if (Polis.adminBypassMode.contains(player.getUniqueId()))
					{
						ConfigManager.unclaim(ConfigManager.isClaimed(thisLocation), location.getExtent().getUniqueId(), chunk.getX(), chunk.getY());
						ConfigManager.claim(zone, location.getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), false);
						claimed++;
					}
					else if (ConfigManager.isClaimed(thisLocation).equals(zone))
						claimed++;
				}
			}
			player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, claimed + " have been marked for saving..."));
			ClaimsConfig.getConfig().save();
			player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", claimed, " total claims were successfully written to disc."));
			if (chunksToClaim.size() > claimed)
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, 
					"Claimed chunks, however ", chunksToClaim.size() - claimed, " chunks in radius contain other claims. Toggle admin-bypass and re-execute this command to force-claim these locations."));

		}
		else //Fairly sure this would be impossible but hey, why take the risk? Shouldn't fire since the only time the chunk would not exist is if the server runs this, but it already checks that in the caller
			player.sendMessage(Text.of(Color.RED + "You are not in a chunk, somehow"));
	}
}
