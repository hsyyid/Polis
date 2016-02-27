package io.github.hsyyid.polis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.github.hsyyid.polis.cmdexecutors.AddUsableExecutor;
import io.github.hsyyid.polis.cmdexecutors.AdminAutoClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.AdminClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.AdminUnClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.AutoClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisAddAllyExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisAddEnemyExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisAddExecutiveExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisChatExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisCreateExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisDeleteExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisDepositExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisDisbandExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisHQExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisHelpExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisInfoExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisInviteExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisJoinExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisKickMemberExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisLeaveExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisListExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisMapExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisRemoveAllyExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisRemoveEnemyExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisRemoveExecutiveExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisSetHQExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisSetLeaderExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisSetTaxExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisSetTaxIntervalExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisToggleTaxExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisUnclaimAllExecutor;
import io.github.hsyyid.polis.cmdexecutors.PolisUnclaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.RemoveUsableExecutor;
import io.github.hsyyid.polis.cmdexecutors.ToggleAdminBypassExecutor;
import io.github.hsyyid.polis.config.Config;
import io.github.hsyyid.polis.config.TeamsConfig;
import io.github.hsyyid.polis.listeners.ChatListener;
import io.github.hsyyid.polis.listeners.EntityMoveListener;
import io.github.hsyyid.polis.listeners.EntitySpawnListener;
import io.github.hsyyid.polis.listeners.ExplosionEventListener;
import io.github.hsyyid.polis.listeners.PlayerBreakBlockListener;
import io.github.hsyyid.polis.listeners.PlayerDamageEventListener;
import io.github.hsyyid.polis.listeners.PlayerDropItemListener;
import io.github.hsyyid.polis.listeners.PlayerInteractEntityListener;
import io.github.hsyyid.polis.listeners.PlayerInteractListener;
import io.github.hsyyid.polis.listeners.PlayerPlaceBlockListener;
import io.github.hsyyid.polis.utils.Invite;
import io.github.hsyyid.polis.utils.Utils;
import me.flibio.updatifier.Updatifier;
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

@Updatifier(repoName = "Polis", repoOwner = "hsyyid", version = "v" + PluginInfo.VERSION)
@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION)
public class Polis
{
	protected Polis()
	{
		;
	}

	private static Polis polis;

	public static Game game;
	public static ArrayList<Invite> invites = Lists.newArrayList();
	public static Set<UUID> autoClaim = Sets.newHashSet();
	public static Set<UUID> polisChat = Sets.newHashSet();
	public static HashMap<UUID, String> adminAutoClaim = new HashMap<>();
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
			.executor(new PolisHelpExecutor())
			.build());

		subcommands.put(Arrays.asList("chat"), CommandSpec.builder()
			.description(Text.of("Chat Command"))
			.permission("polis.chat")
			.executor(new PolisChatExecutor())
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
			.executor(new PolisJoinExecutor())
			.build());

		subcommands.put(Arrays.asList("sethq"), CommandSpec.builder()
			.description(Text.of("Set Town HQ Command"))
			.permission("polis.hq.set")
			.executor(new PolisSetHQExecutor())
			.build());

		subcommands.put(Arrays.asList("adminclaim"), CommandSpec.builder()
			.description(Text.of("Admin Claim Command"))
			.permission("polis.claim.admin")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("zone"))))
			.executor(new AdminClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("adminautoclaim"), CommandSpec.builder()
			.description(Text.of("Admin Auto-Claim Command"))
			.permission("polis.autoclaim.admin")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("zone"))))
			.executor(new AdminAutoClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("adminunclaim"), CommandSpec.builder()
			.description(Text.of("Admin Un-Claim Command"))
			.permission("polis.unclaim.admin")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("zone"))))
			.executor(new AdminUnClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("hq"), CommandSpec.builder()
			.description(Text.of("Teleport to Town HQ Command"))
			.permission("polis.hq.use")
			.executor(new PolisHQExecutor())
			.build());

		subcommands.put(Arrays.asList("invite"), CommandSpec.builder()
			.description(Text.of("Towny Invite Command"))
			.permission("polis.invite")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new PolisInviteExecutor())
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
			.executor(new PolisAddEnemyExecutor())
			.build());

		subcommands.put(Arrays.asList("removeenemy"), CommandSpec.builder()
			.description(Text.of("Remove Enemy Command"))
			.permission("polis.enemy.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new PolisRemoveEnemyExecutor())
			.build());

		subcommands.put(Arrays.asList("map"), CommandSpec.builder()
			.description(Text.of("Polis Map Command"))
			.permission("polis.map.use")
			.executor(new PolisMapExecutor())
			.build());

		subcommands.put(Arrays.asList("kick"), CommandSpec.builder()
			.description(Text.of("Kick Member Command"))
			.permission("polis.kick.use")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new PolisKickMemberExecutor())
			.build());

		subcommands.put(Arrays.asList("addally"), CommandSpec.builder()
			.description(Text.of("Add Ally Command"))
			.permission("polis.ally.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new PolisAddAllyExecutor())
			.build());

		subcommands.put(Arrays.asList("removeally"), CommandSpec.builder()
			.description(Text.of("Remove Ally Command"))
			.permission("polis.ally.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new PolisRemoveAllyExecutor())
			.build());

		subcommands.put(Arrays.asList("leave"), CommandSpec.builder()
			.description(Text.of("Leave Town Command"))
			.permission("polis.leave")
			.executor(new PolisLeaveExecutor())
			.build());

		subcommands.put(Arrays.asList("claim"), CommandSpec.builder()
			.description(Text.of("Claim Command"))
			.permission("polis.claim.use")
			.executor(new PolisClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("deposit"), CommandSpec.builder()
			.description(Text.of("Polis Deposit Command"))
			.arguments(GenericArguments.onlyOne(GenericArguments.doubleNum(Text.of("amount"))))
			.permission("polis.deposit.use")
			.executor(new PolisDepositExecutor())
			.build());

		subcommands.put(Arrays.asList("autoclaim"), CommandSpec.builder()
			.description(Text.of("AutoClaim Command"))
			.permission("polis.autoclaim")
			.executor(new AutoClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("unclaim"), CommandSpec.builder()
			.description(Text.of("Un-Claim Command"))
			.permission("polis.unclaim.use")
			.executor(new PolisUnclaimExecutor())
			.build());

		subcommands.put(Arrays.asList("unclaimall"), CommandSpec.builder()
			.description(Text.of("Un-Claim All Command"))
			.permission("polis.unclaim.all")
			.executor(new PolisUnclaimAllExecutor())
			.build());

		subcommands.put(Arrays.asList("delete"), CommandSpec.builder()
			.description(Text.of("Delete Town Command"))
			.permission("polis.delete")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new PolisDeleteExecutor())
			.build());

		subcommands.put(Arrays.asList("disband"), CommandSpec.builder()
			.description(Text.of("Disband Town Command"))
			.permission("polis.disband")
			.executor(new PolisDisbandExecutor())
			.build());

		subcommands.put(Arrays.asList("info"), CommandSpec.builder()
			.description(Text.of("Town Info Command"))
			.permission("polis.info")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new PolisInfoExecutor())
			.build());

		subcommands.put(Arrays.asList("list"), CommandSpec.builder()
			.description(Text.of("Town List Command"))
			.permission("polis.list")
			.executor(new PolisListExecutor())
			.build());

		subcommands.put(Arrays.asList("create"), CommandSpec.builder()
			.description(Text.of("Create Town Command"))
			.permission("polis.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("town name"))))
			.executor(new PolisCreateExecutor())
			.build());

		subcommands.put(Arrays.asList("setleader"), CommandSpec.builder()
			.description(Text.of("Set Leader of Town Command"))
			.permission("polis.leader.set")
			.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))), GenericArguments.onlyOne(GenericArguments.string(Text.of("town name")))))
			.executor(new PolisSetLeaderExecutor())
			.build());

		subcommands.put(Arrays.asList("addexecutive"), CommandSpec.builder()
			.description(Text.of("Adds Executive of Town Command"))
			.permission("polis.executive.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new PolisAddExecutiveExecutor())
			.build());

		subcommands.put(Arrays.asList("removeexecutive"), CommandSpec.builder()
			.description(Text.of("Remove Executive of Town Command"))
			.permission("polis.executive.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new PolisRemoveExecutiveExecutor())
			.build());

		subcommands.put(Arrays.asList("toggletax", "toggletaxes"), CommandSpec.builder()
			.description(Text.of("Toggle Taxes Command"))
			.permission("polis.taxes.toggle")
			.arguments(GenericArguments.onlyOne(GenericArguments.bool(Text.of("toggle"))))
			.executor(new PolisToggleTaxExecutor())
			.build());

		subcommands.put(Arrays.asList("settax"), CommandSpec.builder()
			.description(Text.of("Set Taxes of Town Command"))
			.permission("polis.taxes.set")
			.arguments(GenericArguments.onlyOne(GenericArguments.doubleNum(Text.of("tax"))))
			.executor(new PolisSetTaxExecutor())
			.build());

		subcommands.put(Arrays.asList("settaxinterval"), CommandSpec.builder()
			.description(Text.of("Sets Tax Interval of Town Command"))
			.permission("polis.taxes.interval.set")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("duration"))))
			.executor(new PolisSetTaxIntervalExecutor())
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
		game.getEventManager().registerListeners(this, new EntityMoveListener());
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

		if (econService.isPresent())
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
