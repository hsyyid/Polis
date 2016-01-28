package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PlayerDropItemListener
{
	@Listener
	public void onPlayerDropItem(DropItemEvent.Dispense event, @First Player player)
	{
		String isClaimed = ConfigManager.isClaimed(player.getLocation());

		if (!isClaimed.equals("false"))
		{
			if (isClaimed.equals("SafeZone") && player.hasPermission("polis.claim.admin.modify"))
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
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
					event.setCancelled(true);
					return;
				}
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
				event.setCancelled(true);
				return;
			}
		}
	}
}
