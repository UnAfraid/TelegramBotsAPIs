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

import java.util.List;

import com.github.unafraid.telegrambot.bots.AbstractTelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author UnAfraid
 */
public interface ICommandHandler extends ITelegramHandler {
	/**
	 * @return The command that will trigger @{link onCommandMessage} method
	 */
	String getCommand();
	
	/**
	 * @return The usage of the command whenever user types in /command without parameters some commands may return that if requires arguments to be supplied
	 */
	String getUsage();
	
	/**
	 * @return The description of the command shown in /help
	 */
	String getDescription();
	
	/**
	 * @return The category mapping whenever u type in /help that would group current command to the returned category
	 */
	default String getCategory() {
		return (getRequiredAccessLevel() > 0) ? "Protected [" + getRequiredAccessLevel() + " level] commands" : "Public commands";
	}
	
	/**
	 * Fired when user types in /command arg0 arg1 arg2..
	 *
	 * @param bot     the bot
	 * @param update  the update
	 * @param message the message
	 * @param args    the arguments after command separated by space or wrapped within "things here are considered one arg"
	 * @throws TelegramApiException the exception
	 */
	void onCommandMessage(AbstractTelegramBot bot, Update update, Message message, List<String> args) throws TelegramApiException;
}
