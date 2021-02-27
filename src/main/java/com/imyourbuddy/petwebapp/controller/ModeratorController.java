package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.Moderator;
import com.imyourbuddy.petwebapp.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for {@link Moderator}
 */

@RestController
@RequestMapping("rest/moder")
public class ModeratorController {
    private final ModeratorService service;

    @Autowired
    public ModeratorController(ModeratorService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Moderator> getAll() {
        return service.getAll();
    }
}
