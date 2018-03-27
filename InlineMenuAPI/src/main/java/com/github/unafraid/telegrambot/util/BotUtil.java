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
package com.github.unafraid.telegrambot.util;

import org.telegram.telegrambots.api.methods.ActionType;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.github.unafraid.telegrambot.handlers.ICommandHandler;

/**
 * @author UnAfraid
 */
public class BotUtil
{
	public static void sendAction(AbsSender bot, Message message, ActionType actionType) throws TelegramApiException
	{
		final SendChatAction sendAction = new SendChatAction();
		sendAction.setChatId(Long.toString(message.getChat().getId()));
		sendAction.setAction(actionType);
		bot.execute(sendAction);
	}
	
	public static void sendUsage(AbsSender bot, Message message, ICommandHandler handler) throws TelegramApiException
	{
		final SendMessage msg = new SendMessage();
		msg.setChatId(Long.toString(message.getChat().getId()));
		msg.setText(handler.getUsage());
		bot.execute(msg);
	}
	
	public static Message sendMessage(AbsSender bot, Message message, String text, boolean replyToMessage, boolean useMarkDown, ReplyKeyboard replayMarkup) throws TelegramApiException
	{
		final SendMessage msg = new SendMessage();
		msg.setChatId(Long.toString(message.getChat().getId()));
		msg.setText(text);
		msg.enableMarkdown(useMarkDown);
		if (replyToMessage)
		{
			msg.setReplyToMessageId(message.getMessageId());
		}
		if (replayMarkup != null)
		{
			msg.setReplyMarkup(replayMarkup);
		}
		return bot.execute(msg);
	}
	
	public static void sendHtmlMessage(AbsSender bot, Message message, String text, boolean replyToMessage, ReplyKeyboard replayMarkup) throws TelegramApiException
	{
		final SendMessage msg = new SendMessage();
		msg.setChatId(Long.toString(message.getChat().getId()));
		msg.setText(text);
		msg.enableHtml(true);
		if (replyToMessage)
		{
			msg.setReplyToMessageId(message.getMessageId());
		}
		if (replayMarkup != null)
		{
			msg.setReplyMarkup(replayMarkup);
		}
		bot.execute(msg);
	}
	
	public static void editMessage(AbsSender bot, Message message, String text, boolean useMarkDown, InlineKeyboardMarkup inlineMarkup) throws TelegramApiException
	{
		final EditMessageText msg = new EditMessageText();
		msg.setChatId(Long.toString(message.getChat().getId()));
		msg.setMessageId(message.getMessageId());
		msg.setText(text);
		msg.enableMarkdown(useMarkDown);
		msg.setReplyMarkup(inlineMarkup);
		bot.execute(msg);
	}
	
	public static void editMessage(AbsSender bot, CallbackQuery query, String text, boolean useMarkDown, InlineKeyboardMarkup inlineMarkup) throws TelegramApiException
	{
		final EditMessageText msg = new EditMessageText();
		msg.setChatId(Long.toString(query.getMessage().getChat().getId()));
		msg.setMessageId(query.getMessage().getMessageId());
		msg.setInlineMessageId(query.getInlineMessageId());
		msg.setText(text);
		msg.enableMarkdown(useMarkDown);
		msg.setReplyMarkup(inlineMarkup);
		bot.execute(msg);
	}
}
