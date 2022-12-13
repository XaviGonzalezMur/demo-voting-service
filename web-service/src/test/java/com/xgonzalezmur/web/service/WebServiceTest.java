package com.xgonzalezmur.web.service;

import com.xgonzalezmur.web.dto.LanguageVotes;
import com.xgonzalezmur.web.dto.VotingResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class WebServiceTest {
    public static final String VOTING_SERVICE_URL = "http://VOTING-SERVICE";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WebService webService = new WebService(VOTING_SERVICE_URL, restTemplate);

    @Test
    void givenVotes_whenMockingGetResults_thenPercentagesAreCalculatedAndSum100() {
        List<LanguageVotes> languageVotes = new ArrayList<>();
        languageVotes.add(new LanguageVotes("Java", 2127));
        languageVotes.add(new LanguageVotes("Python", 1879));
        languageVotes.add(new LanguageVotes("C/C++", 1523));
        languageVotes.add(new LanguageVotes("C#", 1236));
        languageVotes.add(new LanguageVotes("Javascript", 1012));
        languageVotes.add(new LanguageVotes("CSS", 588));
        final int PERCENTAGE_SCALE = 1;

        Mockito.when(restTemplate.exchange(VOTING_SERVICE_URL + "/getVotes", HttpMethod.GET, null, new ParameterizedTypeReference<List<LanguageVotes>>() {
        })).thenReturn(new ResponseEntity<>(languageVotes, HttpStatus.OK));
        VotingResults votingResults = webService.getResults(PERCENTAGE_SCALE);

        Assertions.assertEquals(languageVotes, votingResults.languageVotes());
        long expectedTotalVotes = languageVotes.stream().mapToLong(LanguageVotes::getVotes).sum();
        Assertions.assertEquals(expectedTotalVotes, votingResults.totalVotes());
        double sumPercentages = votingResults.languageVotes().stream().map(LanguageVotes::getPercentage).mapToDouble(BigDecimal::doubleValue).sum();
        Assertions.assertEquals(100, sumPercentages);
    }
}