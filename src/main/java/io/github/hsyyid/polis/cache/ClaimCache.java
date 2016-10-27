package io.github.hsyyid.polis.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import io.github.hsyyid.polis.utils.ConfigManager;

public class ClaimCache
{
	public static HashMap<String, String> claimCache = new HashMap<String, String>();
	
	public synchronized static void onChunkLoad(Vector3i location, UUID world)
	{
		String claim = ConfigManager.isClaimed(location, world);
		if (!claim.equals("false"))
			claimCache.put(world.toString() + "." + location.getX() + "." + location.getZ(), claim);
	}
	
	public synchronized static void onChunkUnload(Vector3i location, UUID world)
	{
		claimCache.remove(world.toString() + "." + location.getX() + "." + location.getZ());	
	}
	
	public synchronized static String getClaim(Location<World> location)
	{
		Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(location.getBlockPosition());
		
		if (optionalChunk.isPresent())
		{
			String townName = claimCache.get(location.getExtent().getUniqueId().toString() + "." + optionalChunk.get().getX() + "." + optionalChunk.get().getZ());
			return (townName == null ? "false" : townName);
		}
		else
			return "false";
	}
	
	public synchronized static void claim(UUID world, Integer chunkX, Integer chunkZ, String teamName)
	{
		claimCache.put(world.toString() + "." + chunkX + "." + chunkZ, teamName);
	}
	
	public synchronized static void unclaim(UUID world, Integer chunkX, Integer chunkZ)
	{
		claimCache.remove(world.toString() + "." + chunkX + "." + chunkZ);
	}
	
	public synchronized static void unclaimAll(String teamName)
	{
		ArrayList<String> removeClaims = new ArrayList<String>();
		
		for (String claimKey : claimCache.keySet())
			if (claimCache.get(claimKey).equals(teamName))
				removeClaims.add(claimKey);
		
		for (String claim : removeClaims)
			claimCache.remove(claim);
	}
}
