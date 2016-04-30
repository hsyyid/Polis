package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class PlayerInteractListener
{
	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event, @First Player player)
	{
		Optional<Location<World>> location = event.getTargetBlock().getLocation();

		if (location.isPresent())
		{
			try
			{
				String isClaimed = ConfigManager.isClaimed(location.get());

				if (!isClaimed.equals("false"))
				{
					if (event instanceof InteractBlockEvent.Secondary && Polis.allowBlocks.containsKey(player.getUniqueId()) && isClaimed.equals(Polis.allowBlocks.get(player.getUniqueId())))
					{
						if (ConfigManager.getLeader(isClaimed).equals(player.getUniqueId().toString()))
						{
							boolean toggled = ConfigManager.allowBlock(isClaimed, event.getTargetBlock().getState().getType());
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, (toggled ? "Allowed " : "Disabled ") + event.getTargetBlock().getState().getType().getTranslation().get()));
							Polis.allowBlocks.remove(player.getUniqueId());
						}
						else
						{
							Polis.allowBlocks.remove(player.getUniqueId());
						}
					}
					else
					{
						if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && player.hasPermission("polis.claim.admin.modify"))
						{
							return;
						}

						if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && ConfigManager.canUseInSafeZone(event.getTargetBlock().getState().getType().getId()))
						{
							return;
						}

						if (Polis.adminBypassMode.contains(player.getUniqueId()))
						{
							return;
						}

						String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

						if (playerTeamName != null)
						{
							if (!isClaimed.equals(playerTeamName))
							{
								if (event instanceof InteractBlockEvent.Secondary && ConfigManager.isAllowedBlock(isClaimed, event.getTargetBlock().getState().getType()))
								{
									return;
								}
								else
								{
									event.setCancelled(true);
									return;
								}
							}
						}
						else
						{
							event.setCancelled(true);
							return;
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}
}
