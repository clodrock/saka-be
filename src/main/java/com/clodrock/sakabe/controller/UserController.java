package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.entity.SakaProperty;
import com.clodrock.sakabe.enums.PropertyTypeEnum;
import com.clodrock.sakabe.mapper.SecurityQuestionMapper;
import com.clodrock.sakabe.model.ChangePasswordRequest;
import com.clodrock.sakabe.model.SecurityQuestion;
import com.clodrock.sakabe.model.SuccessResponse;
import com.clodrock.sakabe.service.PropertyService;
import com.clodrock.sakabe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;
    private final PropertyService propertyService;
    private final SecurityQuestionMapper questionMapper;

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok()
                .body(SuccessResponse.builder().build());
    }

    @GetMapping("/security-questions")
    public ResponseEntity<List<SecurityQuestion>> getSecurityQuestions(){
        List<SakaProperty> properties = propertyService.getAllPropertyByPropertyType(PropertyTypeEnum.SECURITY_QUESTION);
        return ResponseEntity.ok().body(properties.stream().map(questionMapper::toSecurityQuestion).toList());
    }
}