package com.habr.telegrambotmfa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
