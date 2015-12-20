package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.PaginatedList;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.Optional;

public class TownListExecutor implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			ArrayList<String> towns = ConfigManager.getTeams();

			if(towns.size() == 0)
			{
				player.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "There are no towns!"));
				return CommandResult.success();
			}
			
			Optional<Integer> arguments = ctx.<Integer> getOne("page no");
			int pgNo = arguments.orElse(1);

			// Add List
			PaginatedList pList = new PaginatedList("/polis list");
			
			for (String name : towns)
			{
				Text item = Texts.builder(name)
					.onClick(TextActions.runCommand("/polis info " + name))
					.onHover(TextActions.showText(Texts.of(TextColors.WHITE, "View info of town ", TextColors.GOLD, name)))
					.color(TextColors.DARK_AQUA)
					.style(TextStyles.UNDERLINE)
					.build();

				pList.add(item);
			}
			
			pList.setItemsPerPage(10);
			
			// Header
			TextBuilder header = Texts.builder();
			header.append(Texts.of(TextColors.GREEN, "------------"));
			header.append(Texts.of(TextColors.GREEN, " Showing Towns page " + pgNo + " of " + pList.getTotalPages() + " "));
			header.append(Texts.of(TextColors.GREEN, "------------"));

			pList.setHeader(header.build());
			// Send List
			src.sendMessage(pList.getPage(pgNo));

		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis list!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis list!"));
		}

		return CommandResult.success();
	}
}
