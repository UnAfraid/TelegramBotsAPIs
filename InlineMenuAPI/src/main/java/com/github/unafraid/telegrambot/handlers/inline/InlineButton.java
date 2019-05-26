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

import java.util.UUID;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.github.unafraid.telegrambot.handlers.inline.events.IInlineCallbackEvent;
import com.github.unafraid.telegrambot.handlers.inline.events.IInlineMessageEvent;

/**
 * @author UnAfraid
 */
public class InlineButton
{
	private final InlineContext context;
	private final String name;
	private final int row;
	private final boolean forceNewRow;
	private final IInlineCallbackEvent onQueryCallback;
	private final IInlineMessageEvent onInputMessage;
	private final InlineMenu subMenu;
	private final String uuid = UUID.randomUUID().toString();
	
	public InlineButton(InlineButtonBuilder builder)
	{
		this.context = builder.context;
		this.name = builder.name;
		this.row = builder.row;
		this.forceNewRow = builder.forceNewRow;
		this.onQueryCallback = builder.onQueryCallback;
		this.onInputMessage = builder.onInputMessage;
		this.subMenu = builder.subMenu;
	}
	
	/**
	 * @return the context
	 */
	public InlineContext getContext()
	{
		return context;
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the row
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * @return the forceNewRow
	 */
	public boolean isForceNewRow()
	{
		return forceNewRow;
	}
	
	/**
	 * @return the onQueryCallback
	 */
	public IInlineCallbackEvent getOnQueryCallback()
	{
		return onQueryCallback;
	}
	
	/**
	 * @return the onInputMessage
	 */
	public IInlineMessageEvent getInputMessage()
	{
		return onInputMessage;
	}
	
	/**
	 * @return the subMenu
	 */
	public InlineMenu getSubMenu()
	{
		return subMenu;
	}
	
	/**
	 * @return the uuid
	 */
	public String getUUID()
	{
		return uuid;
	}
	
	/**
	 * @return the {@link InlineKeyboardButton}
	 */
	public InlineKeyboardButton createInlineKeyboardButton()
	{
		return new InlineKeyboardButton().setText(name).setCallbackData(uuid);
	}
}