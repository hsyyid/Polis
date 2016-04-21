package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
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
		event.filterEntities(entity -> {
			if (ConfigManager.isClaimed(entity.getLocation()).equals("SafeZone") && entity instanceof Monster)
			{
				return false;
			}

			if (ConfigManager.isClaimed(entity.getLocation()).equals("SafeZone"))
			{
				Map<EntityType, String> protectedMobs = ConfigManager.getMobs("safezone");

				if (protectedMobs.containsKey(entity.getType()))
				{
					String option = protectedMobs.get(entity.getType());

					if (option.equals("all"))
					{
						return false;
					}
					else if (option.equals("player") && event.getCause().first(Player.class).isPresent())
					{
						return false;
					}
					else if (option.equals("non-player") && !event.getCause().first(Player.class).isPresent())
					{
						return false;
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
						return false;
					}
					else if (option.equals("player") && event.getCause().first(Player.class).isPresent())
					{
						return false;
					}
					else if (option.equals("non-player") && !event.getCause().first(Player.class).isPresent())
					{
						return false;
					}
				}
			}

			return true;
		});
	}
}
