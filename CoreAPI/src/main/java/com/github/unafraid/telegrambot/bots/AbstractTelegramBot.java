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
package com.github.unafraid.telegrambot.bots;

import com.github.unafraid.telegrambot.handlers.*;
import com.github.unafraid.telegrambot.util.BotUtil;
import com.github.unafraid.telegrambot.util.IThrowableFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.stickers.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author UnAfraid
 */
public class AbstractTelegramBot implements LongPollingUpdateConsumer, TelegramClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTelegramBot.class);
    private static final Pattern COMMAND_ARGS_PATTERN = Pattern.compile("\"([^\"]*)\"|([^\\s]+)");

    private final List<ITelegramHandler> handlers = new ArrayList<>();
    private volatile IAccessLevelValidator accessLevelValidator = null;
    private volatile String username;

    private final TelegramClient telegramClient;

    public AbstractTelegramBot(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            processUpdate(update);
        }
    }

    private void processUpdate(Update update) {
        try {
            final List<IUpdateHandler> updateHandlers = getAvailableHandlers(IUpdateHandler.class);
            for (IUpdateHandler updateHandler : updateHandlers) {
                try {
                    if (updateHandler.onUpdate(this, update)) {
                        return;
                    }
                } catch (Exception ex) {
                    LOGGER.error("Uncaught exception in onUpdate: {}", update, ex);
                }
            }

            if (update.hasChosenInlineQuery()) {
                handleUpdate(IChosenInlineQueryHandler.class, update, Update::getChosenInlineQuery, ChosenInlineQuery::getFrom, handler -> handler.onChosenInlineQuery(this, update, update.getChosenInlineQuery()));
                return;
            }

            if (update.hasInlineQuery()) {
                handleUpdate(IInlineQueryHandler.class, update, Update::getInlineQuery, InlineQuery::getFrom, handler -> handler.onInlineQuery(this, update, update.getInlineQuery()));
                return;
            }

            if (update.hasCallbackQuery()) {
                handleUpdate(ICallbackQueryHandler.class, update, Update::getCallbackQuery, CallbackQuery::getFrom, handler -> handler.onCallbackQuery(this, update, update.getCallbackQuery()));
                return;
            }

            if (update.hasEditedMessage()) {
                handleUpdate(IEditedMessageHandler.class, update, Update::getEditedMessage, Message::getFrom, handler -> handler.onEditMessage(this, update, update.getEditedMessage()));
                return;
            }

            if (update.hasChannelPost()) {
                handleUpdate(IChannelPostHandler.class, update, Update::getChannelPost, Message::getFrom, handler -> handler.onChannelPost(this, update, update.getChannelPost()));
                return;
            }

            if (update.hasEditedChannelPost()) {
                handleUpdate(IEditedChannelPostHandler.class, update, Update::getChannelPost, Message::getFrom, handler -> handler.onEditedChannelPost(this, update, update.getEditedChannelPost()));
                return;
            }

            if (update.hasShippingQuery()) {
                handleUpdate(IShippingQueryHandler.class, update, Update::getShippingQuery, ShippingQuery::getFrom, handler -> handler.onShippingQuery(this, update, update.getShippingQuery()));
                return;
            }

            if (update.hasPreCheckoutQuery()) {
                handleUpdate(IPreCheckoutQueryHandler.class, update, Update::getPreCheckoutQuery, PreCheckoutQuery::getFrom, handler -> handler.onPreCheckoutQuery(this, update, update.getPreCheckoutQuery()));
                return;
            }

            if (update.hasPoll()) {
                handleUpdate(IPollHandler.class, update, u -> u, u -> u.getMessage().getFrom(), handler -> handler.onPoll(this, update, update.getPoll()));
                return;
            }

            if (update.hasPollAnswer()) {
                handleUpdate(IPollAnswerHandler.class, update, Update::getPollAnswer, PollAnswer::getUser, handler -> handler.onPollAnswer(this, update, update.getPollAnswer()));
                return;
            }

            if (update.hasMyChatMember()) {
                handleUpdate(IHasMyChatMemberHandler.class, update, Update::getMyChatMember, ChatMemberUpdated::getFrom, handler -> handler.onHasMyChatMember(this, update, update.getMyChatMember()));
                return;
            }

            if (update.hasChatMember()) {
                handleUpdate(IChatMemberHandler.class, update, Update::getChatMember, ChatMemberUpdated::getFrom, handler -> handler.onChatMember(this, update, update.getChatMember()));
                return;
            }

            if (update.hasChatJoinRequest()) {
                handleUpdate(IChatJoinRequestHandler.class, update, Update::getChatJoinRequest, ChatJoinRequest::getUser, handler -> handler.onChatJoinRequest(this, update, update.getChatJoinRequest()));
                return;
            }

            if (update.hasMessage()) {
                if (update.getMessage().hasDocument()) {
                    handleUpdate(IDocumentMessageHandler.class, update, Update::getMessage, Message::getFrom, handler -> handler.onDocumentSent(this, update, update.getMessage()));
                    return;
                }

                handleIncomingMessage(update);
                return;
            }

            final List<IUnknownUpdateHandler> unknownHandlers = getAvailableHandlers(IUnknownUpdateHandler.class);
            if (unknownHandlers.isEmpty()) {
                LOGGER.warn("Update doesn't contains neither ChosenInlineQuery/InlineQuery/CallbackQuery/EditedMessage/ChannelPost/EditedChannelPost/Message Update: {}", update);
                return;
            }

            for (IUnknownUpdateHandler unknownHandler : unknownHandlers) {
                try {
                    if (unknownHandler.onUnhandledUpdate(this, update)) {
                        return;
                    }
                } catch (Exception ex) {
                    LOGGER.error("Uncaught exception in onUnhandledUpdate: {}", update, ex);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to handle incoming update", e);
        }
    }

    /**
     * @param <T>        the handler type
     * @param <R>        the return type
     * @param clazz      the handler class
     * @param update     the update
     * @param dataMapper the data mapper function
     * @param idMapper   the id mapper function
     * @param action     the action to execute
     */
    private <T extends ITelegramHandler, R> void handleUpdate(Class<T> clazz, Update update, Function<Update, R> dataMapper, Function<R, User> idMapper, IThrowableFunction<T, Boolean> action) {
        final R query = dataMapper.apply(update);
        if (query == null) {
            return;
        }

        final User user = idMapper.apply(query);
        final List<T> handlers = getAvailableHandlersForUser(clazz, user);
        for (T handler : handlers) {
            try {
                if (action.apply(handler)) {
                    break;
                }
            } catch (TelegramApiRequestException e) {
                LOGGER.warn("Exception caught on handler: {} error: {}", handler.getClass().getSimpleName(), e.getApiResponse(), e);
            } catch (Exception e) {
                LOGGER.warn("Exception caught on handler: {}", handler.getClass().getSimpleName(), e);
            }
        }
    }

    /**
     * @param text the message's text
     * @return Text without @BotNickname if specified
     */
    protected String processText(String text) {
        if ((text == null) || text.isEmpty()) {
            return null;
        }

        // Parse commands that goes like: @BotNickname help to /help
        if (text.startsWith("@" + getBotUsername() + " ")) {
            text = '/' + text.substring(("@" + getBotUsername() + " ").length());
        }
        // Parse commands that goes like: /help@BotNickname to /help
        else if (text.contains("@" + getBotUsername())) {
            text = text.replaceAll("@" + getBotUsername(), "");
            if (text.charAt(0) != '/') {
                text = '/' + text;
            }
        }
        return text;
    }

    private String getBotUsername() {
        if (username == null) {
            synchronized (this) {
                if (username == null) {
                    try {
                        final User user = telegramClient.execute(GetMe.builder().build());
                        username = user.getUserName();
                    } catch (TelegramApiException e) {
                        throw new IllegalStateException("failed to get bot username", e);
                    }
                }
            }
        }
        return username;
    }


    /**
     * @param update the update
     */
    private void handleIncomingMessage(Update update) {
        final Message message = update.getMessage();
        if (message == null) {
            return;
        }

        final String text = processText(message.getText());
        if ((text == null) || text.isEmpty()) {
            return;
        }

        // Parse arguments to a list
        final Matcher matcher = COMMAND_ARGS_PATTERN.matcher(text);
        if (matcher.find()) {
            String command = matcher.group();
            final List<String> args = new ArrayList<>();
            String arg;
            while (matcher.find()) {
                arg = matcher.group(1);
                if (arg == null) {
                    arg = matcher.group(0);
                }

                args.add(arg);
            }

            final ICommandHandler handler = getHandler(command);
            if (handler != null) {
                try {
                    if (!validateAccessLevel(handler, message.getFrom())) {
                        BotUtil.sendMessage(telegramClient, message, message.getFrom().getUserName() + ": You are not authorized to use this function!", true, false, null);
                        return;
                    }

                    handler.onCommandMessage(this, update, message, args);
                } catch (TelegramApiRequestException e) {
                    LOGGER.warn("API Exception caught on handler: {}, response: {} message: {}", handler.getClass().getSimpleName(), e.getApiResponse(), message, e);
                } catch (Exception e) {
                    LOGGER.warn("Exception caught on handler: {}, message: {}", handler.getClass().getSimpleName(), message, e);
                }
            } else {
                for (IMessageHandler messageHandler : getAvailableHandlersForUser(IMessageHandler.class, message.getFrom())) {
                    try {
                        if (messageHandler.onMessage(this, update, message)) {
                            break;
                        }
                    } catch (TelegramApiRequestException e) {
                        LOGGER.warn("API Exception caught on handler: {}, response: {} message: {}", messageHandler.getClass().getSimpleName(), e.getApiResponse(), message, e);
                    } catch (Exception e) {
                        LOGGER.warn("Exception caught on handler: {}, message: {}", messageHandler.getClass().getSimpleName(), message, e);
                    }
                }
            }
        }
    }

    /**
     * Sets the Access Level Validator instance that will be used for future access level validations
     *
     * @param accessLevelValidator the access level validator implementation
     */
    public void setAccessLevelValidator(IAccessLevelValidator accessLevelValidator) {
        this.accessLevelValidator = accessLevelValidator;
    }

    /**
     * @return the Access Level Validator instance that will be used for future access level validations
     */
    public IAccessLevelValidator getAccessLevelValidator() {
        return accessLevelValidator;
    }

    /**
     * Registers ICommandHandler instance into a collection of handlers
     *
     * @param handler the ICommandHandler instance
     */
    public void addHandler(ITelegramHandler handler) {
        handlers.add(handler);
    }

    /**
     * Removes ICommandHandler instance from the collection of handlers
     *
     * @param handler the ICommandHandler instance
     * @return {@code true} if handler with such command name was previously registered, {@code false} otherwise
     */
    public boolean removeHandler(ITelegramHandler handler) {
        return handlers.remove(handler);
    }

    /**
     * @param command the command name
     * @return {@link ICommandHandler} command handler from the collection of handlers, {@code null} if not registered
     */
    public ICommandHandler getHandler(String command) {
        //@formatter:off
		return handlers.stream()
				.filter(handler -> handler instanceof ICommandHandler)
				.map(handler -> (ICommandHandler) handler)
				.filter(handler -> handler.getCommand().equalsIgnoreCase(command))
				.findFirst().orElse(null);
		//@formatter:on
    }

    /**
     * @return {@code Collection<ICommandHandler>} the collection of ICommandHandler containing all currently registered handlers
     */
    public Collection<ITelegramHandler> getHandlers() {
        return Collections.unmodifiableCollection(handlers);
    }

    /**
     * Returns a {@code List<T>} and verifies for access level if any of the handlers implements {@link ITelegramHandler}
     *
     * @param clazz the class of the handler
     * @param <T>   the type of the handler
     * @return {@code List<T>} with all handlers implementing the generic type provided
     */
    public <T extends ITelegramHandler> List<T> getAvailableHandlers(Class<T> clazz) {
        //@formatter:off
		return handlers.stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.collect(Collectors.toList());
		//@formatter:on
    }

    /**
     * Returns a {@code List<T>} and verifies for access level if any of the handlers implements {@link ITelegramHandler}
     *
     * @param clazz the class of the handler
     * @param user  the user that requests this handler
     * @param <T>   the type of the handler
     * @return {@code List<T>} with all handlers implementing the generic type provided
     */
    public <T extends ITelegramHandler> List<T> getAvailableHandlersForUser(Class<T> clazz, User user) {
        //@formatter:off
		return handlers.stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.filter(messageHandler -> validateAccessLevel(messageHandler, user))
				.collect(Collectors.toList());
		//@formatter:on
    }

    /**
     * Validates access level
     *
     * @param <T>     the type
     * @param handler the handler
     * @param user    the user requesting the that handler
     * @return {@code true} if user is able to use that handler, {@code false} otherwise (Not registered, doesn't have access and so)
     */
    public <T extends ITelegramHandler> boolean validateAccessLevel(T handler, User user) {
        final IAccessLevelValidator accessLevelValidator = this.accessLevelValidator;
        if (accessLevelValidator == null) {
            if (handler.getRequiredAccessLevel() > 0) {
                throw new IllegalStateException("Discovered handler with required access level > 0 but there's no access level validator implemented, please use DefaultTelegramBot#setAccessLevelValidator");
            }
            return true;
        }
        return accessLevelValidator.validate(handler, user);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> executeAsync(Method method) throws TelegramApiException {
        return telegramClient.executeAsync(method);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        return telegramClient.execute(method);
    }

    @Override
    public Message execute(SendDocument sendDocument) throws TelegramApiException {
        return telegramClient.execute(sendDocument);
    }

    @Override
    public Message execute(SendPhoto sendPhoto) throws TelegramApiException {
        return telegramClient.execute(sendPhoto);
    }

    @Override
    public Message execute(SendVideo sendVideo) throws TelegramApiException {
        return telegramClient.execute(sendVideo);
    }

    @Override
    public Message execute(SendVideoNote sendVideoNote) throws TelegramApiException {
        return telegramClient.execute(sendVideoNote);
    }

    @Override
    public Message execute(SendSticker sendSticker) throws TelegramApiException {
        return telegramClient.execute(sendSticker);
    }

    @Override
    public Message execute(SendAudio sendAudio) throws TelegramApiException {
        return telegramClient.execute(sendAudio);
    }

    @Override
    public Message execute(SendVoice sendVoice) throws TelegramApiException {
        return telegramClient.execute(sendVoice);
    }

    @Override
    public List<Message> execute(SendMediaGroup sendMediaGroup) throws TelegramApiException {
        return telegramClient.execute(sendMediaGroup);
    }

    @Override
    public Boolean execute(SetChatPhoto setChatPhoto) throws TelegramApiException {
        return telegramClient.execute(setChatPhoto);
    }

    @Override
    public Boolean execute(AddStickerToSet addStickerToSet) throws TelegramApiException {
        return telegramClient.execute(addStickerToSet);
    }

    @Override
    public Boolean execute(ReplaceStickerInSet replaceStickerInSet) throws TelegramApiException {
        return telegramClient.execute(replaceStickerInSet);
    }

    @Override
    public Boolean execute(SetStickerSetThumbnail setStickerSetThumbnail) throws TelegramApiException {
        return telegramClient.execute(setStickerSetThumbnail);
    }

    @Override
    public Boolean execute(CreateNewStickerSet createNewStickerSet) throws TelegramApiException {
        return telegramClient.execute(createNewStickerSet);
    }

    @Override
    public File execute(UploadStickerFile uploadStickerFile) throws TelegramApiException {
        return telegramClient.execute(uploadStickerFile);
    }

    @Override
    public Serializable execute(EditMessageMedia editMessageMedia) throws TelegramApiException {
        return telegramClient.execute(editMessageMedia);
    }

    @Override
    public java.io.File downloadFile(File file) throws TelegramApiException {
        return telegramClient.downloadFile(file);
    }

    @Override
    public InputStream downloadFileAsStream(File file) throws TelegramApiException {
        return telegramClient.downloadFileAsStream(file);
    }

    @Override
    public Message execute(SendAnimation sendAnimation) throws TelegramApiException {
        return telegramClient.execute(sendAnimation);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendDocument sendDocument) {
        return telegramClient.executeAsync(sendDocument);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendPhoto sendPhoto) {
        return telegramClient.executeAsync(sendPhoto);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendVideo sendVideo) {
        return telegramClient.executeAsync(sendVideo);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendVideoNote sendVideoNote) {
        return telegramClient.executeAsync(sendVideoNote);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendSticker sendSticker) {
        return telegramClient.executeAsync(sendSticker);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendAudio sendAudio) {
        return telegramClient.executeAsync(sendAudio);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendVoice sendVoice) {
        return telegramClient.executeAsync(sendVoice);
    }

    @Override
    public CompletableFuture<List<Message>> executeAsync(SendMediaGroup sendMediaGroup) {
        return telegramClient.executeAsync(sendMediaGroup);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(SetChatPhoto setChatPhoto) {
        return telegramClient.executeAsync(setChatPhoto);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(AddStickerToSet addStickerToSet) {
        return telegramClient.executeAsync(addStickerToSet);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(ReplaceStickerInSet replaceStickerInSet) {
        return telegramClient.executeAsync(replaceStickerInSet);
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(SetStickerSetThumbnail setStickerSetThumbnail) {
        return telegramClient.executeAsync(setStickerSetThumbnail);

    }

    @Override
    public CompletableFuture<Boolean> executeAsync(CreateNewStickerSet createNewStickerSet) {
        return telegramClient.executeAsync(createNewStickerSet);
    }

    @Override
    public CompletableFuture<File> executeAsync(UploadStickerFile uploadStickerFile) {
        return telegramClient.executeAsync(uploadStickerFile);
    }

    @Override
    public CompletableFuture<Serializable> executeAsync(EditMessageMedia editMessageMedia) {
        return telegramClient.executeAsync(editMessageMedia);
    }

    @Override
    public CompletableFuture<Message> executeAsync(SendAnimation sendAnimation) {
        return telegramClient.executeAsync(sendAnimation);
    }

    @Override
    public CompletableFuture<java.io.File> downloadFileAsync(File file) {
        return telegramClient.downloadFileAsync(file);
    }

    @Override
    public CompletableFuture<InputStream> downloadFileAsStreamAsync(File file) {
        return telegramClient.downloadFileAsStreamAsync(file);
    }
}
