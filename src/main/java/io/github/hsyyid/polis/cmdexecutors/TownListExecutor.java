package io.github.hsyyid.polis.cmdexecutors;

import com.google.common.collect.Lists;
import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;

public class TownListExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			ArrayList<String> towns = ConfigManager.getTeams();

			if (towns.size() == 0)
			{
				player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "There are no towns!"));
				return CommandResult.success();
			}

			List<Text> townList = Lists.newArrayList();

			for (String name : towns)
			{
				Text item = Text.builder(name)
					.onClick(TextActions.runCommand("/polis info " + name))
					.onHover(TextActions.showText(Text.of(TextColors.WHITE, "View info of town ", TextColors.GOLD, name)))
					.color(TextColors.DARK_AQUA)
					.style(TextStyles.UNDERLINE)
					.build();

				townList.add(item);
			}

			PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
			PaginationBuilder paginationBuilder = paginationService.builder().title(Text.of(TextColors.GREEN, "Showing Towns")).paddingString("-").contents(townList);
			paginationBuilder.sendTo(src);
		}
		else
		{
			src.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis list!"));
		}

		return CommandResult.success();
	}
}
