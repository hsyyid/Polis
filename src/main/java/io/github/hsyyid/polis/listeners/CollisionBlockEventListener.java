package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.CollideBlockEvent;

public class CollisionBlockEventListener
{
	@Listener
	public void onCollide(CollideBlockEvent event)
	{
		if (event.getCause().first(Player.class).isPresent() && event.getCause().first(Item.class).isPresent())
		{
			Player player = event.getCause().first(Player.class).get();
			Item item = event.getCause().first(Item.class).get();
			String isClaimed = ConfigManager.isClaimed(event.getTargetLocation());

			if (!isClaimed.equals("false"))
			{
				if (isClaimed.equals("SafeZone") && player.hasPermission("polis.claim.admin.modify"))
				{
					return;
				}

				if (Polis.adminBypassMode.contains(player.getUniqueId()))
				{
					return;
				}

				String playerTeamName = null;

				for (String team : ConfigManager.getTeams())
				{
					if (ConfigManager.getMembers(team).contains(player.getUniqueId().toString()))
					{
						playerTeamName = team;
						break;
					}
					else if (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()))
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
						item.remove();
					}
				}
				else
				{
					item.remove();
				}
			}
		}
	}
}
