package com0.G22Trello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // Used for building API, serving static HTML Pages
public class HomeController {

    @GetMapping("/**")
    public String index() {
        return "index.html"; // name of main HTML file
    }
}
