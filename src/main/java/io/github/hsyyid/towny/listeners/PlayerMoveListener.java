package io.github.hsyyid.towny.listeners;

import io.github.hsyyid.towny.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class PlayerMoveListener
{
	@Listener
	public void onPlayerMove(DisplaceEntityEvent event)
	{
		if (event.getTargetEntity() instanceof Player)
		{
			Player player = (Player) event.getTargetEntity();

			Location<World> previousLocation = event.getFromTransform().getLocation();
			Location<World> newLocation = event.getToTransform().getLocation();

			if (!ConfigManager.isClaimed(previousLocation).equalsIgnoreCase(ConfigManager.isClaimed(newLocation)))
			{
				if (ConfigManager.isClaimed(newLocation).equalsIgnoreCase("false"))
				{
					player.sendMessage(Texts.of(TextColors.GOLD, "Now entering unclaimed land."));
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GOLD, "Now entering the land of: ", TextColors.GRAY, ConfigManager.isClaimed(newLocation)));
				}
			}
		}
	}
}
