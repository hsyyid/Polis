package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DropItemListener
{
	@Listener
	public void onPlayerDropItem(DropItemEvent.Dispense event, @Root Player player)
	{
		if (!ConfigManager.canPlayersDropItems())
		{
			final String playerPolisName = ConfigManager.getTeam(player.getUniqueId());
			event.getEntities().forEach(e -> {
				String isClaimed = ConfigManager.isClaimed(e.getLocation());

				if (!isClaimed.equals("false"))
				{
					if (playerPolisName == null || !playerPolisName.equals(isClaimed))
					{
						player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You cannot drop items in claimed areas."));
						event.setCancelled(true);
						return;
					}
				}
			});
		}
	}
}
