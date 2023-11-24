package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.SakaProperty;
import com.clodrock.sakabe.enums.PropertyTypeEnum;
import com.clodrock.sakabe.model.PropertyRequest;
import com.clodrock.sakabe.repository.SakaPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final SakaPropertyRepository propertyRepository;

    public List<SakaProperty> getAllPropertyByPropertyType(PropertyTypeEnum propertyTypeEnum) {
        return propertyRepository.findAllByPropertyType(propertyTypeEnum);
    }

    public List<SakaProperty> getAllProperties() {
        return propertyRepository.findAll();
    }

    public void saveProperty(PropertyRequest propertyRequest) {
        propertyRepository.save(SakaProperty.builder()
                        .propertyType(PropertyTypeEnum.valueOf(propertyRequest.propertyType()))
                        .key(propertyRequest.key())
                        .value(propertyRequest.value())
                .build());
    }
}
