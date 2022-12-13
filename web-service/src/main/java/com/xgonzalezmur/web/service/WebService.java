package com.xgonzalezmur.web.service;

import com.xgonzalezmur.web.dto.Language;
import com.xgonzalezmur.web.dto.LanguageVotes;
import com.xgonzalezmur.web.dto.VotingResults;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class WebService {
    private final String votingServiceUrl;

    protected RestTemplate restTemplate;

    public List<Language> getProgrammingLanguages() {
        return restTemplate.exchange(votingServiceUrl + "/getLanguages", HttpMethod.GET, null, new ParameterizedTypeReference<List<Language>>() {
        }).getBody();
    }

    public Integer sendVote(long idLanguage) {
        return restTemplate.postForObject(votingServiceUrl + "/vote", idLanguage, Integer.class);
    }

    public VotingResults getResults(final int percentageScale) {
        // Due to a rounding process below, it is not useful to have ordered results from the DB
        List<LanguageVotes> votesPerLanguage = restTemplate.exchange(votingServiceUrl + "/getVotes", HttpMethod.GET, null, new ParameterizedTypeReference<List<LanguageVotes>>() {
        }).getBody();

        if (votesPerLanguage == null || votesPerLanguage.isEmpty()) {
            return new VotingResults(votesPerLanguage, 0);
        }

        // Calculating the rest of data: total votes and percentages at full resolution
        long totalVotes = votesPerLanguage.stream().mapToLong(LanguageVotes::getVotes).sum();

        votesPerLanguage.forEach(language -> language.setPercentage(BigDecimal.valueOf(100.0 * language.getVotes() / totalVotes)));

        scaleAndRoundPercentages(votesPerLanguage, percentageScale);

        VotingResults votingResults = new VotingResults(votesPerLanguage, totalVotes);
        log.debug(votingResults.toString());

        return votingResults;
    }

    /**
     * Algorithm:
     * 1. Round everything down to the desired scale (Ex: scale=2 => 14.23419->14.23)
     * 2. Get the difference in sum and 100
     * 3. Distribute the difference by adding the minimum value to items, sorted in decreasing order of their decimal parts after the scale position (minimum value: scale=0 => 1, scale=1 => 0.1, scale=2 => 0.01, ...)
     */
    private void scaleAndRoundPercentages(List<LanguageVotes> languages, int scale) {
        // Will compare the decimal parts after the scale position, in descending order
        Comparator<LanguageVotes> comparatorOfPercentageRemainderAfterScale = Comparator.<LanguageVotes, BigDecimal>comparing(language -> language.getPercentage().movePointRight(scale).remainder(BigDecimal.ONE)).reversed();

        log.debug("Languages with initial percentages: {}", languages);
        languages.sort(comparatorOfPercentageRemainderAfterScale);
        log.debug("Languages in descending order by the percentage remainder, starting at decimal no.{}: {}", scale, languages);

        languages.forEach(language -> language.setPercentage(language.getPercentage().setScale(scale, RoundingMode.DOWN)));
        log.debug("Languages with percentages scaled to {} decimal/s: {}", scale, languages);

        BigDecimal sumScaledPercentages = languages.stream().map(LanguageVotes::getPercentage).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(scale, RoundingMode.DOWN);
        BigDecimal error = BigDecimal.valueOf(100).subtract(sumScaledPercentages);
        log.debug("Error: {}", error);

        // Correct value if needed
        if (BigDecimal.ZERO.compareTo(error) != 0) {
            // If the rounding method wasn't DOWN, 'steps' could be negative, so the absolute value should be used
            BigDecimal steps = error.scaleByPowerOfTen(scale);
            BigDecimal correctionValue = error.divide(steps, RoundingMode.DOWN);
            log.debug("The sum of percentages is {}%, so the error up to 100% is {}%", sumScaledPercentages, error);
            log.debug("A correction factor of {}% must be added to the first {} percentages", correctionValue, steps);

            // Applying correction value to the first 'steps' elements
            for (int i = 0; i < steps.intValue(); i++) {
                languages.get(i).setPercentage(languages.get(i).getPercentage().add(correctionValue));
            }
            log.debug("Languages with scaled percentages and corrected values: {}", languages);

            sumScaledPercentages = languages.stream().map(LanguageVotes::getPercentage).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(scale, RoundingMode.DOWN);
            log.debug("Now, the sum of the scaled percentages is {}%", sumScaledPercentages);
        }

        // Left in descending order
        languages.sort(Comparator.reverseOrder());
    }
}
