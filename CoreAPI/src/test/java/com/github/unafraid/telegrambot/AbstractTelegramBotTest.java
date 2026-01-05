package com.github.unafraid.telegrambot;

import com.github.unafraid.telegrambot.bots.AbstractTelegramBot;
import com.github.unafraid.telegrambot.bots.DefaultTelegramBot;
import com.github.unafraid.telegrambot.handlers.ICommandHandler;
import com.github.unafraid.telegrambot.handlers.IPollHandler;
import com.github.unafraid.telegrambot.handlers.IUpdateHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AbstractTelegramBotTest {
    @Test
    public void onUpdateReceivedMessage() throws TelegramApiException {
        TelegramClient telegramClient = Mockito.mock(TelegramClient.class);

        User me = new User(0L, "TestBot", true);
        Mockito.doReturn(me).when(telegramClient).execute(Mockito.any(GetMe.class));

        final DefaultTelegramBot bot = new DefaultTelegramBot(telegramClient);
        final AtomicBoolean didCall = new AtomicBoolean();
        {
            bot.addHandler(new ICommandHandler() {
                @Override
                public String getCommand() {
                    return "/command";
                }

                @Override
                public String getUsage() {
                    return "/command test 1 2 3";
                }

                @Override
                public String getDescription() {
                    return "";
                }

                @Override
                public void onCommandMessage(AbstractTelegramBot bot, Update update, Message message, List<String> args) {
                    Assertions.assertNotNull(message);
                    Assertions.assertEquals(12345L, (long) (message.getMessageId()));
                    Assertions.assertTrue(didCall.compareAndSet(false, true));
                    Assertions.assertEquals("/command test 1 2 3", message.getText());
                    Assertions.assertArrayEquals(new String[]{"test", "1", "2", "3"}, args.toArray());
                }
            });
        }
        {
            bot.addHandler((IUpdateHandler) (b, u) -> {
                Assertions.assertNotNull(u);
                Assertions.assertTrue(u.hasMessage());
                Assertions.assertNotNull(u.getMessage());
                Assertions.assertEquals(12345L, (long) (u.getMessage().getMessageId()));
                return false;
            });
        }

        final Message msg = new Message();
        msg.setFrom(new User(0L, "TestBot", true));
        msg.setMessageId(12345);
        msg.setText("/command test 1 2 3");

        final Update update = new Update();
        update.setUpdateId(1);
        update.setMessage(msg);

        bot.consume(List.of(update));

        Assertions.assertTrue(didCall.get());
    }

    @Test
    public void onUpdateReceivedPoll() {
        final DefaultTelegramBot bot = new DefaultTelegramBot(null);
        final AtomicBoolean didCall = new AtomicBoolean();
        {
            bot.addHandler((IPollHandler) (b, u, poll) -> {
                Assertions.assertNotNull(poll);
                Assertions.assertEquals("test", poll.getId());
                Assertions.assertTrue(didCall.compareAndSet(false, true));
                return true;
            });
        }
        {
            bot.addHandler((IUpdateHandler) (b, u) -> {
                Assertions.assertNotNull(u);
                Assertions.assertTrue(u.hasPoll());
                Assertions.assertNotNull(u.getPoll());
                Assertions.assertEquals("test", u.getPoll().getId());
                return false;
            });
        }

        final Message msg = new Message();
        msg.setFrom(new User(0L, "TestBot", true));

        final Poll poll = new Poll();
        poll.setId("test");

        final Update update = new Update();
        update.setUpdateId(1);
        update.setMessage(msg);
        update.setPoll(poll);

        bot.consume(List.of(update));

        Assertions.assertTrue(didCall.get());
    }
}
