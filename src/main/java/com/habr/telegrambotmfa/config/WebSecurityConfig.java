package com.habr.telegrambotmfa.config;

import com.habr.telegrambotmfa.botCommands.MfaCommand;
import com.habr.telegrambotmfa.login.CustomAuthenticationProvider;
import com.habr.telegrambotmfa.login.CustomFailureHandler;
import com.habr.telegrambotmfa.login.CustomSuccessHandler;
import com.habr.telegrambotmfa.login.CustomWebAuthenticationDetails;
import com.habr.telegrambotmfa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private MfaCommand mfaCommand;

    @Autowired
    public WebSecurityConfig(UserService userService, MfaCommand mfaCommand) {
        this.userService = userService;
        this.mfaCommand = mfaCommand;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/webjars/**", "/resource/**").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin()
                .authenticationDetailsSource(CustomWebAuthenticationDetails::new)
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler())
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout");

        RequestCache requestCache = http.getSharedObject(RequestCache.class);
        if (requestCache != null) {
            authenticationSuccessHandler().setRequestCache(requestCache);
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**", "/webjars/**", "/ajax/user/register");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        var provider = new CustomAuthenticationProvider(mfaCommand);
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CustomSuccessHandler authenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }

    @Bean
    public CustomFailureHandler authenticationFailureHandler() {
        return new CustomFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
