package io.github.hsyyid.polis.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;
import org.spongepowered.api.world.Chunk;

import io.github.hsyyid.polis.cache.ClaimCache;

public class ChunkListener
{
	@Listener
	public void onChunkLoad(LoadChunkEvent event)
	{
		Chunk chunk = event.getTargetChunk();
		ClaimCache.onChunkLoad(chunk.getPosition(), chunk.getWorld().getUniqueId());
	}
	
	@Listener
	public void onChunkUnload(UnloadChunkEvent event)
	{
		ClaimCache.onChunkUnload(event.getTargetChunk().getPosition(), event.getTargetChunk().getWorld().getUniqueId());
	}
}
