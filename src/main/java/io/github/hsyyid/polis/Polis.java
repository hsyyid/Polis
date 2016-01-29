package io.github.hsyyid.polis;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.github.hsyyid.polis.cmdexecutors.AddAllyExecutor;
import io.github.hsyyid.polis.cmdexecutors.AddEnemyExecutor;
import io.github.hsyyid.polis.cmdexecutors.AddExecutiveExecutor;
import io.github.hsyyid.polis.cmdexecutors.AddUsableExecutor;
import io.github.hsyyid.polis.cmdexecutors.AdminAutoClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.AdminClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.AdminUnClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.AutoClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.CreateTownExecutor;
import io.github.hsyyid.polis.cmdexecutors.DeleteTownExecutor;
import io.github.hsyyid.polis.cmdexecutors.DisbandTownExecutor;
import io.github.hsyyid.polis.cmdexecutors.HQExecutor;
import io.github.hsyyid.polis.cmdexecutors.HelpExecutor;
import io.github.hsyyid.polis.cmdexecutors.InviteExecutor;
import io.github.hsyyid.polis.cmdexecutors.JoinTownExecutor;
import io.github.hsyyid.polis.cmdexecutors.KickMemberExecutor;
import io.github.hsyyid.polis.cmdexecutors.LeaveTownExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisExecutor;
import io.github.hsyyid.polis.cmdexecutors.RemoveAllyExecutor;
import io.github.hsyyid.polis.cmdexecutors.RemoveEnemyExecutor;
import io.github.hsyyid.polis.cmdexecutors.RemoveExecutiveExecutor;
import io.github.hsyyid.polis.cmdexecutors.RemoveUsableExecutor;
import io.github.hsyyid.polis.cmdexecutors.SetHQExecutor;
import io.github.hsyyid.polis.cmdexecutors.SetLeaderExecutor;
import io.github.hsyyid.polis.cmdexecutors.SetTaxExecutor;
import io.github.hsyyid.polis.cmdexecutors.SetTaxIntervalExecutor;
import io.github.hsyyid.polis.cmdexecutors.ToggleAdminBypassExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownDepositExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownInfoExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownListExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownToggleTaxExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownUnclaimAllExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownUnclaimExecutor;
import io.github.hsyyid.polis.config.Config;
import io.github.hsyyid.polis.config.TeamsConfig;
import io.github.hsyyid.polis.listeners.ChatListener;
import io.github.hsyyid.polis.listeners.EntitySpawnListener;
import io.github.hsyyid.polis.listeners.ExplosionEventListener;
import io.github.hsyyid.polis.listeners.PlayerBreakBlockListener;
import io.github.hsyyid.polis.listeners.PlayerDamageEventListener;
import io.github.hsyyid.polis.listeners.PlayerDropItemListener;
import io.github.hsyyid.polis.listeners.PlayerInteractEntityListener;
import io.github.hsyyid.polis.listeners.PlayerInteractListener;
import io.github.hsyyid.polis.listeners.PlayerMoveListener;
import io.github.hsyyid.polis.listeners.PlayerPlaceBlockListener;
import io.github.hsyyid.polis.utils.Invite;
import io.github.hsyyid.polis.utils.Utils;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Plugin(id = "Polis", name = "Polis", version = "1.9")
public class Polis
{
	protected Polis()
	{
		;
	}

	private static Polis polis;

	public static Game game;
	public static ArrayList<Invite> invites = new ArrayList<>();
	public static Set<UUID> autoClaim = Sets.newHashSet();
	public static Set<UUID> adminAutoClaim = Sets.newHashSet();
	public static Set<UUID> adminBypassMode = Sets.newHashSet();
	public static HashMap<List<String>, CommandSpec> subcommands;
	public static EconomyService economyService;

	@Inject
	private Logger logger;

	public Logger getLogger()
	{
		return logger;
	}

	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;

	public static Polis getPolis()
	{
		return polis;
	}

	@Listener
	public void onServerInit(GameInitializationEvent event)
	{
		getLogger().info("Polis loading...");
		polis = this;
		game = Sponge.getGame();

		// Config File
		// Create Config Directory for Polis
		if (!Files.exists(configDir))
		{
			try
			{
				Files.createDirectories(configDir);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Create data Directory for Polis
		if (!Files.exists(configDir.resolve("data")))
		{
			try
			{
				Files.createDirectories(configDir.resolve("data"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Create config.conf
		Config.getConfig().setup();
		// Create teams.conf
		TeamsConfig.getConfig().setup();
		// Start Tax Service
		Utils.startTaxService();

		subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("help"), CommandSpec.builder()
			.description(Text.of("Help Command"))
			.permission("polis.help")
			.arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page no"))))
			.executor(new HelpExecutor())
			.build());

		subcommands.put(Arrays.asList("toggleadminbypass"), CommandSpec.builder()
			.description(Text.of("Toggle Admin-Bypass Command"))
			.permission("polis.adminbypass.toggle")
			.executor(new ToggleAdminBypassExecutor())
			.build());

		subcommands.put(Arrays.asList("join"), CommandSpec.builder()
			.description(Text.of("Join Town Command"))
			.permission("polis.join")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new JoinTownExecutor())
			.build());

		subcommands.put(Arrays.asList("sethq"), CommandSpec.builder()
			.description(Text.of("Set Town HQ Command"))
			.permission("polis.hq.set")
			.executor(new SetHQExecutor())
			.build());

		subcommands.put(Arrays.asList("adminclaim"), CommandSpec.builder()
			.description(Text.of("Admin Claim Command"))
			.permission("polis.claim.admin")
			.executor(new AdminClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("adminautoclaim"), CommandSpec.builder()
			.description(Text.of("Admin Auto-Claim Command"))
			.permission("polis.autoclaim.admin")
			.executor(new AdminAutoClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("adminunclaim"), CommandSpec.builder()
			.description(Text.of("Admin Un-Claim Command"))
			.permission("polis.unclaim.admin")
			.executor(new AdminUnClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("hq"), CommandSpec.builder()
			.description(Text.of("Teleport to Town HQ Command"))
			.permission("polis.hq.use")
			.executor(new HQExecutor())
			.build());

		subcommands.put(Arrays.asList("invite"), CommandSpec.builder()
			.description(Text.of("Towny Invite Command"))
			.permission("polis.invite")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new InviteExecutor())
			.build());

		subcommands.put(Arrays.asList("addusable"), CommandSpec.builder()
			.description(Text.of("Polis Add Interactable Command"))
			.permission("polis.safezone.addusable")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("id"))))
			.executor(new AddUsableExecutor())
			.build());

		subcommands.put(Arrays.asList("removeusable"), CommandSpec.builder()
			.description(Text.of("Polis Remove Interactable Command"))
			.permission("polis.safezone.removeusable")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Text.of("id"))))
			.executor(new RemoveUsableExecutor())
			.build());

		subcommands.put(Arrays.asList("addenemy"), CommandSpec.builder()
			.description(Text.of("Add Enemy Command"))
			.permission("polis.enemy.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new AddEnemyExecutor())
			.build());

		subcommands.put(Arrays.asList("removeenemy"), CommandSpec.builder()
			.description(Text.of("Remove Enemy Command"))
			.permission("polis.enemy.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new RemoveEnemyExecutor())
			.build());

		subcommands.put(Arrays.asList("kick"), CommandSpec.builder()
			.description(Text.of("Kick Member Command"))
			.permission("polis.kick.use")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new KickMemberExecutor())
			.build());

		subcommands.put(Arrays.asList("addally"), CommandSpec.builder()
			.description(Text.of("Add Ally Command"))
			.permission("polis.ally.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new AddAllyExecutor())
			.build());

		subcommands.put(Arrays.asList("removeally"), CommandSpec.builder()
			.description(Text.of("Remove Ally Command"))
			.permission("polis.ally.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new RemoveAllyExecutor())
			.build());

		subcommands.put(Arrays.asList("leave"), CommandSpec.builder()
			.description(Text.of("Leave Town Command"))
			.permission("polis.leave")
			.executor(new LeaveTownExecutor())
			.build());

		subcommands.put(Arrays.asList("claim"), CommandSpec.builder()
			.description(Text.of("Claim Command"))
			.permission("polis.claim.use")
			.executor(new TownClaimExecutor())
			.build());
		
		subcommands.put(Arrays.asList("deposit"), CommandSpec.builder()
			.description(Text.of("Polis Deposit Command"))
			.arguments(GenericArguments.onlyOne(GenericArguments.doubleNum(Text.of("amount"))))
			.permission("polis.deposit.use")
			.executor(new TownDepositExecutor())
			.build());

		subcommands.put(Arrays.asList("autoclaim"), CommandSpec.builder()
			.description(Text.of("AutoClaim Command"))
			.permission("polis.autoclaim")
			.executor(new AutoClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("unclaim"), CommandSpec.builder()
			.description(Text.of("Un-Claim Command"))
			.permission("polis.unclaim.use")
			.executor(new TownUnclaimExecutor())
			.build());

		subcommands.put(Arrays.asList("unclaimall"), CommandSpec.builder()
			.description(Text.of("Un-Claim All Command"))
			.permission("polis.unclaim.all")
			.executor(new TownUnclaimAllExecutor())
			.build());

		subcommands.put(Arrays.asList("delete"), CommandSpec.builder()
			.description(Text.of("Delete Town Command"))
			.permission("polis.delete")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new DeleteTownExecutor())
			.build());

		subcommands.put(Arrays.asList("disband"), CommandSpec.builder()
			.description(Text.of("Disband Town Command"))
			.permission("polis.disband")
			.executor(new DisbandTownExecutor())
			.build());

		subcommands.put(Arrays.asList("info"), CommandSpec.builder()
			.description(Text.of("Town Info Command"))
			.permission("polis.info")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new TownInfoExecutor())
			.build());

		subcommands.put(Arrays.asList("list"), CommandSpec.builder()
			.description(Text.of("Town List Command"))
			.permission("polis.list")
			.executor(new TownListExecutor())
			.build());

		subcommands.put(Arrays.asList("create"), CommandSpec.builder()
			.description(Text.of("Create Town Command"))
			.permission("polis.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new CreateTownExecutor())
			.build());

		subcommands.put(Arrays.asList("setleader"), CommandSpec.builder()
			.description(Text.of("Set Leader of Town Command"))
			.permission("polis.leader.set")
			.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))), GenericArguments.onlyOne(GenericArguments.string(Text.of("town name")))))
			.executor(new SetLeaderExecutor())
			.build());

		subcommands.put(Arrays.asList("addexecutive"), CommandSpec.builder()
			.description(Text.of("Adds Executive of Town Command"))
			.permission("polis.executive.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new AddExecutiveExecutor())
			.build());

		subcommands.put(Arrays.asList("removeexecutive"), CommandSpec.builder()
			.description(Text.of("Remove Executive of Town Command"))
			.permission("polis.executive.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new RemoveExecutiveExecutor())
			.build());
		
		subcommands.put(Arrays.asList("toggletax", "toggletaxes"), CommandSpec.builder()
			.description(Text.of("Toggle Taxes Command"))
			.permission("polis.taxes.toggle")
			.arguments(GenericArguments.onlyOne(GenericArguments.bool(Text.of("toggle"))))
			.executor(new TownToggleTaxExecutor())
			.build());
		
		subcommands.put(Arrays.asList("settax"), CommandSpec.builder()
			.description(Text.of("Set Taxes of Town Command"))
			.permission("polis.taxes.set")
			.arguments(GenericArguments.onlyOne(GenericArguments.doubleNum(Text.of("tax"))))
			.executor(new SetTaxExecutor())
			.build());
		
		subcommands.put(Arrays.asList("settaxinterval"), CommandSpec.builder()
			.description(Text.of("Sets Tax Interval of Town Command"))
			.permission("polis.taxes.interval.set")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("duration"))))
			.executor(new SetTaxIntervalExecutor())
			.build());

		CommandSpec polisCommandSpec = CommandSpec.builder()
			.description(Text.of("Polis Command"))
			.permission("polis.use")
			.executor(new PolisExecutor())
			.children(subcommands)
			.build();

		game.getCommandManager().register(this, polisCommandSpec, "polis", "p");

		game.getEventManager().registerListeners(this, new PlayerInteractListener());
		game.getEventManager().registerListeners(this, new PlayerBreakBlockListener());
		game.getEventManager().registerListeners(this, new PlayerPlaceBlockListener());
		game.getEventManager().registerListeners(this, new PlayerMoveListener());
		game.getEventManager().registerListeners(this, new PlayerInteractEntityListener());
		game.getEventManager().registerListeners(this, new ChatListener());
		game.getEventManager().registerListeners(this, new EntitySpawnListener());
		game.getEventManager().registerListeners(this, new ExplosionEventListener());
		game.getEventManager().registerListeners(this, new PlayerDamageEventListener());
		game.getEventManager().registerListeners(this, new PlayerDropItemListener());

		getLogger().info("-----------------------------");
		getLogger().info("Polis was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("Polis loaded!");
	}
	
	@Listener
	public void onGamePostInit(GamePostInitializationEvent event)
	{
		Optional<EconomyService> econService = Sponge.getServiceManager().provide(EconomyService.class);
		
		if(econService.isPresent())
		{
			economyService = econService.get();
		}
		else
		{
			getLogger().error("No economy plugin was found! Polis will not work correctly!");
		}
	}

	public Path getConfigDir()
	{
		return configDir;
	}
}
