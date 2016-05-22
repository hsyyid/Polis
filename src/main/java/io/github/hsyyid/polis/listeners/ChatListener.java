package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;

import java.util.Objects;

public class ChatListener
{
	@Listener
	public void onMessage(MessageChannelEvent.Chat event, @Root Player player)
	{
		if (ConfigManager.displayPrefix())
		{
			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName != null)
			{
				boolean playerIsMember = ConfigManager.getMembers(playerTeamName).contains(player.getUniqueId().toString());
				boolean playerIsExecutive = ConfigManager.getExecutives(playerTeamName).contains(player.getUniqueId().toString());

				if (playerIsMember)
				{
					event.setMessage(Text.builder()
						.append(ConfigManager.getMemberPrefix(playerTeamName))
						.append(event.getMessage())
						.build());
				}
				else if (playerIsExecutive)
				{
					event.setMessage(Text.builder()
						.append(ConfigManager.getExecutivePrefix(playerTeamName))
						.append(event.getMessage())
						.build());
				}
				else
				{
					event.setMessage(Text.builder()
						.append(ConfigManager.getLeaderPrefix(playerTeamName))
						.append(event.getMessage())
						.build());
				}
			}
		}

		if (Polis.polisChat.contains(player.getUniqueId()))
		{
			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

			if (playerTeamName != null)
			{
				MutableMessageChannel messageChannel = MessageChannel.TO_PLAYERS.asMutable();
				messageChannel.getMembers().removeIf(f -> !Objects.equals(ConfigManager.getTeam(((Player) f).getUniqueId()), playerTeamName));
				event.setChannel(messageChannel);
				event.setMessage(Text.builder()
					.append(Text.of(TextColors.AQUA, "[Polis Chat]: "))
					.append(event.getMessage())
					.build());
			}
			else
			{
				Polis.polisChat.remove(player.getUniqueId());
			}
		}
	}
}
