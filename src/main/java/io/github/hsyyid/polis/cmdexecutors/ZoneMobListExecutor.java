package io.github.hsyyid.polis.cmdexecutors;

import com.google.common.collect.Lists;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.List;

public class ZoneMobListExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String zone = ctx.<String> getOne("zone").get();

		if (!(zone.equalsIgnoreCase("SafeZone") || zone.equalsIgnoreCase("WarZone")))
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "Zone specified does not exist."));
			return CommandResult.empty();
		}

		List<EntityType> mobs = ConfigManager.getMobs(zone.toLowerCase());

		if (mobs.isEmpty())
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.RED, "There are no mobs disabled for the specified zone."));
			return CommandResult.empty();
		}

		List<Text> mobList = Lists.newArrayList();

		for (EntityType e : mobs)
		{
			Text item = Text.builder(e.getTranslation().get())
				.color(TextColors.DARK_AQUA)
				.style(TextStyles.UNDERLINE)
				.build();

			mobList.add(item);
		}

		PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
		PaginationList.Builder paginationBuilder = paginationService.builder().title(Text.of(TextColors.GREEN, "Showing Zone Mobs")).padding(Text.of("-")).contents(mobList);
		paginationBuilder.sendTo(src);
		return CommandResult.success();
	}
}
