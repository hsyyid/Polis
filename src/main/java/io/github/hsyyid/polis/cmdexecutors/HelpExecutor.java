package io.github.hsyyid.polis.cmdexecutors;

import com.google.common.collect.Lists;
import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.PaginatedList;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class HelpExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{	
		HashMap<List<String>, CommandSpec> polisSubCommands = Polis.subcommands;
		List<List<Text>> helpList = Lists.newArrayList();
		
		for(List<String> aliases : polisSubCommands.keySet())
		{
			List<Text> commandHelp = Lists.newArrayList();
			CommandSpec commandSpec = polisSubCommands.get(aliases);
			
			commandHelp.add(Texts.of(TextColors.BLUE, "------------"));
			commandHelp.add(Texts.of(TextColors.GRAY, "Command Aliases: ", aliases.toString()));
			commandHelp.add(Texts.builder().append(Texts.of(TextColors.GOLD, "Command Description: "), commandSpec.getShortDescription(src).get()).build());
			commandHelp.add(Texts.builder().append(Texts.of(TextColors.GOLD, "Command Arguments: "), commandSpec.getUsage(src)).build());
			commandHelp.add(Texts.of(TextColors.BLUE, "------------"));
			
			helpList.add(commandHelp);
		}
		
		Optional<Integer> arguments = ctx.<Integer> getOne("page no");
		int pgNo = arguments.orElse(1);

		// Add List
		PaginatedList pList = new PaginatedList("/polis help");
		
		for (List<Text> commandHelp : helpList)
		{
			pList.addAll(commandHelp);
		}
		
		pList.setItemsPerPage(10);
		
		if(pgNo > pList.getTotalPages())
			pgNo = 1;
		
		// Header
		TextBuilder header = Texts.builder();
		header.append(Texts.of(TextColors.AQUA, "------------"));
		header.append(Texts.of(TextColors.AQUA, " Showing Polis Help page " + pgNo + " of " + pList.getTotalPages() + " "));
		header.append(Texts.of(TextColors.AQUA, "------------"));

		pList.setHeader(header.build());
		
		// Send List
		src.sendMessage(pList.getPage(pgNo));
		return CommandResult.success();
	}
}
