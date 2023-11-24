package com.clodrock.sakabe.entity;

import com.clodrock.sakabe.enums.PropertyTypeEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@RequiredArgsConstructor
@Entity
@Table
@SuperBuilder
public class SakaProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String key;
    private String value;
    @Enumerated(EnumType.STRING)
    private PropertyTypeEnum propertyType;
}
