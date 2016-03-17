package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.explosion.Explosion;

public class PlayerBreakBlockListener
{
	@Listener
	public void onPlayerBreakBlock(ChangeBlockEvent.Break event, @First Player player)
	{
		for (Transaction<BlockSnapshot> transaction : event.getTransactions())
		{
			String isClaimed = ConfigManager.isClaimed(transaction.getFinal().getLocation().get());

			if (!isClaimed.equals("false"))
			{
				if ((isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && player.hasPermission("polis.claim.admin.modify"))
				{
					return;
				}

				if (!(isClaimed.equals("SafeZone") || isClaimed.equals("WarZone")) && Polis.adminBypassMode.contains(player.getUniqueId()))
				{
					return;
				}

				String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

				if (!event.getCause().first(Explosion.class).isPresent())
				{
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
	}
}
