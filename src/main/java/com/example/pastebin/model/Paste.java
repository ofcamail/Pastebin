package com.example.pastebin.model;

import com.example.pastebin.enums.Access;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Paste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String hash;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String text;
    @Enumerated(EnumType.STRING)
    private Access access;
    private Instant expiredDate;
    private Instant createdDate = Instant.now();
}
