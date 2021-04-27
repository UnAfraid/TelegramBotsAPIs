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
	
	/**
	 * Creates new Inline Button Builder instance
	 * @param context context
	 */
	public InlineButtonBuilder(InlineContext context)
	{
		this.context = context;
	}
	
	/**
	 * Sets name for the button
	 * @param name the name
	 * @return this builder
	 */
	public InlineButtonBuilder name(String name)
	{
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the button on particular row
	 * @param row the row
	 * @return this builder
	 */
	public InlineButtonBuilder row(int row)
	{
		this.row = row;
		return this;
	}
	
	/**
	 * Forces the button to be on new next row
	 * @return this builder
	 */
	public InlineButtonBuilder forceOnNewRow()
	{
		this.forceNewRow = true;
		return this;
	}
	
	/**
	 * Sets on query callback handler
	 * @param onQueryCallback the on query callback handler
	 * @return this builder
	 */
	public InlineButtonBuilder onQueryCallback(IInlineCallbackEvent onQueryCallback)
	{
		Objects.requireNonNull(onQueryCallback);
		this.onQueryCallback = onQueryCallback;
		return this;
	}
	
	/**
	 * Sets input message handler
	 * @param onInputMessage the input message handler
	 * @return this builder
	 */
	public InlineButtonBuilder onInputMessage(IInlineMessageEvent onInputMessage)
	{
		Objects.requireNonNull(onInputMessage);
		this.onInputMessage = onInputMessage;
		return this;
	}
	
	/**
	 * sets sub menu
	 * @param subMenu the sub menu
	 * @return this builder
	 */
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
	
	/**
	 * Builds inline button
	 * @return the inline button
	 */
	public InlineButton build()
	{
		return new InlineButton(this);
	}
}