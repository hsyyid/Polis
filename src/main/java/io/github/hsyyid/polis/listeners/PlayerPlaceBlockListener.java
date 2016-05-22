package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;

public class PlayerPlaceBlockListener
{
	@Listener
	public void onPlayerPlaceBlock(ChangeBlockEvent.Place event, @Root Player player)
	{
		for (Transaction<BlockSnapshot> transaction : event.getTransactions())
		{
			String isClaimed = ConfigManager.isClaimed(transaction.getFinal().getLocation().get());

			if (!isClaimed.equals("false"))
			{
				if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && player.hasPermission("polis.claim.admin.modify"))
				{
					return;
				}

				if (!(isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && Polis.adminBypassMode.contains(player.getUniqueId()))
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
	}
}
