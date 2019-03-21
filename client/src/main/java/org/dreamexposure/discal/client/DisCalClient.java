package org.dreamexposure.discal.client;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import org.dreamexposure.discal.client.listeners.discal.CrossTalkEventListener;
import org.dreamexposure.discal.client.listeners.discord.ReadyEventListener;
import org.dreamexposure.discal.client.message.MessageManager;
import org.dreamexposure.discal.client.module.command.*;
import org.dreamexposure.discal.core.database.DatabaseManager;
import org.dreamexposure.discal.core.logger.Logger;
import org.dreamexposure.discal.core.network.google.Authorization;
import org.dreamexposure.discal.core.object.BotSettings;
import org.dreamexposure.novautils.event.EventManager;
import org.dreamexposure.novautils.network.crosstalk.ClientSocketHandler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DisCalClient {
	private static DiscordClient client;

	public static void main(String[] args) throws IOException {
		//Get settings
		Properties p = new Properties();
		p.load(new FileReader(new File("settings.properties")));
		BotSettings.init(p);

		//Init logger
		Logger.getLogger().init();

		//Handle client setup
		client = createClient();

		//Register discord events
		client.getEventDispatcher().on(ReadyEvent.class).subscribe(ReadyEventListener::handle);

		//Register discal events
		EventManager.get().init();
		EventManager.get().getEventBus().register(new CrossTalkEventListener());

		//Register commands
		CommandExecutor executor = CommandExecutor.getExecutor().enable();
		executor.registerCommand(new HelpCommand());
		executor.registerCommand(new DisCalCommand());
		executor.registerCommand(new CalendarCommand());
		executor.registerCommand(new AddCalendarCommand());
		executor.registerCommand(new TimeCommand());
		executor.registerCommand(new LinkCalendarCommand());
		executor.registerCommand(new EventListCommand());
		executor.registerCommand(new EventCommand());
		executor.registerCommand(new RsvpCommand());
		executor.registerCommand(new AnnouncementCommand());
		executor.registerCommand(new DevCommand());

		//Connect to MySQL
		DatabaseManager.getManager().connectToMySQL();
		DatabaseManager.getManager().createTables();

		//Start Google authorization daemon
		Authorization.getAuth().init();

		//Load lang files
		MessageManager.reloadLangs();

		//Start CrossTalk client
		ClientSocketHandler.setValues(BotSettings.CROSSTALK_SERVER_HOST.get(), Integer.valueOf(BotSettings.CROSSTALK_SERVER_PORT.get()), BotSettings.CROSSTALK_CLIENT_HOST.get(), Integer.valueOf(BotSettings.CROSSTALK_CLIENT_PORT.get()));

		ClientSocketHandler.initListener();

		//Login
		client.login().block();
	}

	/**
	 * Creates the DisCal bot client.
	 *
	 * @return The client if successful, otherwise <code>null</code>.
	 */
	private static DiscordClient createClient() {
		DiscordClientBuilder clientBuilder = new DiscordClientBuilder(BotSettings.TOKEN.get());
		//Handle shard count and index.
		clientBuilder.setShardIndex(Integer.valueOf(BotSettings.SHARD_INDEX.get()));
		clientBuilder.setShardCount(Integer.valueOf(BotSettings.SHARD_COUNT.get()));

		//Redis info + store service
		//String redisInfo = String.format("redis://%s@%s:%s", BotSettings.REDIS_PASSWORD.get(), BotSettings.REDIS_HOSTNAME.get(), BotSettings.REDIS_PORT.get());
		//RedisClient rc = RedisClient.create(redisInfo);
		//RedisStoreService rss = new RedisStoreService(rc);
		//clientBuilder.setStoreService(rss);

		return clientBuilder.build();
	}

	//Public stuffs
	public static DiscordClient getClient() {
		return client;
	}
}