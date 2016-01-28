package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;

public class PlayerDamageEventListener
{
	@Listener
	public void onPlayerDamaged(DamageEntityEvent event)
	{
		if (event.getTargetEntity().getType() == EntityTypes.PLAYER)
		{
			String isClaimed = ConfigManager.isClaimed(event.getTargetEntity().getLocation());

			if (isClaimed.equals("SafeZone"))
			{
				event.setCancelled(true);
			}
		}
	}
}
