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

import com.github.unafraid.telegrambot.bots.AbstractTelegramBot;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author UnAfraid
 */
public interface IEditedMessageHandler extends ITelegramHandler {
	/**
	 * Fired whenever user types anything but a command
	 *
	 * @param bot     the bot
	 * @param update  the update
	 * @param message the message
	 * @return {@code true} if edit was successful, aborting notification to other handlers, {@code false} otherwise, continuing to look for handler that would return {@code true}
	 * @throws TelegramApiException the exception
	 */
	boolean onEditMessage(AbstractTelegramBot bot, Update update, Message message) throws TelegramApiException;
}
