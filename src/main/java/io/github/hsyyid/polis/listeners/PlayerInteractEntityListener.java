package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

public class PlayerInteractEntityListener
{
	@Listener
	public void onPlayerRightClick(InteractEntityEvent.Secondary event, @Root Player player)
	{
		String isClaimed = ConfigManager.isClaimed(event.getTargetEntity().getLocation());

		if (!isClaimed.equals("false"))
		{
			// Player has permission to interact with entity
			if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && player.hasPermission("polis.claim.admin.modify"))
			{
				return;
			}

			// Entity is usable
			if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && ConfigManager.canUseInSafeZone(event.getTargetEntity().getType().getId()))
			{
				return;
			}

			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName != null)
			{
				// Entity is protected by other Polis
				if (!isClaimed.equals(playerTeamName))
				{
					event.setCancelled(true);
					return;
				}
			}
			else
			{
				// Player has no Polis, so same thing applies.
				event.setCancelled(true);
				return;
			}
		}
	}

	@Listener
	public void onPlayerLeftClick(InteractEntityEvent.Primary event, @Root Player player)
	{
		String isClaimed = ConfigManager.isClaimed(event.getTargetEntity().getLocation());

		if (!isClaimed.equals("false"))
		{
			// It's SafeZone and the player doesn't have permission to interact with the entity
			if (isClaimed.equals("SafeZone") && (!player.hasPermission("polis.claim.admin.modify") && !ConfigManager.canUseInSafeZone(event.getTargetEntity().getType().getId())))
			{
				event.setCancelled(true);
				return;
			}
			// This entity is protected by another Polis.
			else if (!(event.getTargetEntity() instanceof Player) && ConfigManager.getTeam(player.getUniqueId()) != null && !ConfigManager.getTeam(player.getUniqueId()).equals(isClaimed))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
}
