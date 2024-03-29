package com.clodrock.sakabe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity
@Table
@SuperBuilder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "board_ownership",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "email")
    )
    private List<SakaUser> ownerList;

    private String boardCreator;

    @ManyToMany
    @JoinTable(
            name = "board_sakauser",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "email")
    )
    private List<SakaUser> userList;
}
