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

import com.github.unafraid.telegrambot.handlers.inline.events.IInlineCallbackEvent;
import com.github.unafraid.telegrambot.handlers.inline.events.IInlineMessageEvent;

/**
 * @author UnAfraid
 */
public class InlineButtonBuilder
{
	final InlineContext context;
	String name;
	int row;
	boolean forceNewRow;
	IInlineCallbackEvent onQueryCallback;
	IInlineMessageEvent onInputMessage;
	InlineMenu subMenu;
	
	public InlineButtonBuilder(InlineContext context)
	{
		this.context = context;
	}
	
	public InlineButtonBuilder name(String name)
	{
		this.name = name;
		return this;
	}
	
	public InlineButtonBuilder row(int row)
	{
		this.row = row;
		return this;
	}
	
	public InlineButtonBuilder forceOnNewRow()
	{
		this.forceNewRow = true;
		return this;
	}
	
	public InlineButtonBuilder onQueryCallback(IInlineCallbackEvent onQueryCallback)
	{
		Objects.requireNonNull(onQueryCallback);
		this.onQueryCallback = onQueryCallback;
		return this;
	}
	
	public InlineButtonBuilder onInputMessage(IInlineMessageEvent onInputMessage)
	{
		Objects.requireNonNull(onInputMessage);
		this.onInputMessage = onInputMessage;
		return this;
	}
	
	public InlineButtonBuilder menu(InlineMenu subMenu)
	{
		if (this.subMenu != null)
		{
			throw new IllegalStateException("Menu already set!");
		}
		Objects.requireNonNull(subMenu);
		this.subMenu = subMenu;
		return this;
	}
	
	public InlineButton build()
	{
		return new InlineButton(this);
	}
}