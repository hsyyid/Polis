package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;

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
				if(isClaimed.equals("SafeZone") && player.hasPermission("polis.claim.admin.modify"))
				{
					return;
				}
				
				if(isClaimed.equals("SafeZone") && ConfigManager.canUseInSafeZone(event.getTargetEntity().getType().getName()))
				{
					return;
				}
				
				
				if(event.getTargetEntity() instanceof TileEntity && isClaimed.equals("SafeZone") && ConfigManager.canUseInSafeZone(((TileEntity) (event.getTargetEntity())).getBlock().getType().getId()))
				{
					return;
				}
				
				String playerTeamName = null;

				for (String team : ConfigManager.getTeams())
				{
					ArrayList<String> uuids = ConfigManager.getMembers(team);
					if (uuids.contains(player.getUniqueId().toString()))
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
						event.setCancelled(true);
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
						return;
					}
				}
				else
				{
					event.setCancelled(true);
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "This land is claimed."));
					return;
				}
			}
		}
	}
	
	@Listener
	public void onPlayerLeftClick(InteractEntityEvent.Primary event)
	{
		if (event.getCause().first(Player.class).isPresent())
		{
			Player player = event.getCause().first(Player.class).get();
			
			if(event.getTargetEntity() instanceof Player)
			{
				Player target = (Player) event.getTargetEntity();
				
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
					else if (ConfigManager.getExecutives(team).contains(target.getUniqueId().toString()))
					{
						playerTeamName = team;
						break;
					}
				}
				
				String targetPlayerTeamName = null;

				for (String team : ConfigManager.getTeams())
				{
					ArrayList<String> uuids = ConfigManager.getMembers(team);
					if (uuids.contains(target.getUniqueId().toString()))
					{
						targetPlayerTeamName = team;
						break;
					}
					else if (ConfigManager.getLeader(team).equals(target.getUniqueId().toString()))
					{
						targetPlayerTeamName = team;
						break;
					}
					else if (ConfigManager.getExecutives(team).contains(target.getUniqueId().toString()))
					{
						targetPlayerTeamName = team;
						break;
					}
				}
				
				if(targetPlayerTeamName != null && playerTeamName != null && targetPlayerTeamName.equals(playerTeamName))
				{
					player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You cannot hurt people in your team."));
					target.sendMessage(Text.of(TextColors.DARK_RED, player.getName(), TextColors.RED, "Tried to hurt you."));
					event.setCancelled(true);
				}
			}
		}
	}
}
