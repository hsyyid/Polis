package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ChatListener
{
	@Listener
	public void onMessage(MessageChannelEvent.Chat event, @First Player player)
	{
		if (ConfigManager.displayPrefix())
		{
			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());
			boolean playerIsMember = false;
			boolean playerIsExecutive = false;

			if (playerTeamName != null)
			{
				if (playerIsMember)
				{
					event.setMessage(Text.builder()
						.append(Text.of(TextColors.GRAY, "[", TextColors.GOLD, playerTeamName, TextColors.GRAY, "] "))
						.append(event.getMessage().orElse(Text.of()))
						.build());
				}
				else if (playerIsExecutive)
				{
					event.setMessage(Text.builder()
						.append(Text.of(TextColors.GREEN, "+", TextColors.GRAY, "[", TextColors.GOLD, playerTeamName, TextColors.GRAY, "] "))
						.append(event.getMessage().orElse(Text.of()))
						.build());
				}
				else
				{
					event.setMessage(Text.builder()
						.append(Text.of(TextColors.GREEN, "*", TextColors.GRAY, "[", TextColors.GOLD, playerTeamName, TextColors.GRAY, "] "))
						.append(event.getMessage().orElse(Text.of()))
						.build());
				}
			}
		}
	}
}
