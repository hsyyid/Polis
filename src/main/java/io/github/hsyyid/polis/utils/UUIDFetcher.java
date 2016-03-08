package io.github.hsyyid.polis.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UUIDFetcher
{
	public static Optional<String> getName(UUID uuid)
	{
		CompletableFuture<GameProfile> gameProfile = Sponge.getServer().getGameProfileManager().get(uuid);

		try
		{
			if (gameProfile.get() != null)
				return gameProfile.get().getName();
			else
				return Optional.empty();
		}
		catch (Exception ex)
		{
			return Optional.empty();
		}
	}
}
