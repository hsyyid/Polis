package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

public class EntitySpawnListener
{
	@Listener
	public void onEntitySpawn(SpawnEntityEvent event)
	{
		for(Entity entity : event.getEntities())
		{
			if(ConfigManager.isClaimed(entity.getLocation()).equals("SafeZone") && entity instanceof Monster)
			{
				event.setCancelled(true);
			}
		}
	}
}
