package com.habr.telegrambotmfa.login;

import com.habr.telegrambotmfa.AuthorizedUser;
import com.habr.telegrambotmfa.botCommands.MfaCommand;
import com.habr.telegrambotmfa.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    private MfaCommand mfaCommand;

    public CustomAuthenticationProvider(MfaCommand mfaCommand) {
        this.mfaCommand = mfaCommand;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        HttpServletRequest request = ((CustomWebAuthenticationDetails) authentication.getDetails()).getRequest();
        AuthorizedUser authUser = (AuthorizedUser) getUserDetailsService().loadUserByUsername((String) authentication.getPrincipal());
        User user = authUser.getUser();

        if (user.getTelegramChatId() != null) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
            mfaCommand.requireMfa(authenticationToken, SecurityContextHolder.getContext(), request);
            throw new RequireTelegramMfaException("Пожалуйста, подтвердите вход в Telegram!");
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
