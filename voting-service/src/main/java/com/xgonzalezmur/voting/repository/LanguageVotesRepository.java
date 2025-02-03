package com.xgonzalezmur.voting.repository;

import com.xgonzalezmur.voting.model.LanguageVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface LanguageVotesRepository extends JpaRepository<LanguageVotes, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE LanguageVotes lv SET lv.votes = lv.votes + 1 WHERE lv.language.id = ?1")
    int incrementVoteByLanguage(long idLanguage);
}
