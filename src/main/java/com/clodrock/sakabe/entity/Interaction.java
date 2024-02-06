package com.clodrock.sakabe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity
@Table
@SuperBuilder
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_comment_id", nullable = false)
    private UserComment userComment;
    private String emojiCode;
    private Integer count;
    private UUID userId;
    private UUID boardId;
}
