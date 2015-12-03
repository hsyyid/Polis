package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class PolisExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Texts.of(TextColors.GREEN, "Polis: ", TextColors.GRAY, "Version: ", TextColors.GOLD, Polis.game.getPluginManager().getPlugin("Polis").get().getVersion()));
		return CommandResult.success();
	}
}