package com.habr.telegrambotmfa;

import com.habr.telegrambotmfa.botCommands.ConnectAccountCommand;
import com.habr.telegrambotmfa.botCommands.MfaCommand;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingCommandBot {
    private MfaCommand mfaCommand;
    private String botUsername;
    private String botToken;

    public TelegramBot(Environment env, ConnectAccountCommand connectAccountCommand, @Lazy MfaCommand mfaCommand) throws TelegramApiException {
        super(ApiContext.getInstance(DefaultBotOptions.class), false);
        this.botToken = env.getRequiredProperty("telegram.bot.token");
        this.mfaCommand = mfaCommand;
        this.botUsername = getMe().getUserName();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(this);

        register(connectAccountCommand);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            mfaCommand.onCallbackQuery(update);
        }
    }

    public String getUsernameByTelegramChatId(long telegramChatId) throws TelegramApiException {
        GetChat getChat = new GetChat(telegramChatId);
        return execute(getChat).getUserName();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
