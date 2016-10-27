package io.github.hsyyid.polis.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;

import io.github.hsyyid.polis.cache.ClaimCache;

public class ChunkListener
{
	@Listener
	public void onChunkLoad(LoadChunkEvent event)
	{
		ClaimCache.onChunkLoad(event.getTargetChunk().getPosition(), event.getTargetChunk().getWorld().getUniqueId());
	}
	
	@Listener
	public void onChunkUnload(UnloadChunkEvent event)
	{
		ClaimCache.onChunkUnload(event.getTargetChunk().getPosition(), event.getTargetChunk().getWorld().getUniqueId());
	}
}
