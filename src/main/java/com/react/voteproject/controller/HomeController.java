package com.react.voteproject.controller;


import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String hello1() {
        return "홈페이지입니다!";
    }

    @GetMapping("/api/test")
    public String hello() {
        return "테스트입니다.";
    }
}
