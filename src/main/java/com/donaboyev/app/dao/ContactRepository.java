package com.donaboyev.app.dao;

import com.donaboyev.app.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Transactional
    @Modifying
    @Query("update Contact c set " +
            "c.firstName = ?2, " +
            "c.secondName = ?3, " +
            "c.work = ?4, " +
            "c.email = ?5, " +
            "c.phone = ?6, " +
            "c.description = ?7, " +
            "c.updatedAt = ?8 where c.contactId = ?1")
    void updateContact(
            Long contactId,
            String firstName,
            String lastName,
            String work,
            String email,
            String phone,
            String description,
            LocalDateTime currentTime
    );

    @Query("from Contact as c where c.user.id = :userId order by c.updatedAt desc")
    Page<Contact> getContacts(@Param("userId") Long userId, Pageable pageable);
}
