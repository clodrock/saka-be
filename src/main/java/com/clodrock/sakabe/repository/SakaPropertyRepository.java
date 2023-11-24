package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.SakaProperty;
import com.clodrock.sakabe.enums.PropertyTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SakaPropertyRepository extends JpaRepository<SakaProperty, Long> {
    List<SakaProperty> findAllByPropertyType(PropertyTypeEnum propertyTypeEnum);
}
