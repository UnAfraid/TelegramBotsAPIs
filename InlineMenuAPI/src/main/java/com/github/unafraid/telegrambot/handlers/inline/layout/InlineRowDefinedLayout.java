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
package com.github.unafraid.telegrambot.handlers.inline.layout;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.github.unafraid.telegrambot.handlers.inline.InlineButton;

/**
 * @author UnAfraid
 */
public class InlineRowDefinedLayout implements IInlineMenuLayout
{
	/**
	 * The default Inline Row Defined layout
	 */
	public static final InlineRowDefinedLayout DEFAULT = new InlineRowDefinedLayout();
	
	private InlineRowDefinedLayout()
	{
	}
	
	@Override
	public InlineKeyboardMarkup generateLayout(List<InlineButton> buttons)
	{
		final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
		markup.setKeyboard(new ArrayList<>());
		buttons.forEach(uiButton ->
		{
			final InlineKeyboardButton button = uiButton.createInlineKeyboardButton();
			if ((markup.getKeyboard().size() <= uiButton.getRow()) || uiButton.isForceNewRow())
			{
				markup.getKeyboard().add(new ArrayList<>());
			}
			markup.getKeyboard().get(uiButton.isForceNewRow() ? (markup.getKeyboard().size() - 1) : uiButton.getRow()).add(button);
		});
		return markup;
	}
}