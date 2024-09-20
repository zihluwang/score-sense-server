package com.ronglankj.scoresense.controller;

import com.ronglankj.scoresense.model.response.ProvinceResponse;
import com.ronglankj.scoresense.service.DivisionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/divisions")
public class DivisionController {

    private final DivisionService divisionService;

    public DivisionController(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    @GetMapping("/")
    public List<ProvinceResponse> getDivisions() {
        return divisionService.getAllDivisions();
    }

}
