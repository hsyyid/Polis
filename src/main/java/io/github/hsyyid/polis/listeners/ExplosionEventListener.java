package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.explosion.Explosion;

public class ExplosionEventListener
{
	@Listener
	public void onExplosion(ExplosionEvent.Pre event)
	{
		if (event.getExplosion().getSourceExplosive().isPresent())
		{
			Location<World> location = event.getExplosion().getSourceExplosive().get().getLocation();

			if (ConfigManager.isClaimed(location).equals("SafeZone"))
			{
				event.setCancelled(true);
			}

			else if (ConfigManager.isClaimed(location).equals("WarZone"))
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
}
