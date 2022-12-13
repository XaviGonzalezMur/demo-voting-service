package com.xgonzalezmur.web.dto;

import java.util.List;

public record VotingResults(List<LanguageVotes> languageVotes, long totalVotes) {
}
