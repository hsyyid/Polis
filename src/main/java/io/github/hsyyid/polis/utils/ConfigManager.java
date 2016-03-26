package io.github.hsyyid.polis.utils;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.Lists;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.config.ClaimsConfig;
import io.github.hsyyid.polis.config.Config;
import io.github.hsyyid.polis.config.Configs;
import io.github.hsyyid.polis.config.Configurable;
import io.github.hsyyid.polis.config.MessageConfig;
import io.github.hsyyid.polis.config.TeamsConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ConfigManager
{
	private static Configurable mainConfig = Config.getConfig();
	private static Configurable teamConfig = TeamsConfig.getConfig();
	private static Configurable claimsConfig = ClaimsConfig.getConfig();
	private static Configurable messageConfig = MessageConfig.getConfig();

	public static Set<Object> getTeams()
	{
		// Remove old way of getting teams
		if (Configs.getConfig(teamConfig).getNode("teams", "teams").getValue() != null)
		{
			Configs.removeChild(teamConfig, new Object[] { "teams" }, "teams");
		}

		return Configs.getConfig(teamConfig).getNode("teams").getChildrenMap().keySet();
	}

	public static String getTeam(UUID playerUUID)
	{
		for (Object t : ConfigManager.getTeams())
		{
			String team = String.valueOf(t);

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

	public static int getMaxNameLength()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("polis.name.maxlength").split("\\."));

		if (valueNode.getValue() != null)
		{
			return valueNode.getInt();
		}
		else
		{
			Configs.setValue(mainConfig, valueNode.getPath(), 30);
			return 30;
		}
	}

	public static int getMinNameLength()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("polis.name.minlength").split("\\."));

		if (valueNode.getValue() != null)
		{
			return valueNode.getInt();
		}
		else
		{
			Configs.setValue(mainConfig, valueNode.getPath(), 3);
			return 3;
		}
	}

	public static BigDecimal getBalance(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".balance").split("\\."));

		if (valueNode.getValue() != null)
			return new BigDecimal(valueNode.getDouble());
		else
			return new BigDecimal(0);
	}

	public static void depositToTownBank(BigDecimal amount, String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".balance").split("\\."));

		if (valueNode.getValue() != null)
			Configs.setValue(teamConfig, valueNode.getPath(), valueNode.getDouble() + amount.doubleValue());
		else
			Configs.setValue(teamConfig, valueNode.getPath(), amount.doubleValue());
	}

	public static void withdrawFromTownBank(BigDecimal amount, String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".balance").split("\\."));

		if (valueNode.getValue() != null)
			Configs.setValue(teamConfig, valueNode.getPath(), valueNode.getDouble() - amount.doubleValue());
		else
			Configs.setValue(teamConfig, valueNode.getPath(), -amount.doubleValue());
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

		if (valueNode.getValue() != null)
			return true;
		else
			return false;
	}

	public static ArrayList<String> getMembers(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".members").split("\\."));

		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();
		ArrayList<String> membersList = Lists.newArrayList();
		boolean finished = false;

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
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".executives").split("\\."));

		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();
		ArrayList<String> executivesList = Lists.newArrayList();
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

	public static ArrayList<String> getExecutiveNames(String teamName)
	{
		ArrayList<String> names = Lists.newArrayList();

		for (String executive : getExecutives(teamName))
		{
			if (!executive.equals(""))
				names.add(UUIDFetcher.getName(UUID.fromString(executive)).orElse(executive));
		}

		return names;
	}

	public static ArrayList<String> getMemberNames(String teamName)
	{
		ArrayList<String> names = Lists.newArrayList();

		for (String member : getMembers(teamName))
		{
			if (!member.equals(""))
				names.add(UUIDFetcher.getName(UUID.fromString(member)).orElse(member));
		}

		return names;
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
		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();
		ArrayList<String> alliesList = Lists.newArrayList();
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
		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();
		ArrayList<String> enemyList = Lists.newArrayList();
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
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".leader").split("\\."));

		if (valueNode.getValue() != null)
			return valueNode.getString();
		else
			return "";
	}

	public static boolean areTaxesEnabled(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".taxes.enabled").split("\\."));

		if (valueNode.getValue() != null)
			return valueNode.getBoolean();
		else
			return false;
	}

	public static void setTaxesEnabled(String teamName, boolean value)
	{
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "taxes", "enabled" }, value);
	}

	public static int getTaxInterval(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".taxes.interval").split("\\."));

		if (valueNode.getValue() != null)
			return valueNode.getInt();
		else
			setTaxInterval(teamName, 86400);
		return 86400;
	}

	public static void setTaxInterval(String teamName, int interval)
	{
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "taxes", "interval" }, interval);
	}

	public static BigDecimal getTax(String teamName)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams." + teamName + ".taxes.amount").split("\\."));

		if (valueNode.getValue() != null)
			return new BigDecimal(valueNode.getDouble());
		else
			setTax(teamName, new BigDecimal(100));

		return new BigDecimal(100);
	}

	public static void setTax(String teamName, BigDecimal amount)
	{
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "taxes", "amount" }, amount.doubleValue());
	}

	public static void addTeam(String teamName, String leaderUUID)
	{
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "leader" }, leaderUUID);
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "members" }, "");
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "enemies" }, "");
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "allies" }, "");
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "taxes", "enabled" }, true);
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "taxes", "interval" }, 86400);
		Configs.setValue(teamConfig, new Object[] { "teams", teamName, "taxes", "amount" }, 100.00);
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

		if (valueNode.getValue() != null)
		{
			String teams = valueNode.getString();

			if (!teams.contains(executiveUUID + ","))
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

	public static void removeTeam(String teamName, boolean notifyMembers)
	{
		if (notifyMembers)
		{
			List<String> members = ConfigManager.getMembers(teamName);
			members.addAll(ConfigManager.getExecutives(teamName));

			for (String memberUuid : members)
			{
				UUID uuid = UUID.fromString(memberUuid);

				if (Sponge.getServer().getPlayer(uuid).isPresent())
				{
					Sponge.getServer().getPlayer(uuid).get().sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Your Polis has been deleted!"));
				}
			}
		}

		Configs.removeChild(teamConfig, new Object[] { "teams" }, teamName);
		ConfigManager.removeClaims(teamName);
	}

	public static void claim(String teamName, UUID worldUUID, int chunkX, int chunkZ)
	{
		Configs.setValue(claimsConfig, new Object[] { "claims", teamName, worldUUID.toString(), String.valueOf(chunkX), String.valueOf(chunkZ) }, true);
	}

	public static BigDecimal getClaimCost()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode("polis", "claims", "cost");

		if (valueNode.getValue() != null)
			return new BigDecimal(valueNode.getDouble());
		else
			return new BigDecimal(0);
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

	public static Text getUnclaimedNotification()
	{
		return TextSerializers.FORMATTING_CODE.deserialize(Configs.getConfig(messageConfig).getNode("polis", "messages", "claims", "unclaimed", "notification").getString());
	}

	public static Text getClaimedNotification(String teamName)
	{
		return TextSerializers.FORMATTING_CODE.deserialize(Configs.getConfig(messageConfig).getNode("polis", "messages", "claims", "claimed", "notification").getString().replaceAll("@t", teamName));
	}

	public static Text getLeaderPrefix(String teamName)
	{
		return TextSerializers.FORMATTING_CODE.deserialize(Configs.getConfig(messageConfig).getNode("polis", "prefix", "leader").getString().replaceAll("@t", teamName));
	}

	public static Text getExecutivePrefix(String teamName)
	{
		return TextSerializers.FORMATTING_CODE.deserialize(Configs.getConfig(messageConfig).getNode("polis", "prefix", "executive").getString().replaceAll("@t", teamName));
	}

	public static Text getMemberPrefix(String teamName)
	{
		return TextSerializers.FORMATTING_CODE.deserialize(Configs.getConfig(messageConfig).getNode("polis", "prefix", "member").getString().replaceAll("@t", teamName));
	}

	public static int getClaims(String teamName)
	{
		return Configs.getConfig(claimsConfig).getNode("claims", teamName).getChildrenMap().keySet().size();
	}

	public static int getClaimCap()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("polis", "claims", "cap");

		if (node.getValue() != null)
		{
			return node.getInt();
		}
		else
		{
			Configs.setValue(mainConfig, node.getPath(), 50);
			return 50;
		}
	}

	public static void removeClaims(String teamName)
	{
		Configs.removeChild(claimsConfig, new Object[] { "claims" }, teamName);
	}

	public static void unclaim(String teamName, UUID worldUUID, int chunkX, int chunkZ)
	{
		Configs.setValue(claimsConfig, new Object[] { "claims", teamName, worldUUID.toString(), String.valueOf(chunkX), String.valueOf(chunkZ) }, false);
	}

	public static boolean isClaimed(String teamName, UUID worldUUID, int chunkX, int chunkZ)
	{
		try
		{
			ConfigurationNode valueNode = Configs.getConfig(claimsConfig).getNode((Object[]) ("claims." + teamName + "." + worldUUID.toString() + "." + String.valueOf(chunkX) + "." + String.valueOf(chunkZ)).split("\\."));
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

			for (Object team : getTeams())
			{
				String teamName = String.valueOf(team);

				try
				{
					ConfigurationNode valueNode = Configs.getConfig(claimsConfig).getNode((Object[]) ("claims." + teamName + "." + worldUUID.toString() + "." + String.valueOf(chunk.getX()) + "." + String.valueOf(chunk.getZ())).split("\\."));
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

	public static String isClaimed(Vector3i chunk, UUID worldUUID)
	{
		String claimed = "false";

		for (Object team : getTeams())
		{
			String teamName = String.valueOf(team);

			try
			{
				ConfigurationNode valueNode = Configs.getConfig(claimsConfig).getNode((Object[]) ("claims." + teamName + "." + worldUUID.toString() + "." + String.valueOf(chunk.getX()) + "." + String.valueOf(chunk.getZ())).split("\\."));
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

	public static TextColor getColorTo(String observerPolis, String polis)
	{
		if (observerPolis.equals(polis))
		{
			return TextColors.GOLD;
		}
		else if (ConfigManager.getAllies(observerPolis).contains(polis))
		{
			return TextColors.GREEN;
		}
		else if (ConfigManager.getEnemies(observerPolis).contains(polis))
		{
			return TextColors.RED;
		}
		else if (polis.equals("WarZone"))
		{
			return TextColors.DARK_RED;
		}
		else if (polis.equals("SafeZone"))
		{
			return TextColors.YELLOW;
		}
		else
		{
			return TextColors.DARK_GREEN;
		}
	}

	public static void addUsableSafeZoneBlock(String id)
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode((Object[]) ("teams.safezone.usable.blocks").split("\\."));

		if (valueNode.getString() != null)
		{
			String items = valueNode.getString();

			if (!items.contains(id + ","))
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

	public static void transferClaimsConfig()
	{
		ConfigurationNode valueNode = Configs.getConfig(teamConfig).getNode("claims");

		if (valueNode.getValue() != null)
		{
			Configs.setValue(claimsConfig, valueNode.getPath(), valueNode.getValue());
			Configs.removeChildren(teamConfig, valueNode.getPath());
			Configs.setValue(teamConfig, valueNode.getPath(), null);
		}
	}

	public static void updatePolisName(String oldPolisName, String newPolisName)
	{
		// Transfer Claims
		ConfigurationNode claimNode = Configs.getConfig(claimsConfig).getNode("claims", oldPolisName);
		Configs.setValue(claimsConfig, new Object[] { "claims", newPolisName }, claimNode.getValue());
		Configs.removeChild(claimsConfig, new Object[] { "claims" }, oldPolisName);

		// Transfer Polis Info
		ConfigurationNode polisNode = Configs.getConfig(teamConfig).getNode("teams", oldPolisName);
		Configs.setValue(teamConfig, new Object[] { "teams", newPolisName }, polisNode.getValue());
		Configs.removeChild(teamConfig, new Object[] { "teams" }, oldPolisName);
	}

	public static BigDecimal getPolisCreateCost()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode("polis", "create", "cost");

		if (valueNode.getValue() != null)
		{
			return new BigDecimal(valueNode.getDouble());
		}
		else
		{
			Configs.setValue(mainConfig, valueNode.getPath(), 50.00);
			return new BigDecimal(50.00);
		}
	}
}
