package com.ronglankj.scoresense.controller;

import com.ronglankj.scoresense.entity.Question;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @PostMapping("/submit")
    public Question submitQuestion() {
        return null;
    }

}
