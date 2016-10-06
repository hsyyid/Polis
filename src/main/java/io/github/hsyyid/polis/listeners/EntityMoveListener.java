package io.github.hsyyid.polis.listeners;

import com.flowpowered.math.vector.Vector3i;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EntityMoveListener {
	@Listener(order = Order.POST)
	public void onPlayerMove(final MoveEntityEvent event, @Getter("getTargetEntity") final Player player) {
		// Run this after the event, with a snapshot of said event. We're not changing the transform, so this is OK.
		// We can then use the new location.
		final Location<World> previousLocation = event.getFromTransform().getLocation();
		final Location<World> newLocation = event.getToTransform().getLocation();

		// The next bit only needs to care if we've moved chunks. As per the order, this should not be changed,
		// there is just the potential for it to be canceled.
		if (previousLocation.getChunkPosition().equals(newLocation.getChunkPosition())) {
			return;
		}

		Sponge.getScheduler().createSyncExecutor(Polis.getPolis()).submit(() -> {
			if (event.isCancelled()) {
				return;
			}

			// Claim on entry into new location
			if (Polis.autoClaim.contains(player.getUniqueId()) && ConfigManager.isClaimed(newLocation).equals("false")) {
				String playerTeamName = ConfigManager.getTeam(player.getUniqueId());

				if (playerTeamName != null && (ConfigManager.getExecutives(playerTeamName).contains(player.getUniqueId().toString()) || ConfigManager.getLeader(playerTeamName).equals(player.getUniqueId().toString()))) {
					Vector3i chunk = newLocation.getChunkPosition();

					if (ConfigManager.getClaims(playerTeamName) < ConfigManager.getClaimCapMax()) {
						if (ConfigManager.getBalance(playerTeamName).compareTo(ConfigManager.getClaimCost()) >= 0) {
							Account account = Polis.economyService.getOrCreateAccount(playerTeamName).get();
							TransactionResult transactionResult = account.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getClaimCost(), Cause.of(NamedCause.source(player)));

							if (transactionResult.getResult() == ResultType.SUCCESS) {
								// TODO: Perhaps move this into an asynchronous task so if they're running around super fast it's not going to cause issues. Alternately only save the claims after they turn it off
								ConfigManager.claim(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), true);
								ConfigManager.withdrawFromTownBank(ConfigManager.getClaimCost(), playerTeamName);
								player.sendMessage(Text.builder().append(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location for " + ConfigManager.getClaimCost() + " "))
										.append(Polis.economyService.getDefaultCurrency().getPluralDisplayName()).build());
							} else if (transactionResult.getResult() == ResultType.ACCOUNT_NO_FUNDS) {
								player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Not enough funds! Deposit funds or setup taxes!"));
							} else {
								player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "An error occured while trying to withdraw from your Polis' bank."));
							}
						} else {
							player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Your Polis does not have enough funds to claim this land! Deposit funds soon!"));
						}
					} else {
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You already have the maximum number of claims!"));
					}
				}
			} else if (Polis.adminAutoClaim.containsKey(player.getUniqueId()) && ConfigManager.isClaimed(newLocation).equals("false")) {
				Vector3i chunk = newLocation.getChunkPosition();
				ConfigManager.claim(Polis.adminAutoClaim.get(player.getUniqueId()), player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), true);
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Successfully claimed this location for " + Polis.adminAutoClaim.get(player.getUniqueId())));
			}

			if (!ConfigManager.isClaimed(previousLocation).equals(ConfigManager.isClaimed(newLocation))) {
				if (ConfigManager.isClaimed(newLocation).equals("false")) {
					player.sendMessage(ConfigManager.getUnclaimedNotification());
				} else {
					player.sendMessage(ConfigManager.getClaimedNotification(ConfigManager.isClaimed(newLocation)));
				}
			}
		});
	}

	@Listener
	public void onPlayerMove(MoveEntityEvent event, @Getter("getTargetEntity") Monster monster) {
		// No need to do anything if the block location has not changed.
		Location<World> previousLocation = event.getFromTransform().getLocation();
		Location<World> newLocation = event.getToTransform().getLocation();
		if (previousLocation.getBlockPosition().toVector2(true).equals(newLocation.getBlockPosition().toVector2(true))) {
			return;
		}

		if (ConfigManager.isClaimed(newLocation).equals("SafeZone")) {
			// Don't allow mobs in SafeZone!
			event.setCancelled(true);
		}
	}
}
