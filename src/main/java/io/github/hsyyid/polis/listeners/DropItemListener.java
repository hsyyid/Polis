package io.github.hsyyid.polis.listeners;

import com.google.common.collect.Lists;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class DropItemListener
{
	@Listener
	public void onPlayerDropItem(DropItemEvent.Dispense event, @First Player player)
	{
		if (!ConfigManager.canPlayersDropItems())
		{
			final String playerPolisName = ConfigManager.getTeam(player.getUniqueId());
			List<Item> itemsCancelled = Lists.newArrayList();

			event.filterEntities(e -> {
				String isClaimed = ConfigManager.isClaimed(e.getLocation());

				if (!isClaimed.equals("false"))
				{
					if (playerPolisName == null || !playerPolisName.equals(isClaimed))
					{
						itemsCancelled.add((Item) e);
						return false;
					}
				}

				return true;
			});

			if (!itemsCancelled.isEmpty())
			{
				player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You cannot drop items in claimed areas."));

				// Give the items back to the player that they failed to drop
				for (Item item : itemsCancelled)
				{
					player.getInventory().offer(item.getItemData().item().get().createStack());
				}
			}
		}
	}
}
