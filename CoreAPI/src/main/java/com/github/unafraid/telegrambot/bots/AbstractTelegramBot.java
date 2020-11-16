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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import com.github.unafraid.telegrambot.handlers.IAccessLevelValidator;
import com.github.unafraid.telegrambot.handlers.ICallbackQueryHandler;
import com.github.unafraid.telegrambot.handlers.IChannelPostHandler;
import com.github.unafraid.telegrambot.handlers.IChosenInlineQueryHandler;
import com.github.unafraid.telegrambot.handlers.ICommandHandler;
import com.github.unafraid.telegrambot.handlers.IDocumentMessageHandler;
import com.github.unafraid.telegrambot.handlers.IEditedChannelPostHandler;
import com.github.unafraid.telegrambot.handlers.IEditedMessageHandler;
import com.github.unafraid.telegrambot.handlers.IInlineQueryHandler;
import com.github.unafraid.telegrambot.handlers.IMessageHandler;
import com.github.unafraid.telegrambot.handlers.ITelegramHandler;
import com.github.unafraid.telegrambot.handlers.IUnknownUpdateHandler;
import com.github.unafraid.telegrambot.handlers.IUpdateHandler;
import com.github.unafraid.telegrambot.util.BotUtil;
import com.github.unafraid.telegrambot.util.IThrowableFunction;

/**
 * @author UnAfraid
 */
public class AbstractTelegramBot extends TelegramLongPollingBot
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTelegramBot.class);
	private static final Pattern COMMAND_ARGS_PATTERN = Pattern.compile("\"([^\"]*)\"|([^\\s]+)");
	
	private final List<ITelegramHandler> handlers = new ArrayList<>();
	private volatile IAccessLevelValidator accessLevelValidator = null;
	
	private final String _token;
	private final String _username;
	
	public AbstractTelegramBot(String token, String username)
	{
		_token = token;
		_username = username;
	}
	
	@Override
	public void onUpdateReceived(Update update)
	{
		try
		{
			final List<IUpdateHandler> updateHandlers = getAvailableHandlers(IUpdateHandler.class);
			for (IUpdateHandler updateHandler : updateHandlers)
			{
				try
				{
					if (updateHandler.onUpdate(this, update))
					{
						return;
					}
				}
				catch (Exception ex)
				{
					LOGGER.error("Uncaught exception in onUpdate: {}", update, ex);
				}
			}
			
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
			else if (update.hasChannelPost())
			{
				// Handle channel post
				handleUpdate(IChannelPostHandler.class, update, Update::getChannelPost, Message::getFrom, handler -> handler.onChannelPost(this, update, update.getChannelPost()));
			}
			else if (update.hasEditedChannelPost())
			{
				// Handle edited channel post
				handleUpdate(IEditedChannelPostHandler.class, update, Update::getChannelPost, Message::getFrom, handler -> handler.onEditedChannelPost(this, update, update.getEditedChannelPost()));
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
				final List<IUnknownUpdateHandler> unknownHandlers = getAvailableHandlers(IUnknownUpdateHandler.class);
				if (unknownHandlers.isEmpty())
				{
					LOGGER.warn("Update doesn't contains neither ChosenInlineQuery/InlineQuery/CallbackQuery/EditedMessage/ChannelPost/EditedChannelPost/Message Update: {}", update);
					return;
				}
				
				for (IUnknownUpdateHandler unknownHandler : unknownHandlers)
				{
					try
					{
						if (unknownHandler.onUnhandledUpdate(this, update))
						{
							return;
						}
					}
					catch (Exception ex)
					{
						LOGGER.error("Uncaught exception in onUnhandledUpdate: {}", update, ex);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to handle incoming update", e);
		}
	}
	
	/**
	 * @param <T> the handler type
	 * @param <R> the return type
	 * @param clazz the handler class
	 * @param update the update
	 * @param dataMapper the data mapper function
	 * @param idMapper the id mapper function
	 * @param action the action to execute
	 */
	private <T extends ITelegramHandler, R> void handleUpdate(Class<T> clazz, Update update, Function<Update, R> dataMapper, Function<R, User> idMapper, IThrowableFunction<T, Boolean> action)
	{
		final R query = dataMapper.apply(update);
		if (query == null)
		{
			return;
		}
		
		final User user = idMapper.apply(query);
		final List<T> handlers = getAvailableHandlersForUser(clazz, user);
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
	 * @param text the message's text
	 * @return Text without @BotNickname if specified
	 */
	protected String processText(String text)
	{
		if ((text == null) || text.isEmpty())
		{
			return null;
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
		return text;
	}
	
	/**
	 * @param update the update
	 */
	private void handleIncomingMessage(Update update)
	{
		final Message message = update.getMessage();
		if (message == null)
		{
			return;
		}
		
		final String text = processText(message.getText());
		if ((text == null) || text.isEmpty())
		{
			return;
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
			
			final ICommandHandler handler = getHandler(command);
			if (handler != null)
			{
				try
				{
					if (!validateAccessLevel(handler, message.getFrom()))
					{
						BotUtil.sendMessage(this, message, message.getFrom().getUserName() + ": You are not authorized to use this function!", true, false, null);
						return;
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
				for (IMessageHandler messageHandler : getAvailableHandlersForUser(IMessageHandler.class, message.getFrom()))
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
	
	/**
	 * Sets the Access Level Validator instance that will be used for future access level validations
	 * @param accessLevelValidator the access level validator implementation
	 */
	public void setAccessLevelValidator(IAccessLevelValidator accessLevelValidator)
	{
		this.accessLevelValidator = accessLevelValidator;
	}
	
	/**
	 * @return the Access Level Validator instance that will be used for future access level validations
	 */
	public IAccessLevelValidator getAccessLevelValidator()
	{
		return accessLevelValidator;
	}
	
	/**
	 * Registers ICommandHandler instance into a collection of handlers
	 * @param handler the ICommandHandler instance
	 */
	public void addHandler(ITelegramHandler handler)
	{
		handlers.add(handler);
	}
	
	/**
	 * Removes ICommandHandler instance from the collection of handlers
	 * @return {@code true} if handler with such command name was previously registered, {@code false} otherwise
	 * @param handler the ICommandHandler instance
	 */
	public boolean removeHandler(ITelegramHandler handler)
	{
		return handlers.remove(handler);
	}
	
	/**
	 * @param command the command name
	 * @return {@link ICommandHandler} command handler from the collection of handlers, {@code null} if not registered
	 */
	public ICommandHandler getHandler(String command)
	{
		//@formatter:off
		return handlers.stream()
				.filter(handler -> handler instanceof ICommandHandler)
				.map(handler -> (ICommandHandler) handler)
				.filter(handler -> handler.getCommand().equalsIgnoreCase(command))
				.findFirst().orElse(null);
		//@formatter:on
	}
	
	/**
	 * @return {@link Collection <ICommandHandler>} the collection of ICommandHandler containing all currently registered handlers
	 */
	public Collection<ITelegramHandler> getHandlers()
	{
		return Collections.unmodifiableCollection(handlers);
	}
	
	/**
	 * Returns a {@code List<T>} and verifies for access level if any of the handlers implements {@link ITelegramHandler}
	 * @param clazz the class of the handler
	 * @param <T> the type of the handler
	 * @return {@code List<T>} with all handlers implementing the generic type provided
	 */
	public <T extends ITelegramHandler> List<T> getAvailableHandlers(Class<T> clazz)
	{
		//@formatter:off
		return handlers.stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.collect(Collectors.toList());
		//@formatter:on
	}
	
	/**
	 * Returns a {@code List<T>} and verifies for access level if any of the handlers implements {@link ITelegramHandler}
	 * @param clazz the class of the handler
	 * @param user the user that requests this handler
	 * @param <T> the type of the handler
	 * @return {@code List<T>} with all handlers implementing the generic type provided
	 */
	public <T extends ITelegramHandler> List<T> getAvailableHandlersForUser(Class<T> clazz, User user)
	{
		//@formatter:off
		return handlers.stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.filter(messageHandler -> validateAccessLevel(messageHandler, user))
				.collect(Collectors.toList());
		//@formatter:on
	}
	
	/**
	 * Validates access level
	 * @param handler the handler
	 * @param user the user requesting the that handler
	 * @return {@code true} if user is able to use that handler, {@code false} otherwise (Not registered, doesn't have access and so)
	 */
	public <T extends ITelegramHandler> boolean validateAccessLevel(T handler, User user)
	{
		final IAccessLevelValidator accessLevelValidator = this.accessLevelValidator;
		if (accessLevelValidator == null)
		{
			if (handler.getRequiredAccessLevel() > 0)
			{
				throw new IllegalStateException("Discovered handler with required access level > 0 but there's no access level validator implemented, please use DefaultTelegramBot#setAccessLevelValidator");
			}
			return true;
		}
		return accessLevelValidator.validate(handler, user);
	}
}
