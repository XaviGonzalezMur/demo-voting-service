package com.xgonzalezmur.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_id_seq")
    @SequenceGenerator(name="language_id_seq", sequenceName = "language_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "language")
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private LanguageVotes votes;
}
