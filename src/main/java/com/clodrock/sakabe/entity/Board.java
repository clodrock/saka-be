package com.clodrock.sakabe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table
@SuperBuilder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String boardId;

    @ManyToMany
    @JoinTable(
            name = "board_ownership",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "email")
    )
    private List<SakaUser> ownerList;


    @ManyToMany
    @JoinTable(
            name = "board_sakauser",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "email")
    )
    private List<SakaUser> userList;
}
