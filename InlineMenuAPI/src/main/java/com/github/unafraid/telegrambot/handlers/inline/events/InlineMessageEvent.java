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
package com.github.unafraid.telegrambot.handlers.inline.events;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;

import com.github.unafraid.telegrambot.handlers.inline.InlineButton;
import com.github.unafraid.telegrambot.handlers.inline.InlineContext;

/**
 * @author UnAfraid
 */
public class InlineMessageEvent
{
	private final InlineContext context;
	private final InlineButton button;
	private final AbsSender bot;
	private final Update update;
	private final Message message;
	
	/**
	 * @param context
	 * @param button
	 * @param bot
	 * @param update
	 * @param message
	 */
	public InlineMessageEvent(InlineContext context, InlineButton button, AbsSender bot, Update update, Message message)
	{
		this.context = context;
		this.button = button;
		this.bot = bot;
		this.update = update;
		this.message = message;
	}
	
	/**
	 * @return the context
	 */
	public InlineContext getContext()
	{
		return context;
	}
	
	/**
	 * @return the button
	 */
	public InlineButton getButton()
	{
		return button;
	}
	
	/**
	 * @return the bot
	 */
	public AbsSender getBot()
	{
		return bot;
	}
	
	/**
	 * @return the update
	 */
	public Update getUpdate()
	{
		return update;
	}
	
	/**
	 * @return the query
	 */
	public Message getMessage()
	{
		return message;
	}
}