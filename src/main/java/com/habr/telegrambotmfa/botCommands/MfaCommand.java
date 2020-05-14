package com.habr.telegrambotmfa.botCommands;

import com.habr.telegrambotmfa.AuthorizedUser;
import com.habr.telegrambotmfa.TelegramBot;
import com.habr.telegrambotmfa.login.AuthenticationInfo;
import com.habr.telegrambotmfa.login.CustomSuccessHandler;
import com.habr.telegrambotmfa.models.User;
import com.habr.telegrambotmfa.services.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Component
public class MfaCommand {
    private static final Logger log = LoggerFactory.getLogger(MfaCommand.class);
    private static final String CONFIRM_BUTTON = "confirm";
    private Map<Long, AuthInfo> connectingUser = new HashMap<>();
    private TelegramBot telegramBot;
    private WebSocketService webSocketService;
    private CustomSuccessHandler customSuccessHandler;

    @Autowired
    public MfaCommand(TelegramBot telegramBot, WebSocketService webSocketService, @Lazy CustomSuccessHandler customSuccessHandler) {
        this.telegramBot = telegramBot;
        this.webSocketService = webSocketService;
        this.customSuccessHandler = customSuccessHandler;
    }

    public void requireMfa(Authentication authentication, SecurityContext context, HttpServletRequest request) {
        User user = ((AuthorizedUser) authentication.getPrincipal()).getUser();

        String csrfToken = request.getParameter("_csrf");
        HttpSession session = request.getSession(true);
        String redirectUrl = customSuccessHandler.getRedirectUrl(request, null);
        AuthInfo authInfo = new AuthInfo(authentication, context, session, csrfToken, redirectUrl);
        connectingUser.put(user.getTelegramChatId(), authInfo);

        sendUserMessage(user);
    }

    public void onCallbackQuery(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        AuthInfo authInfo = connectingUser.remove(message.getChatId());

        EditMessageText editMessageText = new EditMessageText()
                .setChatId(message.getChatId())
                .setMessageId(message.getMessageId());

        AuthenticationInfo info = new AuthenticationInfo();

        if (callbackQuery.getData().equals(CONFIRM_BUTTON)) {
            Authentication authentication = authInfo.getAuthentication();
            authInfo.getSecurityContext().setAuthentication(authentication);
            authInfo.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, authInfo.getSecurityContext());

            editMessageText.setText("Вы успешно подтвердили вход!");
            info.setSuccess(true);
            info.setRedirectUrl(authInfo.getRedirectUrl());
        } else {
            editMessageText.setText("Вы успешно отклонили вход!");
            info.setSuccess(false);
            info.setErrorMessage("Вы отклонили вход в Telegram");
        }

        webSocketService.sendLoginStatus(info, authInfo.getCsrf());

        try {
            telegramBot.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Error updating telegram MFA message", e);
        }
    }

    private void sendUserMessage(User user) {
        InlineKeyboardButton confirmButton = new InlineKeyboardButton("Подтвердить");
        confirmButton.setCallbackData(CONFIRM_BUTTON);

        InlineKeyboardButton declineButton = new InlineKeyboardButton("Отклонить");
        declineButton.setCallbackData("decline");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                Collections.singletonList(
                        Arrays.asList(confirmButton, declineButton)
                )
        );

        SendMessage sendMessage = new SendMessage()
                .setChatId(user.getTelegramChatId())
                .setText("Подтвердите вход в аккаунт <b>" + user.getUsername() + "</b>")
                .setParseMode("HTML")
                .setReplyMarkup(markup);

        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending telegram MFA message", e);
        }
    }

    private static class AuthInfo {
        private final Authentication authentication;
        private final SecurityContext securityContext;
        private final HttpSession session;
        private final String csrf;
        private final String redirectUrl;

        public AuthInfo(Authentication authentication, SecurityContext securityContext, HttpSession session, String csrf, String redirectUrl) {
            this.authentication = authentication;
            this.securityContext = securityContext;
            this.session = session;
            this.csrf = csrf;
            this.redirectUrl = redirectUrl;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public SecurityContext getSecurityContext() {
            return securityContext;
        }

        public HttpSession getSession() {
            return session;
        }

        public String getCsrf() {
            return csrf;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }
    }
}
