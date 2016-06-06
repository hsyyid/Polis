package io.github.hsyyid.polis.listeners;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class EntityMoveListener
{
	@Listener
	public void onPlayerMove(MoveEntityEvent event)
	{
		if (event.getTargetEntity() instanceof Player)
		{
			Player player = (Player) event.getTargetEntity();

			Location<World> previousLocation = event.getFromTransform().getLocation();
			Location<World> newLocation = event.getToTransform().getLocation();

			if (Polis.autoClaim.contains(player.getUniqueId()) && ConfigManager.isClaimed(previousLocation).equals("false"))
			{
				String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

				if (playerTeamName != null && (ConfigManager.getExecutives(playerTeamName).contains(player.getUniqueId().toString()) || ConfigManager.getLeader(playerTeamName).equals(player.getUniqueId().toString())))
				{
					Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());

					if (optionalChunk.isPresent())
					{
						Vector3i chunk = optionalChunk.get();

						if (ConfigManager.getClaims(playerTeamName) < ConfigManager.getClaimCap())
						{
							if (ConfigManager.getBalance(playerTeamName).compareTo(ConfigManager.getClaimCost()) >= 0)
							{
								Account account = Polis.economyService.getOrCreateAccount(playerTeamName).get();
								TransactionResult transactionResult = account.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getClaimCost(), Cause.of(NamedCause.source(player)));

								if (transactionResult.getResult() == ResultType.SUCCESS)
								{
									//TODO Perhaps move this into an asynchronous task so if they're running around super fast it's not going to cause issues. Alternately only save the claims after they turn it off
									ConfigManager.claim(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), true);
									ConfigManager.withdrawFromTownBank(ConfigManager.getClaimCost(), playerTeamName);
									player.sendMessage(Text.builder().append(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location for " + ConfigManager.getClaimCost() + " "))
										.append(Polis.economyService.getDefaultCurrency().getPluralDisplayName()).build());
								}
								else if (transactionResult.getResult() == ResultType.ACCOUNT_NO_FUNDS)
								{
									player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Not enough funds! Deposit funds or setup taxes!"));
								}
								else
								{
									player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "An error occured while trying to withdraw from your Polis' bank."));
								}
							}
							else
							{
								player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Your Polis does not have enough funds to claim this land! Deposit funds soon!"));
							}
						}
						else
						{
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You already have the maximum number of claims!"));
						}
					}
				}
			}

			if (Polis.adminAutoClaim.containsKey(player.getUniqueId()) && ConfigManager.isClaimed(previousLocation).equals("false"))
			{
				Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());

				if (optionalChunk.isPresent())
				{
					//Same TODO as above
					Vector3i chunk = optionalChunk.get();
					ConfigManager.claim(Polis.adminAutoClaim.get(player.getUniqueId()), player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), true);
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location for " + Polis.adminAutoClaim.get(player.getUniqueId())));
				}
			}

			if (!ConfigManager.isClaimed(previousLocation).equalsIgnoreCase(ConfigManager.isClaimed(newLocation)))
			{
				if (ConfigManager.isClaimed(newLocation).equalsIgnoreCase("false"))
				{
					player.sendMessage(ConfigManager.getUnclaimedNotification());
				}
				else
				{
					player.sendMessage(ConfigManager.getClaimedNotification(ConfigManager.isClaimed(newLocation)));
				}
			}
		}

		if (event.getTargetEntity() instanceof Monster)
		{
			Location<World> newLocation = event.getToTransform().getLocation();

			if (ConfigManager.isClaimed(newLocation).equals("SafeZone"))
			{
				event.setCancelled(true);
			}
		}
	}
}
