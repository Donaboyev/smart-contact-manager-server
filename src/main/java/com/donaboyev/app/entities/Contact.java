package com.donaboyev.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "CONTACTS")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long contactId;

    private String firstName;
    private String secondName;
    private String work;
    private String email;
    private String phone;

    @JsonIgnore
    @ManyToOne
    private User user;

    @Column(length = 100)
    private String description;

    private Boolean confirmed;

    @CreationTimestamp
    private LocalDateTime updatedAt;

}
