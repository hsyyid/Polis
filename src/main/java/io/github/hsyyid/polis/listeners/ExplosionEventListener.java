package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.explosion.Explosion;

public class ExplosionEventListener
{
	@Listener
	public void onExplosion(ExplosionEvent.Pre event)
	{
		if (ConfigManager.isClaimed(event.getExplosion().getWorld().getLocation(event.getExplosion().getOrigin())).equals("SafeZone"))
		{
			event.setCancelled(true);
		}
		else if (ConfigManager.isClaimed(event.getExplosion().getWorld().getLocation(event.getExplosion().getOrigin())).equals("WarZone"))
		{
			if (event.getExplosion().shouldBreakBlocks())
			{
				Explosion explosion = Sponge.getRegistry().createBuilder(Explosion.Builder.class)
					.from(event.getExplosion())
					.canCauseFire(false)
					.shouldBreakBlocks(false)
					.build();
				
				event.setExplosion(explosion);
			}
		}
	}
}
