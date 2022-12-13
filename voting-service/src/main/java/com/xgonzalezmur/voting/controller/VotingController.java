package com.xgonzalezmur.voting.controller;

import com.xgonzalezmur.voting.model.Language;
import com.xgonzalezmur.voting.model.LanguageVotes;
import com.xgonzalezmur.voting.repository.LanguageRepository;
import com.xgonzalezmur.voting.repository.LanguageVotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class VotingController {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageVotesRepository languageVotesRepository;

    @GetMapping("/getLanguages")
    public List<Language> getLanguages() {
        log.debug("Voting Service - /getLanguages");

        return languageRepository.findAll();
    }

    @PostMapping("/vote")
    public Integer vote(@Validated @RequestBody long idLanguage) {
        log.debug("Voting Service - /vote/{}", idLanguage);

        Optional<LanguageVotes> optionalLanguageVotes = languageVotesRepository.findById(idLanguage);
        if (optionalLanguageVotes.isPresent()) {
            return languageVotesRepository.incrementVoteByLanguage(idLanguage);
        } else {
            Optional<Language> language = languageRepository.findById(idLanguage);

            if(language.isPresent()) {
                LanguageVotes newLanguageVotes = LanguageVotes.builder().language(language.get()).votes(1L).build();
                LanguageVotes persistedLanguageVotes = languageVotesRepository.saveAndFlush(newLanguageVotes);

                return persistedLanguageVotes.getVotes() == 1L ? 1 : 0;
            }
            else {
                return 0;
            }
        }
    }

    @GetMapping("/getVotes")
    public List<LanguageVotes> getVotes() {
        log.debug("Voting Service - /getVotes");

        return languageVotesRepository.findAll();
    }
}
