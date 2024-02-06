package com.clodrock.sakabe.entity;

import com.clodrock.sakabe.enums.CommentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity
@Table
@SuperBuilder
public class UserComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    @Enumerated
    private CommentType commentType;
    private UUID userId;
    private UUID boardId;

    @OneToMany(mappedBy = "userComment", cascade = CascadeType.ALL)
    private List<Interaction> interactionList;
    private Long parentId;
}
