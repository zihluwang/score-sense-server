package com.ahgtgk.scoresense.controller;

import com.ahgtgk.scoresense.model.response.ProvinceResponse;
import com.ahgtgk.scoresense.service.DivisionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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
