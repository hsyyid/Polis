package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PlayerInteractEntityListener
{
	@Listener
	public void onPlayerRightClick(InteractEntityEvent.Secondary event, @First Player player)
	{
		String isClaimed = ConfigManager.isClaimed(event.getTargetEntity().getLocation());

		if (!isClaimed.equals("false"))
		{
			if (isClaimed.equals("SafeZone") && player.hasPermission("polis.claim.admin.modify"))
			{
				return;
			}

			if (isClaimed.equals("SafeZone") && ConfigManager.canUseInSafeZone(event.getTargetEntity().getType().getName()))
			{
				return;
			}

			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

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

	@Listener
	public void onPlayerLeftClick(InteractEntityEvent.Primary event, @First Player player)
	{
		if (event.getTargetEntity() instanceof Player)
		{
			Player target = (Player) event.getTargetEntity();
			String isClaimed = ConfigManager.isClaimed(event.getTargetEntity().getLocation());

			if (!isClaimed.equals("false"))
			{
				if (isClaimed.equals("SafeZone") && !player.hasPermission("polis.claim.admin.modify"))
				{
					event.setCancelled(true);
					return;
				}
			}

			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());
			String targetPlayerTeamName = ConfigManager.getTeam(target.getUniqueId());

			if (targetPlayerTeamName != null && playerTeamName != null && targetPlayerTeamName.equals(playerTeamName))
			{
				player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You cannot hurt people in your Polis."));
				target.sendMessage(Text.of(TextColors.DARK_RED, player.getName(), TextColors.RED, " tried to hurt you."));
				event.setCancelled(true);
			}
		}
	}
}
