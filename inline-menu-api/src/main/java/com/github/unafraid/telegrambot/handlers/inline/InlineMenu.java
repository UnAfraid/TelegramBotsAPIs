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

import java.util.List;

import com.github.unafraid.telegrambot.handlers.inline.layout.IInlineMenuLayout;

/**
 * @author UnAfraid
 */
public class InlineMenu {
	private final InlineContext context;
	private final String name;
	private final InlineMenu parentMenu;
	private final List<InlineButton> buttons;
	private final IInlineMenuLayout layout;
	
	/**
	 * Creates new inline menu
	 *
	 * @param builder the builder
	 */
	public InlineMenu(InlineMenuBuilder builder) {
		this.context = builder.context;
		this.name = builder.name;
		this.parentMenu = builder.parentMenu;
		this.buttons = builder.buttons;
		this.layout = builder.layout;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the context
	 */
	public InlineContext getContext() {
		return context;
	}
	
	/**
	 * @return the parentMenu
	 */
	public InlineMenu getParentMenu() {
		return parentMenu;
	}
	
	/**
	 * @return the buttons
	 */
	public List<InlineButton> getButtons() {
		return buttons;
	}
	
	/**
	 * @return the layout
	 */
	public IInlineMenuLayout getLayout() {
		return layout;
	}
}