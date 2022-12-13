package com.xgonzalezmur.voting.repository;

import com.xgonzalezmur.voting.model.Language;
import com.xgonzalezmur.voting.model.LanguageVotes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class LanguageVotesRepositoryTest {

    @Autowired
    private LanguageRepository repositoryLanguages;
    @Autowired
    private LanguageVotesRepository repositoryVotes;

    @Test
    void contextLoads() {
        assertNotNull(repositoryLanguages);
        assertNotNull(repositoryVotes);
    }

    @Test
    void givenInitializedDb_whenStoringSomeVotes_thenVotesCanBeRead() {
        Optional<Language> anyLanguage = repositoryLanguages.findAll().stream().findAny();

        anyLanguage.ifPresent(language -> {
            LanguageVotes languageVotes = LanguageVotes.builder().language(language).votes(0L).build();
            repositoryVotes.saveAndFlush(languageVotes);

            int recordsModified = repositoryVotes.incrementVoteByLanguage(language.getId());

            assertEquals(1, recordsModified);
            assertEquals(1L, repositoryVotes.findById(language.getId()).orElseThrow(PersistenceException::new).getVotes());
        });
    }
}