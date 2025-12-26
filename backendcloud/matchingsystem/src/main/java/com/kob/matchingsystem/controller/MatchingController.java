package com.kob.matchingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kob.matchingsystem.service.MatchingService;

import java.util.Objects;

@RestController
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @PostMapping("/api/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        System.out.println("MatchingSystemReceived");
        Integer userId = Integer.valueOf(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.valueOf(Objects.requireNonNull(data.getFirst("rating")));
        Integer botId = Integer.valueOf(Objects.requireNonNull(data.getFirst("bot_id")));
        return matchingService.addPlayer(userId, rating, botId);
    }

    @PostMapping("/api/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.valueOf(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(userId);
    }
}
