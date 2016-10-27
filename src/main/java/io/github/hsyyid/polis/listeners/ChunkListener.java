package io.github.hsyyid.polis.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;

import io.github.hsyyid.polis.PluginInfo;
import io.github.hsyyid.polis.cache.ClaimCache;

public class ChunkListener
{
	@Listener
	public void onChunkLoad(LoadChunkEvent event)
	{
		Sponge.getScheduler().createTaskBuilder().async().execute(new Runnable() {
			@Override
			public void run()
			{
				ClaimCache.onChunkLoad(event.getTargetChunk().getPosition(), event.getTargetChunk().getWorld().getUniqueId());
			}
		}).submit(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get().getInstance().get());
	}
	
	@Listener
	public void onChunkUnload(UnloadChunkEvent event)
	{
		Sponge.getScheduler().createTaskBuilder().async().execute(new Runnable() {
			@Override
			public void run()
			{
				ClaimCache.onChunkUnload(event.getTargetChunk().getPosition(), event.getTargetChunk().getWorld().getUniqueId());
			}
		}).submit(Sponge.getPluginManager().getPlugin(PluginInfo.ID).get().getInstance().get());
	}
}
