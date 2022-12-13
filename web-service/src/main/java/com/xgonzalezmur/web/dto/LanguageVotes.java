package com.xgonzalezmur.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@ToString
public class LanguageVotes implements Comparable<LanguageVotes> {
    long id;
    String name;
    long votes;
    @Setter
    BigDecimal percentage;

    public LanguageVotes(String name, long votes) {
        this.name = name;
        this.votes = votes;
        this.percentage = BigDecimal.ZERO;
    }

    @Override
    public int compareTo(LanguageVotes o) {
        return percentage.compareTo(o.percentage);
    }
}
