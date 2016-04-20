package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import java.util.Map;

public class EntitySpawnListener
{
	@Listener
	public void onEntitySpawn(SpawnEntityEvent event)
	{
		for (Entity entity : event.getEntities())
		{
			if (ConfigManager.isClaimed(entity.getLocation()).equals("SafeZone") && entity instanceof Monster)
			{
				event.setCancelled(true);
				return;
			}

			if (ConfigManager.isClaimed(entity.getLocation()).equals("SafeZone"))
			{
				Map<EntityType, String> protectedMobs = ConfigManager.getMobs("safezone");

				if (protectedMobs.containsKey(entity.getType()))
				{
					String option = protectedMobs.get(entity.getType());

					if (option.equals("all"))
					{
						event.setCancelled(true);
						return;
					}
					else if (option.equals("player") && event.getCause().first(Player.class).isPresent())
					{
						event.setCancelled(true);
						return;
					}
					else if (option.equals("non-player") && !event.getCause().first(Player.class).isPresent())
					{
						event.setCancelled(true);
						return;
					}
				}
			}
			else if (ConfigManager.isClaimed(entity.getLocation()).equals("WarZone"))
			{
				Map<EntityType, String> protectedMobs = ConfigManager.getMobs("warzone");

				if (protectedMobs.containsKey(entity.getType()))
				{
					String option = protectedMobs.get(entity.getType());

					if (option.equals("all"))
					{
						event.setCancelled(true);
						return;
					}
					else if (option.equals("player") && event.getCause().first(Player.class).isPresent())
					{
						event.setCancelled(true);
						return;
					}
					else if (option.equals("non-player") && !event.getCause().first(Player.class).isPresent())
					{
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
