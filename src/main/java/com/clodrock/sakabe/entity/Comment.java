package com.clodrock.sakabe.entity;

import com.clodrock.sakabe.enums.CommentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@RequiredArgsConstructor
@Entity
@Table
@SuperBuilder
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    @Enumerated
    private CommentType commentType;
    private Long userId;
    private String boardId;
}
