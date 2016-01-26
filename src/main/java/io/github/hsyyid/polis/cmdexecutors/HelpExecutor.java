package io.github.hsyyid.polis.cmdexecutors;

import com.google.common.collect.Lists;
import io.github.hsyyid.polis.Polis;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.List;

public class HelpExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{	
		HashMap<List<String>, CommandSpec> polisSubCommands = Polis.subcommands;
		List<Text> helpList = Lists.newArrayList();

        for (List<String> aliases : polisSubCommands.keySet()) {
            CommandSpec commandSpec = polisSubCommands.get(aliases);
            Text commandHelp = Text.builder()
                    .append(Text.builder()
                            .append(Text.of(TextColors.GOLD, "Command: "))
                            .append(Text.of(aliases.toString(), "\n"))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.GOLD, "Command Description: "), commandSpec.getShortDescription(src).get(), Text.of("\n"))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.GOLD, "Command Arguments: "), commandSpec.getUsage(src), Text.of("\n"))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.GOLD, "Permission: "), Text.of(commandSpec.testPermission(src)), Text.of("\n"))
                            .build())
                    .append(Text.builder()
                            .append(Text.of(TextColors.GOLD, "Permission Node: "), 
                                    Text.of(commandSpec.toString().substring(commandSpec.toString().lastIndexOf("permission") + 11, 
                                            commandSpec.toString().indexOf("argumentParser") - 2)), 
                                    Text.of("\n"))
                            .build())
                    .build();
            helpList.add(commandHelp);
        }

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationBuilder paginationBuilder = paginationService.builder().title(Text.of(TextColors.AQUA, "Showing Polis Help")).paddingString("-").contents(helpList);
        paginationBuilder.sendTo(src);
        return CommandResult.success();
	}
}
