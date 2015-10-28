package io.github.hsyyid.towny.utils;

import io.github.hsyyid.towny.Towny;

import java.io.IOException;
import java.util.ArrayList;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class ConfigManager
{
	public static ArrayList<String> getTeams()
	{
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams.teams").split("\\."));
		String list = valueNode.getString();

		ArrayList<String> teamsList = new ArrayList<String>();
		boolean finished = false;

		// Add all homes to homeList
		if (finished != true)
		{
			int endIndex = list.indexOf(",");
			if (endIndex != -1)
			{
				String substring = list.substring(0, endIndex);
				teamsList.add(substring);

				// If they Have More than 1
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
			else
			{
				teamsList.add(list);
				finished = true;
			}
		}

		return teamsList;
	}

	public static String getHQWorldName(String teamName)
	{
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".hq.world").split("\\."));
		return valueNode.getString();
	}
	
	public static double getX(String teamName)
	{
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".hq.X").split("\\."));
		return valueNode.getDouble();
	}

	public static double getY(String teamName)
	{
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".hq.Y").split("\\."));
		return valueNode.getDouble();
	}
	
	public static double getZ(String teamName)
	{
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".hq.Z").split("\\."));
		return valueNode.getDouble();
	}
	
	public static boolean inConfig(String teamName)
	{
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".hq.X").split("\\."));
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
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".members").split("\\."));
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
	
	public static ArrayList<String> getExecutives(String teamName)
    {
        ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".executives").split("\\."));
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

	public static void setHQ(String teamName, Location<World> hqLocation, String worldName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();
		Towny.config.getNode("teams", teamName, "hq", "world").setValue(worldName);
		Towny.config.getNode("teams", teamName, "hq", "X").setValue(hqLocation.getX());
		Towny.config.getNode("teams", teamName, "hq", "Y").setValue(hqLocation.getY());
		Towny.config.getNode("teams", teamName, "hq", "Z").setValue(hqLocation.getZ());
		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			;
		}
	}

	public static ArrayList<String> getAllies(String teamName)
	{
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".allies").split("\\."));
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
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".enemies").split("\\."));
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
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".leader").split("\\."));
		return valueNode.getString();
	}

	public static void addTeam(String teamName, String leaderUUID)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();
		Towny.config.getNode("teams", teamName, "leader").setValue(leaderUUID);
		Towny.config.getNode("teams", teamName, "members").setValue("");
		Towny.config.getNode("teams", teamName, "enemies").setValue("");
		Towny.config.getNode("teams", teamName, "allies").setValue("");
		Towny.config.getNode("teams", teamName, "executives").setValue("");

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams.teams").split("\\."));
		if (valueNode.getString() != null)
		{
			String items = valueNode.getString();
			if (items.contains(teamName + ","))
				;
			else
			{
				String formattedItem = (teamName + ",");
				valueNode.setValue(items + formattedItem);
			}
		}
		else
		{
			valueNode.setValue(teamName + ",");
		}

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Teams]: Failed to create team!");
		}
	}

	public static void addTeamMember(String teamName, String memberUUID)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".members").split("\\."));
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
				valueNode.setValue(teams + formattedItem);
			}
		}
		else
		{
			valueNode.setValue(memberUUID + ",");
		}

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Teams]: Failed to add " + memberUUID + " to team " + teamName + "!");
		}
	}

	public static void addEnemy(String teamName, String enemyTeamName, boolean addEnemy)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".enemies").split("\\."));
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
					valueNode.setValue(teams + formattedItem);
				}
			}
		}
		catch (NullPointerException e)
		{
			valueNode.setValue(enemyTeamName + ",");
		}

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Teams]: Failed to add " + enemyTeamName + " as enemy to team " + teamName + "!");
		}

		if (addEnemy)
		{
			ConfigManager.addEnemy(enemyTeamName, teamName, false);
		}
	}

	public static void addAlly(String teamName, String allyTeamName, boolean addAlly)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".allies").split("\\."));
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
					valueNode.setValue(teams + formattedItem);
				}
			}
		}
		catch (NullPointerException e)
		{
			valueNode.setValue(allyTeamName + ",");
		}

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Teams]: Failed to add " + allyTeamName + " as ally to team " + teamName + "!");
		}

		if (addAlly)
		{
			ConfigManager.addAlly(allyTeamName, teamName, false);
		}
	}

	public static void removeMember(String teamName, String memberUUID)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".members").split("\\."));
		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();
			teams = teams.replace(memberUUID + ",", "");
			valueNode.setValue(teams);
		}

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Teams]: Failed to remove " + memberUUID + " from team " + teamName + "!");
		}
	}
	
	public static void removeExecutive(String teamName, String executiveUUID)
    {
        ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

        ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".executives").split("\\."));
        
        if (valueNode.getValue() != null && valueNode.getString().length() > 0)
        {
            String teams = valueNode.getString();
            teams = teams.replace(executiveUUID + ",", "");
            valueNode.setValue(teams);
        }

        try
        {
            configManager.save(Towny.config);
            configManager.load();
        }
        catch (IOException e)
        {
            System.out.println("[Teams]: Failed to remove executive " + executiveUUID + " from team " + teamName + "!");
        }
    }

	public static void removeEnemy(String teamName, String enemyName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".enemies").split("\\."));
		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();
			teams = teams.replace(enemyName + ",", "");
			valueNode.setValue(teams);
		}

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			;
		}
	}

	public static void removeAlly(String teamName, String allyName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".allies").split("\\."));
		if (valueNode.getString() != null || valueNode.getString().length() > 0)
		{
			String teams = valueNode.getString();
			teams = teams.replace(allyName + ",", "");
			valueNode.setValue(teams);
		}

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			;
		}
	}

	public static void setTeamLeader(String teamName, String leaderUUID)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".leader").split("\\."));
		valueNode.setValue(leaderUUID);

		removeMember(teamName, leaderUUID);

		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Teams]: Failed to set " + teamName + "'s leader!");
		}
	}
	
	public static void addTeamExecutive(String teamName, String executiveUUID)
    {
        ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

        ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams." + teamName + ".executives").split("\\."));
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
                valueNode.setValue(teams + formattedItem);
            }
        }
        else
        {
            valueNode.setValue(executiveUUID + ",");
        }

        removeMember(teamName, executiveUUID);

        try
        {
            configManager.save(Towny.config);
            configManager.load();
        }
        catch (IOException e)
        {
            System.out.println("[Teams]: Failed to add an executive to " + teamName + "!");
        }
    }

	public static void removeTeam(String teamName)
	{
		ConfigurationLoader<CommentedConfigurationNode> configManager = Towny.getConfigManager();

		ConfigurationNode node = Towny.config.getNode((Object[]) ("teams." + teamName).split("\\."));
		node.setValue("");
		ConfigurationNode valueNode = Towny.config.getNode((Object[]) ("teams.teams").split("\\."));
		String val = valueNode.getString();
		valueNode.setValue(val.replace(teamName + ",", ""));
		try
		{
			configManager.save(Towny.config);
			configManager.load();
		}
		catch (IOException e)
		{
			System.out.println("[Teams]: Failed to set " + teamName + "'s leader!");
		}
	}
}
