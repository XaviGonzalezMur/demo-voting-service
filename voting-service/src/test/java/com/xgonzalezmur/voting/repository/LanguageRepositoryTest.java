package com.xgonzalezmur.voting.repository;

import com.xgonzalezmur.voting.model.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.util.Assert.notEmpty;

@DataJpaTest
class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository repository;

    @Test
    void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    void givenInitializedDb_whenContextLoads_thenThereAreLanguagesInDb() {
        List<Language> programmingLanguagesInDb = repository.findAll();

        notEmpty(programmingLanguagesInDb, "Database cannot be empty");
    }
}