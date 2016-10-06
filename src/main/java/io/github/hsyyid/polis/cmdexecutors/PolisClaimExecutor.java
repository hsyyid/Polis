package io.github.hsyyid.polis.cmdexecutors;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import com.flowpowered.math.vector.Vector3i;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.CanClaimResult;
import io.github.hsyyid.polis.utils.ConfigManager;

public class PolisClaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			String playerTeamName = ConfigManager.getTeam(player.getUniqueId());
			
			CanClaimResult canClaim = ConfigManager.canClaim(player);
			
			if (canClaim == CanClaimResult.YES)
			{
				Optional<Vector3i> optionalChunk = Sponge.getServer().getChunkLayout().toChunk(player.getLocation().getBlockPosition());
				if (optionalChunk.isPresent())
				{
					Vector3i chunk = optionalChunk.get();
					TransactionResult transactionResult = null;
					Account account = Polis.economyService.getOrCreateAccount(playerTeamName).get();
					transactionResult = account.withdraw(Polis.economyService.getDefaultCurrency(), ConfigManager.getClaimCost(), Cause.of(NamedCause.source(player)));
					
					if (transactionResult.getResult() == ResultType.SUCCESS)
					{
						ConfigManager.claim(playerTeamName, player.getLocation().getExtent().getUniqueId(), chunk.getX(), chunk.getZ(), true);
						
						if (ConfigManager.getHQY(playerTeamName) == 0 && ConfigManager.getHQX(playerTeamName) == 0 && ConfigManager.getHQZ(playerTeamName) == 0)
							ConfigManager.setHQ(playerTeamName, player.getLocation(), player.getWorld().getName());
						
						ConfigManager.withdrawFromTownBank(ConfigManager.getClaimCost(), playerTeamName);
					}
				}
			}
			player.sendMessage(canClaim.text);
		}
		return CommandResult.success();
	}
}
