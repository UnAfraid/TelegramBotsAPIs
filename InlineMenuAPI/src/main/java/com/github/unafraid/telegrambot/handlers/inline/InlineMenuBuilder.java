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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.github.unafraid.telegrambot.handlers.inline.layout.IInlineMenuLayout;
import com.github.unafraid.telegrambot.handlers.inline.layout.InlineRowDefinedLayout;
import org.jetbrains.annotations.NotNull;

/**
 * @author UnAfraid
 */
public class InlineMenuBuilder {
	final InlineContext context;
	String name;
	InlineMenu parentMenu;
	final List<InlineButton> buttons = new ArrayList<>();
	IInlineMenuLayout layout = InlineRowDefinedLayout.DEFAULT;
	
	/**
	 * Creates new inline menu builder
	 *
	 * @param context the context
	 */
	public InlineMenuBuilder(InlineContext context) {
		this(context, null);
	}
	
	/**
	 * Creates new inline menu builder
	 *
	 * @param context    the inline context
	 * @param parentMenu the parent menu
	 */
	public InlineMenuBuilder(@NotNull InlineContext context, InlineMenu parentMenu) {
		Objects.requireNonNull(context);
		
		this.context = context;
		this.parentMenu = parentMenu;
	}
	
	/**
	 * Sets parent menu
	 *
	 * @param parentMenu the parent menu to set
	 * @return this builder
	 */
	public InlineMenuBuilder parentMenu(@NotNull InlineMenu parentMenu) {
		Objects.requireNonNull(parentMenu);
		
		this.parentMenu = parentMenu;
		return this;
	}
	
	/**
	 * Sets name
	 *
	 * @param name the name to set
	 * @return this builder
	 */
	public InlineMenuBuilder name(@NotNull String name) {
		Objects.requireNonNull(name);
		
		this.name = name;
		return this;
	}
	
	/**
	 * Adds button
	 *
	 * @param button the button to add
	 * @return this builder
	 */
	public InlineMenuBuilder button(@NotNull InlineButton button) {
		Objects.requireNonNull(button);
		
		buttons.add(button);
		return this;
	}
	
	/**
	 * Adds collection of buttons
	 *
	 * @param buttons the buttons to add
	 * @return this builder
	 */
	public InlineMenuBuilder buttons(@NotNull Collection<InlineButton> buttons) {
		Objects.requireNonNull(buttons);
		
		this.buttons.addAll(buttons);
		return this;
	}
	
	/**
	 * Sets layout to generate the menu with
	 *
	 * @param layout the layout
	 * @return this builder
	 */
	public InlineMenuBuilder layout(@NotNull IInlineMenuLayout layout) {
		Objects.requireNonNull(layout);
		
		this.layout = layout;
		return this;
	}
	
	/**
	 * @return the built inline menu
	 */
	public InlineMenu build() {
		return new InlineMenu(this);
	}
}
