package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

public class PlayerDamageEventListener
{
	@Listener
	public void onPlayerDamaged(DamageEntityEvent event, @First Player player)
	{
		String isClaimed = ConfigManager.isClaimed(event.getTargetEntity().getLocation());

		if (isClaimed.equals("SafeZone"))
		{
			event.setCancelled(true);
		}
	}
}
