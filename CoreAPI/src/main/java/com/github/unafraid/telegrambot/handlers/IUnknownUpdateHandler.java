package com.github.unafraid.telegrambot.handlers;

import com.github.unafraid.telegrambot.bots.AbstractTelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/***
 * @author UnAfraid
 */
public interface IUnknownUpdateHandler extends ITelegramHandler {
    /**
     * Fired unhandled update is received
     *
     * @param bot    the bot
     * @param update the update
     * @return {@code true} if handler 'consumed' that event, aborting notification to other handlers, {@code false} otherwise, continuing to look for handler that would return {@code true}
     * @throws TelegramApiException the exception
     */
    boolean onUnhandledUpdate(AbstractTelegramBot bot, Update update) throws TelegramApiException;
}