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
import io.github.hsyyid.polis.cmdexecutors.TownClaimExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownInfoExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownListExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownUnclaimAllExecutor;
import io.github.hsyyid.polis.cmdexecutors.TownUnclaimExecutor;
import io.github.hsyyid.polis.listeners.ChatListener;
import io.github.hsyyid.polis.listeners.EntitySpawnListener;
import io.github.hsyyid.polis.listeners.ExplosionEventListener;
import io.github.hsyyid.polis.listeners.PlayerBreakBlockListener;
import io.github.hsyyid.polis.listeners.PlayerDamageEventListener;
import io.github.hsyyid.polis.listeners.PlayerInteractEntityListener;
import io.github.hsyyid.polis.listeners.PlayerInteractListener;
import io.github.hsyyid.polis.listeners.PlayerMoveListener;
import io.github.hsyyid.polis.listeners.PlayerPlaceBlockListener;
import io.github.hsyyid.polis.utils.Invite;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Texts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Plugin(id = "Polis", name = "Polis", version = "1.5")
public class Polis
{
	public static Game game;
	public static ConfigurationNode config;
	public static ConfigurationLoader<CommentedConfigurationNode> configurationManager;
	public static ArrayList<Invite> invites = new ArrayList<>();
	public static Set<UUID> autoClaim = Sets.newHashSet();
	public static Set<UUID> adminAutoClaim = Sets.newHashSet();
	public static HashMap<List<String>, CommandSpec> subcommands;

	@Inject
	private Logger logger;

	public Logger getLogger()
	{
		return logger;
	}

	@Inject
	@DefaultConfig(sharedRoot = true)
	private File dConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> confManager;

	@Listener
	public void onServerInit(GameInitializationEvent event)
	{
		getLogger().info("Polis loading...");
		game = Sponge.getGame();

		// Config File
		try
		{
			if (!dConfig.exists())
			{
				dConfig.createNewFile();
				config = confManager.load();
				confManager.save(config);
			}
			configurationManager = confManager;
			config = confManager.load();

		}
		catch (IOException exception)
		{
			getLogger().error("The default configuration could not be loaded or created!");
		}

		subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("help"), CommandSpec.builder()
			.description(Texts.of("Help Command"))
			.permission("polis.help")
			.arguments(GenericArguments.optional(GenericArguments.integer(Texts.of("page no"))))
			.executor(new HelpExecutor())
			.build());
		
		subcommands.put(Arrays.asList("join"), CommandSpec.builder()
			.description(Texts.of("Join Town Command"))
			.permission("polis.join")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new JoinTownExecutor())
			.build());

		subcommands.put(Arrays.asList("sethq"), CommandSpec.builder()
			.description(Texts.of("Set Town HQ Command"))
			.permission("polis.hq.set")
			.executor(new SetHQExecutor())
			.build());
		
		subcommands.put(Arrays.asList("adminclaim"), CommandSpec.builder()
			.description(Texts.of("Admin Claim Command"))
			.permission("polis.claim.admin")
			.executor(new AdminClaimExecutor())
			.build());
		
		subcommands.put(Arrays.asList("adminautoclaim"), CommandSpec.builder()
			.description(Texts.of("Admin Auto-Claim Command"))
			.permission("polis.autoclaim.admin")
			.executor(new AdminAutoClaimExecutor())
			.build());
		
		subcommands.put(Arrays.asList("adminunclaim"), CommandSpec.builder()
			.description(Texts.of("Admin Un-Claim Command"))
			.permission("polis.unclaim.admin")
			.executor(new AdminUnClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("hq"), CommandSpec.builder()
			.description(Texts.of("Teleport to Town HQ Command"))
			.permission("polis.hq.use")
			.executor(new HQExecutor())
			.build());

		subcommands.put(Arrays.asList("invite"), CommandSpec.builder()
			.description(Texts.of("Towny Invite Command"))
			.permission("polis.invite")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Texts.of("player"))))
			.executor(new InviteExecutor())
			.build());
		
		subcommands.put(Arrays.asList("addusable"), CommandSpec.builder()
			.description(Texts.of("Polis Add Interactable Command"))
			.permission("polis.safezone.addusable")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Texts.of("id"))))
			.executor(new AddUsableExecutor())
			.build());
		
		subcommands.put(Arrays.asList("removeusable"), CommandSpec.builder()
			.description(Texts.of("Polis Remove Interactable Command"))
			.permission("polis.safezone.removeusable")
			.arguments(GenericArguments.onlyOne(GenericArguments.remainingJoinedStrings(Texts.of("id"))))
			.executor(new RemoveUsableExecutor())
			.build());

		subcommands.put(Arrays.asList("addenemy"), CommandSpec.builder()
			.description(Texts.of("Add Enemy Command"))
			.permission("polis.enemy.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new AddEnemyExecutor())
			.build());

		subcommands.put(Arrays.asList("removeenemy"), CommandSpec.builder()
			.description(Texts.of("Remove Enemy Command"))
			.permission("polis.enemy.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new RemoveEnemyExecutor())
			.build());

		subcommands.put(Arrays.asList("kick"), CommandSpec.builder()
			.description(Texts.of("Kick Member Command"))
			.permission("polis.kick.use")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Texts.of("player"))))
			.executor(new KickMemberExecutor())
			.build());

		subcommands.put(Arrays.asList("addally"), CommandSpec.builder()
			.description(Texts.of("Add Ally Command"))
			.permission("polis.ally.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new AddAllyExecutor())
			.build());

		subcommands.put(Arrays.asList("removeally"), CommandSpec.builder()
			.description(Texts.of("Remove Ally Command"))
			.permission("polis.ally.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new RemoveAllyExecutor())
			.build());

		subcommands.put(Arrays.asList("leave"), CommandSpec.builder()
			.description(Texts.of("Leave Town Command"))
			.permission("polis.leave")
			.executor(new LeaveTownExecutor())
			.build());

		subcommands.put(Arrays.asList("claim"), CommandSpec.builder()
			.description(Texts.of("Claim Command"))
			.permission("polis.claim.use")
			.executor(new TownClaimExecutor())
			.build());
		
		subcommands.put(Arrays.asList("autoclaim"), CommandSpec.builder()
			.description(Texts.of("AutoClaim Command"))
			.permission("polis.autoclaim")
			.executor(new AutoClaimExecutor())
			.build());

		subcommands.put(Arrays.asList("unclaim"), CommandSpec.builder()
			.description(Texts.of("Un-Claim Command"))
			.permission("polis.unclaim.use")
			.executor(new TownUnclaimExecutor())
			.build());

		subcommands.put(Arrays.asList("unclaimall"), CommandSpec.builder()
			.description(Texts.of("Un-Claim All Command"))
			.permission("polis.unclaim.all")
			.executor(new TownUnclaimAllExecutor())
			.build());

		subcommands.put(Arrays.asList("delete"), CommandSpec.builder()
			.description(Texts.of("Delete Town Command"))
			.permission("polis.delete")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new DeleteTownExecutor())
			.build());

		subcommands.put(Arrays.asList("disband"), CommandSpec.builder()
			.description(Texts.of("Disband Town Command"))
			.permission("polis.disband")
			.executor(new DisbandTownExecutor())
			.build());

		subcommands.put(Arrays.asList("info"), CommandSpec.builder()
			.description(Texts.of("Town Info Command"))
			.permission("polis.info")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new TownInfoExecutor())
			.build());

		subcommands.put(Arrays.asList("list"), CommandSpec.builder()
			.description(Texts.of("Town List Command"))
			.permission("polis.list")
			.executor(new TownListExecutor())
			.build());

		subcommands.put(Arrays.asList("create"), CommandSpec.builder()
			.description(Texts.of("Create Town Command"))
			.permission("polis.add")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name"))))
			.executor(new CreateTownExecutor())
			.build());

		subcommands.put(Arrays.asList("setleader"), CommandSpec.builder()
			.description(Texts.of("Set Leader of Town Command"))
			.permission("polis.leader.set")
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.player(Texts.of("player"))),
				GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name")))))
			.executor(new SetLeaderExecutor())
			.build());

		subcommands.put(Arrays.asList("addexecutive"), CommandSpec.builder()
			.description(Texts.of("Adds Executive of Town Command"))
			.permission("polis.executive.add")
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.player(Texts.of("player"))),
				GenericArguments.onlyOne(GenericArguments.string(Texts.of("town name")))))
			.executor(new AddExecutiveExecutor())
			.build());

		subcommands.put(Arrays.asList("removeexecutive"), CommandSpec.builder()
			.description(Texts.of("Remove Executive of Town Command"))
			.permission("polis.executive.remove")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Texts.of("player"))))
			.executor(new RemoveExecutiveExecutor())
			.build());
		
		CommandSpec polisCommandSpec = CommandSpec.builder()
			.description(Texts.of("Polis Command"))
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

		getLogger().info("-----------------------------");
		getLogger().info("Polis was made by HassanS6000!");
		getLogger().info("Please post all errors on the Sponge Thread or on GitHub!");
		getLogger().info("Have fun, and enjoy! :D");
		getLogger().info("-----------------------------");
		getLogger().info("Polis loaded!");
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configurationManager;
	}
}
