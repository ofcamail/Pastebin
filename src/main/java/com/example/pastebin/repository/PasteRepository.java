package com.example.pastebin.repository;

import com.example.pastebin.enums.Access;
import com.example.pastebin.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {

    List<Paste> findTop10ByAccessAndExpiredDateIsAfterOrderByCreatedDateDesc(Access access, Instant instant);

    @Modifying
    @Query(value = "DELETE FROM paste\n" +
            "WHERE paste.expired_date<now()", nativeQuery = true)
    void delete();

    Optional<Paste> findPasteByHashAndExpiredDateIsAfter(String hash, Instant instant);

    @Query("SELECT p FROM Paste p\n" +
            "where p.access = 'PUBLIC' and p.expiredDate > now() \n " +
            "and (p.name = ?1 or p.text = ?2)")
    List<Paste> findAllByNameOrText(String name, String text);
}
