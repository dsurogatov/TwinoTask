package org.dsu.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public final class AboutController {
    
    @RequestMapping("/about")
    public String index() {
        return "This is the test task for TWINO company!";
    }
    
}
