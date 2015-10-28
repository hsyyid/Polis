package io.github.hsyyid.towny.listeners;

import io.github.hsyyid.towny.utils.ConfigManager;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;

public class PlayerInteractEntityListener
{
	@Listener
	public void onPlayerRightClick(InteractEntityEvent.Secondary event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = event.getCause().first(Player.class).get();

			String isClaimed = ConfigManager.isClaimed(event.getTargetEntity().getLocation());

			if (!isClaimed.equals("false"))
			{
				String playerTeamName = null;

				for (String team : ConfigManager.getTeams())
				{
					ArrayList<String> uuids = ConfigManager.getMembers(team);
					if (uuids.contains(player.getUniqueId().toString()))
					{
						playerTeamName = team;
						break;
					}
					else if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
					{
						playerTeamName = team;
						break;
					}
				}

				if (playerTeamName != null)
				{
					if (!(isClaimed.equals(playerTeamName)))
					{
						event.setCancelled(true);
						player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
						return;
					}
				}
				else
				{
					event.setCancelled(true);
					player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
					return;
				}
			}
		}
	}
}
