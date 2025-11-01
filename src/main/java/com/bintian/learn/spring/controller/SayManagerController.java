package com.bintian.learn.spring.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage")
public class SayManagerController {



    @GetMapping("/initData")
    public String initData() {
        return "ok";
    }
}
