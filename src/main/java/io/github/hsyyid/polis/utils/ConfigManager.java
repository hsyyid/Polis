package io.github.hsyyid.polis.utils;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Lists;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.config.Config;
import io.github.hsyyid.polis.config.Configs;
import io.github.hsyyid.polis.config.Configurable;
import io.github.hsyyid.polis.config.TeamsConfig;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ConfigManager
{
	private static Configurable mainConfig = Config.getConfig();
	private static Configurable teamConfig = TeamsConfig.getConfig();

	public static ArrayList<String> getTeams()
	{
		try
		{
			ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams.teams").split("\\."));
			String list = valueNode.getString();

			ArrayList<String> teamsList = new ArrayList<String>();
			boolean finished = false;

			if (finished != true)
			{
				int endIndex = list.indexOf(",");

				if (endIndex != -1)
				{
					String substring = list.substring(0, endIndex);
					teamsList.add(substring);

					while (finished != true)
					{
						int startIndex = endIndex;
						endIndex = list.indexOf(",", startIndex + 1);

						if (endIndex != -1)
						{
							String substrings = list.substring(startIndex + 1, endIndex);
							teamsList.add(substrings);
						}
						else
						{
							finished = true;
						}
					}
				}
				else if (!list.equals(""))
				{
					teamsList.add(list);
					finished = true;
				}
			}

			return teamsList;
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}
	}

	public static String getTeam(UUID playerUUID)
	{
		for (String team : ConfigManager.getTeams())
		{
			if (ConfigManager.getMembers(team).contains(playerUUID.toString()))
			{
				return team;
			}
			else if (ConfigManager.getLeader(team).equals(playerUUID.toString()))
			{
				return team;
			}
			else if (ConfigManager.getExecutives(team).contains(playerUUID.toString()))
			{
				return team;
			}
		}

		return null;
	}

	public static String getHQWorldName(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".hq.world").split("\\."));
		return valueNode.getString();
	}

	public static double getHQX(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".hq.X").split("\\."));
		return valueNode.getDouble();
	}

	public static double getHQY(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".hq.Y").split("\\."));
		return valueNode.getDouble();
	}

	public static double getHQZ(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".hq.Z").split("\\."));
		return valueNode.getDouble();
	}

	public static boolean inConfig(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".hq.X").split("\\."));
		try
		{
			Object inConfig = valueNode.getValue();
			if (inConfig != null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}

	public static ArrayList<String> getMembers(String teamName)
	{
		try
		{
			ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".members").split("\\."));
			String list = valueNode.getString();

			ArrayList<String> membersList = new ArrayList<String>();
			boolean finished = false;

			// Add all homes to homeList
			if (finished != true)
			{
				int endIndex = list.indexOf(",");
				if (endIndex != -1)
				{
					String substring = list.substring(0, endIndex);
					membersList.add(substring);

					// If they Have More than 1
					while (finished != true)
					{
						int startIndex = endIndex;
						endIndex = list.indexOf(",", startIndex + 1);
						if (endIndex != -1)
						{
							String substrings = list.substring(startIndex + 1, endIndex);
							membersList.add(substrings);
						}
						else
						{
							finished = true;
						}
					}
				}
				else
				{
					membersList.add(list);
					finished = true;
				}
			}

			return membersList;
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}
	}

	public static ArrayList<String> getExecutives(String teamName)
	{
		try
		{
			ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".executives").split("\\."));
			String list = valueNode.getString();

			ArrayList<String> executivesList = new ArrayList<String>();
			boolean finished = false;

			if (finished != true)
			{
				int endIndex = list.indexOf(",");
				if (endIndex != -1)
				{
					String substring = list.substring(0, endIndex);
					executivesList.add(substring);

					// If they Have More than 1
					while (finished != true)
					{
						int startIndex = endIndex;
						endIndex = list.indexOf(",", startIndex + 1);
						if (endIndex != -1)
						{
							String substrings = list.substring(startIndex + 1, endIndex);
							executivesList.add(substrings);
						}
						else
						{
							finished = true;
						}
					}
				}
				else
				{
					executivesList.add(list);
					finished = true;
				}
			}

			return executivesList;
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}
	}

	public static void setHQ(String teamName, Location<World> hqLocation, String worldName)
	{
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "hq", "world" }, worldName);
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "hq", "X" }, hqLocation.getX());
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "hq", "Y" }, hqLocation.getY());
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "hq", "Z" }, hqLocation.getZ());
	}

	public static ArrayList<String> getAllies(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".allies").split("\\."));
		String list = valueNode.getString();

		ArrayList<String> alliesList = new ArrayList<String>();
		boolean finished = false;

		// Add all homes to homeList
		if (finished != true)
		{
			int endIndex = list.indexOf(",");
			if (endIndex != -1)
			{
				String substring = list.substring(0, endIndex);
				alliesList.add(substring);

				// If they Have More than 1
				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = list.indexOf(",", startIndex + 1);
					if (endIndex != -1)
					{
						String substrings = list.substring(startIndex + 1, endIndex);
						alliesList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else
			{
				alliesList.add(list);
				finished = true;
			}
		}

		return alliesList;
	}

	public static ArrayList<String> getEnemies(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".enemies").split("\\."));
		String list = valueNode.getString();

		ArrayList<String> enemyList = new ArrayList<String>();
		boolean finished = false;

		// Add all homes to homeList
		if (finished != true)
		{
			int endIndex = list.indexOf(",");
			if (endIndex != -1)
			{
				String substring = list.substring(0, endIndex);
				enemyList.add(substring);

				// If they Have More than 1
				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = list.indexOf(",", startIndex + 1);
					if (endIndex != -1)
					{
						String substrings = list.substring(startIndex + 1, endIndex);
						enemyList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else
			{
				enemyList.add(list);
				finished = true;
			}
		}

		return enemyList;
	}

	public static String getLeader(String teamName)
	{
		try
		{
			ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".leader").split("\\."));

			if (valueNode.getValue() != null)
				return valueNode.getString();
			else
				return "";
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public static void addTeam(String teamName, String leaderUUID)
	{
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "leader" }, leaderUUID);
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "members" }, "");
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "enemies" }, "");
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "allies" }, "");
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "executives" }, "");

		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams.teams").split("\\."));

		if (valueNode.getString() != null)
		{
			String items = valueNode.getString();
			if (items.contains(teamName + ","))
				;
			else
			{
				String formattedItem = (teamName + ",");
				Configs.setValue(teamConfig, valueNode.getPath(), items + formattedItem);
			}
		}
		else
		{
			Configs.setValue(teamConfig, valueNode.getPath(), teamName + ",");
		}
	}

	public static void addTeamMember(String teamName, String memberUUID)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".members").split("\\."));

		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();

			if (teams.contains(memberUUID + ","))
			{
				;
			}
			else
			{
				String formattedItem = (memberUUID + ",");
				Configs.setValue(teamConfig, valueNode.getPath(), teams + formattedItem);
			}
		}
		else
		{
			Configs.setValue(teamConfig, valueNode.getPath(), memberUUID + ",");
		}
	}

	public static void addEnemy(String teamName, String enemyTeamName, boolean addEnemy)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".enemies").split("\\."));

		try
		{
			if (valueNode.getString() != null || valueNode.getString().length() > 0)
			{
				String teams = valueNode.getString();

				if (teams.contains(enemyTeamName + ","))
				{
					;
				}
				else
				{
					String formattedItem = (enemyTeamName + ",");
					Configs.setValue(teamConfig, valueNode.getPath(), teams + formattedItem);
				}
			}
		}
		catch (NullPointerException e)
		{
			Configs.setValue(teamConfig, valueNode.getPath(), enemyTeamName + ",");
		}

		if (addEnemy)
		{
			ConfigManager.addEnemy(enemyTeamName, teamName, false);
		}
	}

	public static void addAlly(String teamName, String allyTeamName, boolean addAlly)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".allies").split("\\."));

		try
		{
			if (valueNode.getString() != null || valueNode.getString().length() > 0)
			{
				String teams = valueNode.getString();

				if (teams.contains(allyTeamName + ","))
				{
					;
				}
				else
				{
					String formattedItem = (allyTeamName + ",");
					Configs.setValue(teamConfig, valueNode.getPath(), teams + formattedItem);
				}
			}
		}
		catch (NullPointerException e)
		{
			Configs.setValue(teamConfig, valueNode.getPath(), allyTeamName + ",");
		}

		if (addAlly)
		{
			ConfigManager.addAlly(allyTeamName, teamName, false);
		}
	}

	public static void removeMember(String teamName, String memberUUID)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".members").split("\\."));

		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();
			teams = teams.replace(memberUUID + ",", "");
			Configs.setValue(teamConfig, valueNode.getPath(), teams);
		}
	}

	public static void removeExecutive(String teamName, String executiveUUID)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".executives").split("\\."));

		if (valueNode.getValue() != null && valueNode.getString().length() > 0)
		{
			String executives = valueNode.getString();
			executives = executives.replace(executiveUUID + ",", "");
			Configs.setValue(teamConfig, valueNode.getPath(), executives);
		}
	}

	public static void removeEnemy(String teamName, String enemyName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".enemies").split("\\."));

		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();
			teams = teams.replace(enemyName + ",", "");
			Configs.setValue(teamConfig, valueNode.getPath(), teams);
		}
	}

	public static void removeAlly(String teamName, String allyName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".allies").split("\\."));

		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();
			teams = teams.replace(allyName + ",", "");
			Configs.setValue(teamConfig, valueNode.getPath(), teams);
		}
	}

	public static void setTeamLeader(String teamName, String leaderUUID)
	{
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "leader" }, leaderUUID);
		ConfigManager.removeMember(teamName, leaderUUID);
	}

	public static void addTeamExecutive(String teamName, String executiveUUID)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".executives").split("\\."));

		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();

			if (teams.contains(executiveUUID + ","))
			{
				;
			}
			else
			{
				String formattedItem = (executiveUUID + ",");
				Configs.setValue(teamConfig, valueNode.getPath(), teams + formattedItem);
			}
		}
		else
		{
			Configs.setValue(teamConfig, valueNode.getPath(), executiveUUID + ",");
		}

		ConfigManager.removeMember(teamName, executiveUUID);
	}

	public static void removeTeam(String teamName)
	{
		Configs.setValue(teamConfig, ((Object[]) ("teams." + teamName).split("\\.")), "");
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams.teams").split("\\."));
		String val = valueNode.getString();
		Configs.setValue(teamConfig, valueNode.getPath(), val.replace(teamName + ",", ""));
		ConfigManager.removeClaims(teamName);
	}

	public static void claim(String teamName, UUID worldUUID, int chunkX, int chunkZ)
	{
		Configs.setValue(teamConfig, new Object[] { "claims", teamName, worldUUID.toString(), String.valueOf(chunkX), String.valueOf(chunkZ) }, true);
	}

	public static boolean displayPrefix()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode("polis", "prefix", "display");

		if (valueNode.getValue() != null)
		{
			return valueNode.getBoolean();
		}
		else
		{
			setDisplayPrefix(true);
			return true;
		}
	}

	public static void setDisplayPrefix(boolean value)
	{
		Configs.setValue(mainConfig, new Object[] { "polis", "prefix", "display" }, value);
	}

	public static void removeClaims(String teamName)
	{
		Configs.removeChild(teamConfig, new Object[] { "claims" }, teamName);
	}

	public static void unclaim(String teamName, UUID worldUUID, int chunkX, int chunkZ)
	{
		Configs.setValue(teamConfig, new Object[] { "claims", teamName, worldUUID.toString(), String.valueOf(chunkX), String.valueOf(chunkZ) }, false);
	}

	public static boolean isClaimed(String teamName, UUID worldUUID, int chunkX, int chunkZ)
	{
		try
		{
			ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("claims." + teamName + "." + worldUUID.toString() + "." + String.valueOf(chunkX) + "." + String.valueOf(chunkZ)).split("\\."));
			return valueNode.getBoolean();
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static String isClaimed(Location<World> location)
	{
		String claimed = "false";
		UUID worldUUID = location.getExtent().getUniqueId();
		Optional<Vector3i> optionalChunk = Polis.game.getServer().getChunkLayout().toChunk(location.getBlockPosition());

		if (optionalChunk.isPresent())
		{
			Vector3i chunk = optionalChunk.get();

			for (String teamName : getTeams())
			{
				try
				{
					ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("claims." + teamName + "." + worldUUID.toString() + "." + String.valueOf(chunk.getX()) + "." + String.valueOf(chunk.getZ())).split("\\."));
					boolean value = valueNode.getBoolean();

					if (value)
					{
						claimed = teamName;
						break;
					}
				}
				catch (Exception e)
				{
					;
				}
			}
		}

		return claimed;
	}

	public static boolean canUseInSafeZone(String id)
	{
		try
		{
			return getAllowedBlocksInSafeZone().contains(id);
		}
		catch (Exception e)
		{
			addUsableSafeZoneBlock("");
			return false;
		}
	}

	public static void addUsableSafeZoneBlock(String id)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams.safezone.usable.blocks").split("\\."));

		if (valueNode.getString() != null)
		{
			String items = valueNode.getString();
			if (items.contains(id + ","))
				;
			else
			{
				String formattedItem = (id + ",");
				Configs.setValue(teamConfig, valueNode.getPath(), items + formattedItem);
			}
		}
		else
		{
			Configs.setValue(teamConfig, valueNode.getPath(), id + ",");
		}
	}

	public static void removeUsableSafeZoneBlock(String id)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams.safezone.usable.blocks").split("\\."));
		String val = valueNode.getString();
		Configs.setValue(teamConfig, valueNode.getPath(), val.replace(id + ",", ""));
	}

	public static ArrayList<String> getAllowedBlocksInSafeZone()
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams.safezone.usable.blocks").split("\\."));

		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();

		ArrayList<String> teamsList = new ArrayList<String>();
		boolean finished = false;

		if (finished != true)
		{
			int endIndex = list.indexOf(",");

			if (endIndex != -1)
			{
				String substring = list.substring(0, endIndex);
				teamsList.add(substring);

				while (finished != true)
				{
					int startIndex = endIndex;
					endIndex = list.indexOf(",", startIndex + 1);

					if (endIndex != -1)
					{
						String substrings = list.substring(startIndex + 1, endIndex);
						teamsList.add(substrings);
					}
					else
					{
						finished = true;
					}
				}
			}
			else if (!list.equals(""))
			{
				teamsList.add(list);
				finished = true;
			}
		}

		return teamsList;
	}
}
