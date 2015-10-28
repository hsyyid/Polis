package io.github.hsyyid.towny.listeners;

import org.spongepowered.api.text.Texts;

import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import io.github.hsyyid.towny.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;

public class PlayerBreakBlockListener
{
	@Listener
	public void onPlayerBreakBlock(ChangeBlockEvent.Break event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = (Player) event.getCause().first(Player.class).get();

			for (Transaction<BlockSnapshot> transaction : event.getTransactions())
			{
				String isClaimed = ConfigManager.isClaimed(transaction.getFinal().getLocation().get());
					
				if(!isClaimed.equals("false"))
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
						if(isClaimed.equals(playerTeamName))
						{
							;
						}
						else
						{
							event.setCancelled(true);
							player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
						}
					}
					else
					{
						event.setCancelled(true);
						player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));	
					}
				}
			}
		}
	}

}
