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

import java.util.Objects;

/**
 * @param <T> the type
 * @param <R> the return type
 * @author UnAfraid
 */
@FunctionalInterface
public interface IThrowableFunction<T, R> {
	R apply(T var1) throws Exception;
	
	default <V> IThrowableFunction<V, R> compose(IThrowableFunction<? super V, ? extends T> var1) {
		Objects.requireNonNull(var1);
		return (var2) ->
		{
			return this.apply(var1.apply(var2));
		};
	}
	
	default <V> IThrowableFunction<T, V> andThen(IThrowableFunction<? super R, ? extends V> var1) {
		Objects.requireNonNull(var1);
		return (var2) ->
		{
			return var1.apply(this.apply(var2));
		};
	}
	
	static <T> IThrowableFunction<T, T> identity() {
		return (var0) ->
		{
			return var0;
		};
	}
}
