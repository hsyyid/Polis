package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

public class PlayerInteractListener
{
	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event, @First Player player)
	{
		try
		{
			String isClaimed = ConfigManager.isClaimed(event.getTargetBlock().getLocation().get());

			if (!isClaimed.equals("false"))
			{
				if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && player.hasPermission("polis.claim.admin.modify"))
				{
					return;
				}

				if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && ConfigManager.canUseInSafeZone(event.getTargetBlock().getState().getType().getId()))
				{
					return;
				}

				if (Polis.adminBypassMode.contains(player.getUniqueId()))
				{
					return;
				}

				String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

				if (playerTeamName != null)
				{
					if (!(isClaimed.equals(playerTeamName)))
					{
						event.setCancelled(true);
						return;
					}
				}
				else
				{
					event.setCancelled(true);
					return;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
