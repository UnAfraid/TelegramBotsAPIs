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

import com.github.unafraid.telegrambot.handlers.inline.InlineButton;
import com.github.unafraid.telegrambot.handlers.inline.InlineContext;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author UnAfraid
 */
public class InlineCallbackEvent {
	private final InlineContext context;
	private final InlineButton button;
	private final AbsSender bot;
	private final Update update;
	private final CallbackQuery query;
	
	/**
	 * @param context the context
	 * @param button  the button
	 * @param bot     the bot
	 * @param update  the update received
	 * @param query   the query received
	 */
	public InlineCallbackEvent(InlineContext context, InlineButton button, AbsSender bot, Update update, CallbackQuery query) {
		this.context = context;
		this.button = button;
		this.bot = bot;
		this.update = update;
		this.query = query;
	}
	
	/**
	 * @return the context
	 */
	public InlineContext getContext() {
		return context;
	}
	
	/**
	 * @return the button
	 */
	public InlineButton getButton() {
		return button;
	}
	
	/**
	 * @return the bot
	 */
	public AbsSender getBot() {
		return bot;
	}
	
	/**
	 * @return the update
	 */
	public Update getUpdate() {
		return update;
	}
	
	/**
	 * @return the query
	 */
	public CallbackQuery getQuery() {
		return query;
	}
}