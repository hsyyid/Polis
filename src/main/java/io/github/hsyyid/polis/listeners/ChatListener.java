package io.github.hsyyid.polis.listeners;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.MessageSinkEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class ChatListener
{
	@Listener
	public void onMessage(MessageSinkEvent.Chat event)
	{
		if (event.getCause().first(Player.class).isPresent() && ConfigManager.getDisplayPrefix())
		{
			Player player = event.getCause().first(Player.class).get();

			String playerTeamName = null;
			boolean playerIsMember = false;
			boolean playerIsExecutive = false;

			for (String team : ConfigManager.getTeams())
			{
				if (ConfigManager.getMembers(team).contains(player.getUniqueId().toString()))
				{
					playerIsMember = true;
					playerTeamName = team;
					break;
				}
				else if (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()))
				{
					playerIsExecutive = true;
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
				if (playerIsMember)
				{
					event.setMessage(Texts.builder()
						.append(Texts.of(TextColors.GRAY, "[", TextColors.GOLD, playerTeamName, TextColors.GRAY, "] "))
						.append(event.getMessage())
						.build());
				}
				else if (playerIsExecutive)
				{
					event.setMessage(Texts.builder()
						.append(Texts.of(TextColors.GREEN, "+", TextColors.GRAY, "[", TextColors.GOLD, playerTeamName, TextColors.GRAY, "] "))
						.append(event.getMessage())
						.build());
				}
				else
				{
					event.setMessage(Texts.builder()
						.append(Texts.of(TextColors.GREEN, "*", TextColors.GRAY, "[", TextColors.GOLD, playerTeamName, TextColors.GRAY, "] "))
						.append(event.getMessage())
						.build());
				}
			}
		}
	}
}
