[![Build Status](https://github.com/UnAfraid/TelegramBotsAPIs/actions/workflows/test.yml/badge.svg)](https://github.com/UnAfraid/TelegramBotsAPIs/actions/workflows/test.yml)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](https://github.com/rubenlagus/TelegramBots/blob/master/LICENSE)

# InlineMenus
Allows creation of Inline Menus and bot command handlers

This library depends on https://github.com/rubenlagus/TelegramBots, check it out for more telegram bot implementation details, also https://core.telegram.org/bots/api for telegram bots API details

# Maven dependency
```xml
<!-- https://mvnrepository.com/artifact/com.github.unafraid.telegram-apis/CoreAPI -->
<dependency>
    <groupId>com.github.unafraid.telegram-apis</groupId>
    <artifactId>CoreAPI</artifactId>
    <version>1.0.15</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.github.unafraid.telegram-apis/InlineMenuAPI -->
<dependency>
    <groupId>com.github.unafraid.telegram-apis</groupId>
    <artifactId>InlineMenuAPI</artifactId>
    <version>1.0.15</version>
</dependency>
```

# Gradle dependency
```gradle
// https://mvnrepository.com/artifact/com.github.unafraid.telegram-apis/CoreAPI
implementation("com.github.unafraid.telegram-apis:CoreAPI:1.0.15")

// https://mvnrepository.com/artifact/com.github.unafraid.telegram-apis/InlineMenuAPI
implementation("com.github.unafraid.telegram-apis:InlineMenuAPI:1.0.15")
```

In order to get started download the library from maven central as jar or maven/gradle dependency manager.

# Example
```java
import java.util.List;

import com.github.unafraid.telegrambot.bots.AbstractTelegramBot;
import com.github.unafraid.telegrambot.bots.DefaultTelegramBot;
import com.github.unafraid.telegrambot.handlers.IAccessLevelValidator;
import com.github.unafraid.telegrambot.handlers.ICommandHandler;
import com.github.unafraid.telegrambot.handlers.ITelegramHandler;
import com.github.unafraid.telegrambot.handlers.inline.AbstractInlineHandler;
import com.github.unafraid.telegrambot.handlers.inline.InlineButtonBuilder;
import com.github.unafraid.telegrambot.handlers.inline.InlineContext;
import com.github.unafraid.telegrambot.handlers.inline.InlineMenuBuilder;
import com.github.unafraid.telegrambot.handlers.inline.InlineUserData;
import com.github.unafraid.telegrambot.handlers.inline.events.InlineCallbackEvent;
import com.github.unafraid.telegrambot.util.BotUtil;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
	private static final String TOKEN = "123455:abcd..";
	private static final String USERNAME = "MyBot";
	
	public static void main(String[] args) throws Exception {
		// Create new instance of TelegramBotsAPI
		final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		
		// Register the default bot with token and username
		final DefaultTelegramBot telegramBot = new DefaultTelegramBot(TOKEN, USERNAME);
		telegramBotsApi.registerBot(telegramBot);
		
		// Register access level validator
		telegramBot.setAccessLevelValidator(new AccessLevelValidator());
		
		// Register handlers
		telegramBot.addHandler(new StartCommand());
		telegramBot.addHandler(new ExampleInlineMenu());
	}
	
	/**
	 * Very simple access level validator class<br />
	 * Note: This is just an example implementation, database verification is advised
	 */
	static class AccessLevelValidator implements IAccessLevelValidator {
		@Override
		public boolean validate(ITelegramHandler handler, User user) {
			if (handler.getRequiredAccessLevel() == 0) {
				return true;
			}
			
			// Database validation
			// TODO: Database validation
			
			// In this example we gonna use required access level 1 to ensure user has set their UserName
			if (handler.getRequiredAccessLevel() == 1 && user.getUserName() != null && !user.getUserName().isEmpty()) {
				return true;
			}
			
			// Refuse access
			return false;
		}
	}
	
	/**
	 * Very basic command handler accepts command starting with /start
	 */
	static class StartCommand implements ICommandHandler {
		@Override
		public String getCommand() {
			return "/start";
		}
		
		@Override
		public String getUsage() {
			return "/start";
		}
		
		@Override
		public String getDescription() {
			return "The initial command that you send when you start talking to a bot";
		}
		
		@Override
		public int getRequiredAccessLevel() {
			return 0;
		}
		
		@Override
		public void onCommandMessage(AbstractTelegramBot bot, Update update, Message message, List<String> args) throws TelegramApiException {
			final StringBuilder sb = new StringBuilder();
			if (message.getFrom().getUserName() == null || message.getFrom().getUserName().isEmpty()) {
				sb.append("Hello ").append(message.getFrom().getFirstName()).append(", how are ya doin'?").append(System.lineSeparator());
				sb.append("You may want to set an UserName in order to access /menu command").append(System.lineSeparator());
			} else {
				sb.append("Hello @").append(message.getFrom().getUserName()).append(", how are ya doin'?").append(System.lineSeparator());
				sb.append("Type in /menu to see my cool inline menus!");
			}
			BotUtil.sendMessage(bot, message, sb.toString(), true, false, null);
		}
	}
	
	/**
	 * Very basic inline menu handler, accepts command starting with /menu
	 */
	static class ExampleInlineMenu extends AbstractInlineHandler {
		@Override
		public String getUsage() {
			return "/menu";
		}
		
		@Override
		public String getDescription() {
			return "Renders static menu";
		}
		
		@Override
		public String getCommand() {
			return "/menu";
		}
		
		@Override
		public int getRequiredAccessLevel() {
			return 1;
		}
		
		@Override
		public void registerMenu(InlineContext ctx, InlineMenuBuilder builder) {
			//@formatter:off
			builder.name("Main Menu")
					.button(new InlineButtonBuilder(ctx)
							.name("Button 1")
							.onQueryCallback(this::handleButtonClick)
							.build())
					.button(new InlineButtonBuilder(ctx)
							.name("Button 2")
							.onQueryCallback(this::handleButtonClick)
							.build())
					.button(new InlineButtonBuilder(ctx)
							.name("Button 3")
							.onQueryCallback(this::handleButtonClick)
							.build())
					.button(new InlineButtonBuilder(ctx)
							.name("Sub menu")
							.menu(new InlineMenuBuilder(ctx)
									.button(new InlineButtonBuilder(ctx)
											.name("Sub Button 1")
											.onQueryCallback(this::handleButtonClick)
											.build())
									.button(new InlineButtonBuilder(ctx)
											.name("Sub Button 2")
											.onQueryCallback(this::handleButtonClick)
											.build())
									.button(new InlineButtonBuilder(ctx)
											.name("Sub Button 3")
											.onQueryCallback(this::handleButtonClick)
											.build())
									.button(defaultBack(ctx))
									.build())
							.build())
					.button(defaultClose(ctx));
			//@formatter:on
		}
		
		private boolean handleButtonClick(InlineCallbackEvent event) throws TelegramApiException {
			final InlineUserData userData = event.getContext().getUserData(event.getQuery().getFrom().getId());
			final AnswerCallbackQuery answer = new AnswerCallbackQuery();
			answer.setCallbackQueryId(event.getQuery().getId());
			answer.setShowAlert(true);
			answer.setText("You've clicked at " + userData.getActiveButton().getName());
			event.getBot().execute(answer);
			return true;
		}
	}
}
```
