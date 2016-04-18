package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ZoneMobDeleteExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String zone = ctx.<String> getOne("zone").get();
		EntityType type = ctx.<EntityType> getOne("mob").get();

		if (!(zone.equalsIgnoreCase("SafeZone") || zone.equalsIgnoreCase("WarZone")))
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "Zone specified does not exist."));
			return CommandResult.empty();
		}

		if (!ConfigManager.getMobs(zone.toLowerCase()).contains(type))
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "Mob is not blocked in zone!"));
			return CommandResult.empty();
		}

		ConfigManager.removeMob(zone.toLowerCase(), type);
		src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Un-blocked mob in zone!"));
		return CommandResult.success();
	}
}
