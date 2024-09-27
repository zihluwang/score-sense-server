package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.entity.Question;
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
