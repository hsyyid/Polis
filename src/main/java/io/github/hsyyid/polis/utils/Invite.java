package io.github.hsyyid.polis.utils;

public class Invite
{
	String teamName;
	String playerUUID;

	public Invite(String teamName, String playerUUID)
	{
		this.teamName = teamName;
		this.playerUUID = playerUUID;
	}

	public String getTeamName()
	{
		return teamName;
	}

	public String getPlayerUUID()
	{
		return playerUUID;
	}
}
