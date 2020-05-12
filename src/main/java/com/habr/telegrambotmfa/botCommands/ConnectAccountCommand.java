package com.habr.telegrambotmfa.botCommands;

import com.habr.telegrambotmfa.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ConnectAccountCommand extends BotCommand {
    private static final Logger log = LoggerFactory.getLogger(ConnectAccountCommand.class);
    private UserService userService;

    public ConnectAccountCommand(UserService userService) {
        super("connect", "Команда для подключения аккаунта");
        this.userService = userService;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        String username = strings[0];
        userService.connectBot(username, chat.getId());

        SendMessage message = new SendMessage()
                .setChatId(chat.getId())
                .setText("Вы успешно подключили бота!");
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending success telegram bot connect message", e);
        }
    }
}
