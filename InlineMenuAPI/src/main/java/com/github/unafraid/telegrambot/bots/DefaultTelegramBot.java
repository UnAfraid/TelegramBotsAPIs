/*
 * Copyright (c) 2017 Rumen Nikiforov <unafraid89@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.unafraid.telegrambot.bots;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import com.github.unafraid.telegrambot.handlers.CommandHandlers;
import com.github.unafraid.telegrambot.handlers.IAccessLevelHandler;
import com.github.unafraid.telegrambot.handlers.ICallbackQueryHandler;
import com.github.unafraid.telegrambot.handlers.IChosenInlineQueryHandler;
import com.github.unafraid.telegrambot.handlers.ICommandHandler;
import com.github.unafraid.telegrambot.handlers.IDocumentMessageHandler;
import com.github.unafraid.telegrambot.handlers.IEditedMessageHandler;
import com.github.unafraid.telegrambot.handlers.IInlineQueryHandler;
import com.github.unafraid.telegrambot.handlers.IMessageHandler;
import com.github.unafraid.telegrambot.util.BotUtil;
import com.github.unafraid.telegrambot.util.IThrowableFunction;

/**
 * Default Telegram bot implementation, handles all updates and sends notification to registered handlers.
 * @author UnAfraid
 */
public class DefaultTelegramBot extends TelegramLongPollingBot
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTelegramBot.class);
	private static final Pattern COMMAND_ARGS_PATTERN = Pattern.compile("\"([^\"]*)\"|([^\\s]+)");
	
	private final String _token;
	private final String _username;
	
	public DefaultTelegramBot(String token, String username)
	{
		_token = token;
		_username = username;
	}
	
	@Override
	public void onUpdateReceived(Update update)
	{
		try
		{
			if (update.hasChosenInlineQuery())
			{
				// Handle Chosen inline query
				handleUpdate(IChosenInlineQueryHandler.class, update, Update::getChosenInlineQuery, ChosenInlineQuery::getFrom, handler -> handler.onChosenInlineQuery(this, update, update.getChosenInlineQuery()));
			}
			else if (update.hasInlineQuery())
			{
				// Handle inline query
				handleUpdate(IInlineQueryHandler.class, update, Update::getInlineQuery, InlineQuery::getFrom, handler -> handler.onInlineQuery(this, update, update.getInlineQuery()));
			}
			else if (update.hasCallbackQuery())
			{
				// Handle callback query
				handleUpdate(ICallbackQueryHandler.class, update, Update::getCallbackQuery, CallbackQuery::getFrom, handler -> handler.onCallbackQuery(this, update, update.getCallbackQuery()));
			}
			else if (update.hasEditedMessage())
			{
				// Handle edited message
				handleUpdate(IEditedMessageHandler.class, update, Update::getEditedMessage, Message::getFrom, handler -> handler.onEditMessage(this, update, update.getEditedMessage()));
			}
			else if (update.hasMessage())
			{
				if (update.getMessage().hasDocument())
				{
					handleUpdate(IDocumentMessageHandler.class, update, Update::getMessage, Message::getFrom, handler -> handler.onDocumentSent(this, update, update.getMessage()));
				}
				else
				{
					// Handle message
					handleIncomingMessage(update);
				}
			}
			else
			{
				LOGGER.warn("Update doesn't contains neither ChosenInlineQuery/InlineQuery/CallbackQuery/EditedMessage/Message Update: {}", update);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to handle incomming update", e);
		}
	}
	
	/**
	 * @param <T>
	 * @param <R>
	 * @param clazz
	 * @param update
	 * @param dataMapper
	 * @param idMapper
	 * @param action
	 */
	private <T, R> void handleUpdate(Class<T> clazz, Update update, Function<Update, R> dataMapper, Function<R, User> idMapper, IThrowableFunction<T, Boolean> action)
	{
		final R query = dataMapper.apply(update);
		if (query == null)
		{
			return;
		}
		
		final User user = idMapper.apply(query);
		final List<T> handlers = CommandHandlers.getInstance().getHandlers(clazz, user);
		for (T handler : handlers)
		{
			try
			{
				if (action.apply(handler))
				{
					break;
				}
			}
			catch (TelegramApiRequestException e)
			{
				LOGGER.warn("Exception caught on handler: {} error: {}", handler.getClass().getSimpleName(), e.getApiResponse(), e);
			}
			catch (Exception e)
			{
				LOGGER.warn("Exception caught on handler: {}", handler.getClass().getSimpleName(), e);
			}
		}
	}
	
	/**
	 * @param update
	 */
	private void handleIncomingMessage(Update update)
	{
		final Message message = update.getMessage();
		if (message == null)
		{
			return;
		}
		
		String text = message.getText();
		if ((text == null) || text.isEmpty())
		{
			return;
		}
		
		// Parse commands that goes like: @BotNickname help to /help
		if (text.startsWith("@" + getBotUsername() + " "))
		{
			text = '/' + text.substring(("@" + getBotUsername() + " ").length());
		}
		// Parse commands that goes like: /help@BotNickname to /help
		else if (text.contains("@" + getBotUsername()))
		{
			text = text.replaceAll("@" + getBotUsername(), "");
			if (text.charAt(0) != '/')
			{
				text = '/' + text;
			}
		}
		
		// Parse arguments to a list
		final Matcher matcher = COMMAND_ARGS_PATTERN.matcher(text);
		if (matcher.find())
		{
			String command = matcher.group();
			final List<String> args = new ArrayList<>();
			String arg;
			while (matcher.find())
			{
				arg = matcher.group(1);
				if (arg == null)
				{
					arg = matcher.group(0);
				}
				
				args.add(arg);
			}
			
			final ICommandHandler handler = CommandHandlers.getInstance().getHandler(command);
			if (handler != null)
			{
				try
				{
					if (handler instanceof IAccessLevelHandler)
					{
						final IAccessLevelHandler accessHandler = (IAccessLevelHandler) handler;
						if (!accessHandler.validate(message.getFrom()))
						{
							BotUtil.sendMessage(this, message, message.getFrom().getUserName() + ": You are not authorized to use this function!", true, false, null);
							return;
						}
					}
					
					handler.onCommandMessage(this, update, message, args);
				}
				catch (TelegramApiRequestException e)
				{
					LOGGER.warn("API Exception caught on handler: {}, response: {} message: {}", handler.getClass().getSimpleName(), e.getApiResponse(), message, e);
				}
				catch (Exception e)
				{
					LOGGER.warn("Exception caught on handler: {}, message: {}", handler.getClass().getSimpleName(), message, e);
				}
			}
			else
			{
				for (IMessageHandler messageHandler : CommandHandlers.getInstance().getHandlers(IMessageHandler.class, message.getFrom()))
				{
					try
					{
						if (messageHandler.onMessage(this, update, message))
						{
							break;
						}
					}
					catch (TelegramApiRequestException e)
					{
						LOGGER.warn("API Exception caught on handler: {}, response: {} message: {}", messageHandler.getClass().getSimpleName(), e.getApiResponse(), message, e);
					}
					catch (Exception e)
					{
						LOGGER.warn("Exception caught on handler: {}, message: {}", messageHandler.getClass().getSimpleName(), message, e);
					}
				}
			}
		}
	}
	
	@Override
	public String getBotUsername()
	{
		return _username;
	}
	
	@Override
	public String getBotToken()
	{
		return _token;
	}
}
