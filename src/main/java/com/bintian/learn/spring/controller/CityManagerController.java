package com.bintian.learn.spring.controller;

import com.bintian.learn.spring.service.CityManagerService;
import com.bintian.learn.spring.vo.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage")
public class CityManagerController {

    @Autowired
    private CityManagerService managerService;

    @PostMapping("/insert")
    public String addCity(@RequestBody PersonVO person) {
        managerService.addCity(person);
        return "ok";
    }
}
