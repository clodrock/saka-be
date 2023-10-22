package com.clodrock.messagingstompwebsocket.entity;

import com.clodrock.messagingstompwebsocket.enums.CommentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode()
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    @Enumerated
    private CommentType commentType;
    private Long userId;
}
