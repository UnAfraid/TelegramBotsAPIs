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

import com.github.unafraid.telegrambot.handlers.inline.InlineButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * @author UnAfraid
 */
public class InlineFixedButtonsPerRowLayout implements IInlineMenuLayout {
	private final int maxButtonsPerRow;
	
	/**
	 * Creates new Inline Fixed Buttons per row layout
	 *
	 * @param maxButtonsPerRow buttons per row
	 */
	public InlineFixedButtonsPerRowLayout(int maxButtonsPerRow) {
		this.maxButtonsPerRow = maxButtonsPerRow;
	}
	
	@Override
	public InlineKeyboardMarkup generateLayout(List<InlineButton> buttons) {
		final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
		markup.setKeyboard(new ArrayList<>());
		final List<List<InlineKeyboardButton>> keyboard = markup.getKeyboard();
		buttons.forEach(uiButton ->
		{
			final InlineKeyboardButton button = uiButton.createInlineKeyboardButton();
			if (keyboard.isEmpty() || uiButton.isForceNewRow() || (keyboard.get(markup.getKeyboard().size() - 1).size() >= maxButtonsPerRow)) {
				keyboard.add(new ArrayList<>());
			}
			keyboard.get(keyboard.size() - 1).add(button);
		});
		return markup;
	}
}