[![Build Status](https://travis-ci.org/UnAfraid/TelegramBotsAPIs.svg?branch=master)](https://travis-ci.org/UnAfraid/TelegramBotsAPIs)
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
    <version>1.0.3</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.github.unafraid.telegram-apis/InlineMenuAPI -->
<dependency>
    <groupId>com.github.unafraid.telegram-apis</groupId>
    <artifactId>InlineMenuAPI</artifactId>
    <version>1.0.3</version>
</dependency>
```

# Gradle dependency
```gradle
<!-- https://mvnrepository.com/artifact/com.github.unafraid.telegram-apis/CoreAPI -->
compile group: 'com.github.unafraid.telegram-apis', name: 'CoreAPI', version: '1.0.2'

<!-- https://mvnrepository.com/artifact/com.github.unafraid.telegram-apis/InlineMenuAPI -->
compile group: 'com.github.unafraid.telegram-apis', name: 'InlineMenuAPI', version: '1.0.2'
```

In order to get started download the library from maven central as jar or maven/gradle dependency manager.

# Example
```java
import java.util.List;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.github.unafraid.telegrambot.bots.DefaultTelegramBot;
import com.github.unafraid.telegrambot.handlers.CommandHandlers;
import com.github.unafraid.telegrambot.handlers.ICommandHandler;
import com.github.unafraid.telegrambot.handlers.inline.AbstractInlineHandler;
import com.github.unafraid.telegrambot.handlers.inline.InlineButtonBuilder;
import com.github.unafraid.telegrambot.handlers.inline.InlineContext;
import com.github.unafraid.telegrambot.handlers.inline.InlineMenuBuilder;
import com.github.unafraid.telegrambot.handlers.inline.InlineUserData;
import com.github.unafraid.telegrambot.handlers.inline.events.InlineCallbackEvent;
import com.github.unafraid.telegrambot.util.BotUtil;

public class Main
{
	private static final String TOKEN = "123455:abcd..";
	private static final String USERNAME = "MyBot";
	
	public void main(String[] args) throws Exception
	{
		// Initialize API Context
		ApiContextInitializer.init();
		
		// Create new instance of TelegramBotsAPI
		final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		
		// Register the default bot with token and username
		telegramBotsApi.registerBot(new DefaultTelegramBot(TOKEN, USERNAME));
		
		// Register handlers
		CommandHandlers.getInstance().addHandler(new StartCommand());
		CommandHandlers.getInstance().addHandler(new ExampleInlineMenu());
	}
	
	/**
	 * Very basic command handler accepts command starting with /start
	 */
	class StartCommand implements ICommandHandler
	{
		@Override
		public String getCommand()
		{
			return "/start";
		}
		
		@Override
		public String getUsage()
		{
			return "/start";
		}
		
		@Override
		public String getDescription()
		{
			return "The initial command that you send when you start talking to a bot";
		}
		
		@Override
		public void onCommandMessage(AbsSender bot, Update update, Message message, List<String> args) throws TelegramApiException
		{
			final StringBuilder sb = new StringBuilder();
			sb.append("Hello @").append(message.getFrom().getUserName()).append(", how are ya doin'?").append(System.lineSeparator());
			sb.append("Type in /menu to see my cool inline menus!");
			BotUtil.sendMessage(bot, message, sb.toString(), true, false, null);
		}
	}
	
	/**
	 * Very basic inline menu handler, accepts command starting with /menu
	 */
	class ExampleInlineMenu extends AbstractInlineHandler
	{
		@Override
		public boolean validate(User from)
		{
			return true;
		}
		
		@Override
		public String getUsage()
		{
			return "/menu";
		}
		
		@Override
		public String getDescription()
		{
			return "Renders static menu";
		}
		
		@Override
		public String getCommand()
		{
			return "/menu";
		}
		
		@Override
		public void registerMenu(InlineContext ctx, InlineMenuBuilder builder)
		{
			//@formatter:off
			builder.button(new InlineButtonBuilder(ctx)
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
		
		private boolean handleButtonClick(InlineCallbackEvent event) throws TelegramApiException
		{
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
