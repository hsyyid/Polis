package io.github.hsyyid.polis.utils;

import com.google.common.util.concurrent.ListenableFuture;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;

import java.util.Optional;
import java.util.UUID;

public class UUIDFetcher
{
	public static Optional<String> getName(UUID uuid)
	{
		ListenableFuture<GameProfile> gameProfile = Sponge.getServer().getGameProfileManager().get(uuid);

		try
		{
			if (gameProfile.get() != null)
				return Optional.of(gameProfile.get().getName());
			else
				return Optional.empty();
		}
		catch (Exception ex)
		{
			return Optional.empty();
		}
	}
}
