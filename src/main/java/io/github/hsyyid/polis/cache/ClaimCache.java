package io.github.hsyyid.polis.cache;

import java.util.HashMap;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import io.github.hsyyid.polis.utils.ConfigManager;

public class ClaimCache
{
	private static HashMap<UUID, HashMap<Integer, HashMap<Integer, String>>> claimCache = new HashMap<UUID, HashMap<Integer, HashMap<Integer, String>>>();
	
	public synchronized static void onChunkLoad(Vector3i location, UUID world)
	{
		if (!claimCache.containsKey(world))
			claimCache.put(world, new HashMap<Integer, HashMap<Integer, String>>());
		
		HashMap<Integer, HashMap<Integer, String>> worldCache = claimCache.get(world);
		
		if (!worldCache.containsKey(location.getX()))
			worldCache.put(location.getX(), new HashMap<Integer, String>());
		
		HashMap<Integer, String> chunkXCache = worldCache.get(location.getX());
		
		String townName = ConfigManager.isClaimed(location, world);
		
		if (townName.equals("false"))
			return;
		
		chunkXCache.put(location.getZ(), townName);
	}
	
	public synchronized static void onChunkUnload(Vector3i location, UUID world)
	{
		try
		{
			claimCache.get(world).get(location.getX()).remove(location.getZ());	
		}
		catch(Exception e)
		{
			e.printStackTrace(); // Don't think this would happen, but would be nice to see if it does
		}
	}
	
	public synchronized static String getClaim(Location<World> location)
	{
		HashMap<Integer, HashMap<Integer, String>> worldCache = claimCache.get(location.getExtent().getUniqueId());
		
		if (worldCache == null)
			return "false";
		
		Integer chunkX = (int) location.getX();
		
		HashMap<Integer, String> chunkXCache = worldCache.get(chunkX);
		
		if (chunkXCache == null)
			return "false";
		
		Integer chunkZ = (int) location.getZ();
		
		String townName = chunkXCache.get(chunkZ);
		
		return (townName == null ? "false" : townName);
	}
}
