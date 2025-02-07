package com.xgonzalezmur.web.controller;

import com.xgonzalezmur.web.dto.Language;
import com.xgonzalezmur.web.dto.VotingResults;
import com.xgonzalezmur.web.service.WebService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class WebController {

    private static final int PERCENTAGE_SCALE = 2;

    private final WebService webService;

    @GetMapping("/")
    public List<Language> getLanguages() {
        log.debug("Web Service - / (getLanguages)");

        return webService.getProgrammingLanguages();
    }

    @PostMapping("/vote")
    public Integer vote(@Validated @RequestBody int idLanguage) {
        log.debug("Web Service - /vote idLanguage={}", idLanguage);

        return webService.sendVote(idLanguage);
    }

    @GetMapping("/getVotes")
    public VotingResults getVotes() {
        log.debug("Web Service - /getVotes");

        return webService.getResults(PERCENTAGE_SCALE);
    }
}
