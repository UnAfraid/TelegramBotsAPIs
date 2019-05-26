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
package com.github.unafraid.telegrambot.handlers.inline;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.github.unafraid.telegrambot.handlers.inline.layout.IInlineMenuLayout;
import com.github.unafraid.telegrambot.util.BotUtil;
import com.github.unafraid.telegrambot.util.MapUtil;

/**
 * @author UnAfraid
 */
public class InlineUserData
{
	private final int id;
	private final MapUtil params = new MapUtil(new ConcurrentHashMap<>());
	private InlineMenu activeMenu;
	private InlineButton activeButton;
	private int state;
	
	public InlineUserData(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * @return the state
	 */
	public int getState()
	{
		return state;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(int state)
	{
		this.state = state;
	}
	
	/**
	 * @return the activeMenu
	 */
	public InlineMenu getActiveMenu()
	{
		return activeMenu;
	}
	
	/**
	 * @param activeMenu the activeMenu to set
	 */
	public void setActiveMenu(InlineMenu activeMenu)
	{
		this.activeMenu = activeMenu;
	}
	
	/**
	 * @return the activeButton
	 */
	public InlineButton getActiveButton()
	{
		return activeButton;
	}
	
	/**
	 * @param activeButton the activeButton to set
	 */
	public void setActiveButton(InlineButton activeButton)
	{
		this.activeButton = activeButton;
	}
	
	/**
	 * @return the params
	 */
	public MapUtil getParams()
	{
		return params;
	}
	
	/**
	 * Sends the InlineMenu to message's chat
	 * @param bot
	 * @param message
	 * @param text
	 * @param layout
	 * @param menu
	 * @throws TelegramApiException
	 */
	public void sendMenu(AbsSender bot, Message message, String text, IInlineMenuLayout layout, InlineMenu menu) throws TelegramApiException
	{
		Objects.requireNonNull(menu);
		activeMenu = menu;
		final InlineKeyboardMarkup markup = layout.generateLayout(activeMenu.getButtons());
		BotUtil.sendMessage(bot, message, text, false, true, markup);
	}
	
	/**
	 * Edits current message with the new text and menu
	 * @param bot
	 * @param message
	 * @param text
	 * @param layout
	 * @param menu
	 * @throws TelegramApiException
	 */
	public void editCurrentMenu(AbsSender bot, Message message, String text, IInlineMenuLayout layout, InlineMenu menu) throws TelegramApiException
	{
		Objects.requireNonNull(menu);
		if ((text == null) || text.trim().isEmpty())
		{
			throw new IllegalStateException("Menu's name should be non empty!");
		}
		activeMenu = menu;
		final InlineKeyboardMarkup markup = layout.generateLayout(activeMenu.getButtons());
		BotUtil.editMessage(bot, message, text, true, markup);
	}
	
	/**
	 * Edits the message sets menu.getName as text of the message and renders the menu specified
	 * @param bot
	 * @param message
	 * @param layout
	 * @param menu
	 * @throws TelegramApiException
	 */
	public void editCurrentMenu(AbsSender bot, Message message, IInlineMenuLayout layout, InlineMenu menu) throws TelegramApiException
	{
		Objects.requireNonNull(menu);
		editCurrentMenu(bot, message, menu.getName(), layout, menu);
	}
}