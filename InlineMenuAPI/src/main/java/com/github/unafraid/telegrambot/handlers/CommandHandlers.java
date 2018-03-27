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
package com.github.unafraid.telegrambot.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.User;

import com.github.lordrex34.reflection.util.ClassPathUtil;

import javassist.Modifier;

/**
 * @author UnAfraid
 */
public final class CommandHandlers
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandlers.class);
	private final Map<String, ICommandHandler> handlers = new ConcurrentHashMap<>();
	
	protected CommandHandlers()
	{
	}
	
	/**
	 * Scans packageName for classes that implements ICommandHandler and registers them into the handlers map
	 * @param packageName
	 */
	public void registerHandlers(String packageName)
	{
		try
		{
			//@formatter:off
			ClassPathUtil.getAllClassesExtending(packageName, ICommandHandler.class)
				.filter(clazz -> !clazz.isInterface())
				.filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
				.forEach(this::registerHandler);
			//@formatter:off
			LOGGER.info("Registered {} command handlers", handlers.size());
		}
		catch (IOException e)
		{
			LOGGER.warn("Failed to initialize handlers", e);
		}
	}
	
	public ICommandHandler registerHandler(Class<? extends ICommandHandler> clazz)
	{
		try
		{
			if (clazz.isInterface())
			{
				LOGGER.warn("Attempting to initialize interface class: {}", clazz.getName());
				return null;
			}
			
			final ICommandHandler handler = clazz.getConstructor().newInstance();
			addHandler(handler);
			return handler;
		}
		catch (Exception e)
		{
			LOGGER.warn("Failed to initialize {}", clazz.getName(), e);
		}
		return null;
	}
	
	public void addHandler(ICommandHandler handler)
	{
		handlers.put(handler.getCommand(), handler);
	}
	
	public void removeHandler(ICommandHandler handler)
	{
		handlers.remove(handler.getCommand());
	}
	
	public void removeHandler(String command)
	{
		handlers.remove(command);
	}
	
	public ICommandHandler getHandler(String command)
	{
		return handlers.get(command);
	}
	
	public Collection<ICommandHandler> getHandlers()
	{
		return handlers.values();
	}
	
	public <T> List<T> getHandlers(Class<T> clazz, User user)
	{
		//@formatter:off
		return handlers.values().stream()
			.filter(clazz::isInstance)
			.map(clazz::cast)
			.filter(messageHandler -> messageHandler instanceof IAccessLevelHandler ? ((IAccessLevelHandler) messageHandler).validate(user) : true)
			.collect(Collectors.toList());
		//@formatter:on
	}
	
	public static CommandHandlers getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CommandHandlers INSTANCE = new CommandHandlers();
	}
}
