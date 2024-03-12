package com.bintian.learn.spring.controller;

import com.bintian.learn.spring.service.CityManagerService;
import com.bintian.learn.spring.vo.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage")
public class SayManagerController {

    @Autowired
    private CityManagerService managerService;

    @PostMapping("/insert")
    public String addCity(@RequestBody PersonVO person) {
        managerService.addCity(person);
        return "ok";
    }

    @GetMapping("/initData")
    public String initData() {
        return "ok";
    }
}
