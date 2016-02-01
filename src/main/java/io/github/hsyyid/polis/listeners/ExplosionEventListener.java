package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;

public class ExplosionEventListener
{
	@Listener
	public void onExplosion(ExplosionEvent.Pre event)
	{
		float radius = event.getExplosion().getRadius();

		for(int i = 0; i < radius; i++)
		{
			if (ConfigManager.isClaimed(event.getExplosion().getWorld().getLocation(event.getExplosion().getOrigin())).equals("SafeZone"))
			{
				event.setCancelled(true);
			}
		}
	}
}
