package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class PolisPlayerInfoExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Player player = ctx.<Player> getOne("player").get();
		String polis = ConfigManager.getTeam(player.getUniqueId());

		if (polis != null)
		{
			Optional<UniqueAccount> account = Polis.economyService.getOrCreateAccount(player.getUniqueId());

			src.sendMessage(Text.of(TextColors.DARK_GRAY, "Town: ", TextColors.GOLD, polis));

			if (account.isPresent())
				src.sendMessage(Text.of(TextColors.DARK_GRAY, "Balance: ", TextColors.GOLD, account.get().getBalance(Polis.economyService.getDefaultCurrency())));

			src.sendMessage(Text.of(TextColors.DARK_GRAY, "Server Join Date: ", TextColors.GOLD, player.getJoinData().firstPlayed().get().toString()));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Player is not in a Polis!"));
		}

		return CommandResult.success();
	}
}
