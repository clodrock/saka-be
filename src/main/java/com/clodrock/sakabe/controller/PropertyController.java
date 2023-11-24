package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.entity.SakaProperty;
import com.clodrock.sakabe.model.ExceptionMessage;
import com.clodrock.sakabe.model.PropertyRequest;
import com.clodrock.sakabe.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @GetMapping("/get-all")
    public ResponseEntity<List<SakaProperty>> getAllProperty(){
        return ResponseEntity.ok().body(propertyService.getAllProperties());
    }

    @PostMapping("/save")
    public ResponseEntity<ExceptionMessage> save( @RequestBody @Valid PropertyRequest request){
        propertyService.saveProperty(request);
        return ResponseEntity.ok().body(ExceptionMessage.builder().message("İşlem başarılı").error(false).build());
    }
}
