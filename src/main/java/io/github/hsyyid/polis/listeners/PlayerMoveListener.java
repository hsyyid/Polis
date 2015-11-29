package io.github.hsyyid.polis.listeners;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class PlayerMoveListener
{
	@Listener
	public void onPlayerMove(DisplaceEntityEvent event)
	{
		if (event.getTargetEntity() instanceof Player)
		{
			Player player = (Player) event.getTargetEntity();

			Location<World> previousLocation = event.getFromTransform().getLocation();
			Location<World> newLocation = event.getToTransform().getLocation();

			if (Polis.autoClaim.contains(player.getUniqueId()) && ConfigManager.isClaimed(previousLocation).equals("false"))
			{
				String team = ConfigManager.getTeam(player.getUniqueId());
				
				if (team != null && (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()) || ConfigManager.getLeader(team).equals(player.getUniqueId().toString())))
				{
					Optional<Vector3i> optionalChunk = Polis.game.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());

					if (optionalChunk.isPresent())
					{
						Vector3i chunk = optionalChunk.get();
						ConfigManager.claim(team, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ());
						player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location!"));
					}
				}
			}

			if (!ConfigManager.isClaimed(previousLocation).equalsIgnoreCase(ConfigManager.isClaimed(newLocation)))
			{
				if (ConfigManager.isClaimed(newLocation).equalsIgnoreCase("false"))
				{
					player.sendMessage(Texts.of(TextColors.GOLD, "Now entering unclaimed land."));
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GOLD, "Now entering the land of: ", TextColors.GRAY, ConfigManager.isClaimed(newLocation)));
				}
			}
		}
	}
}
