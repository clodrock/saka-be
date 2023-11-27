package com.clodrock.sakabe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailRequest {
    private String sendTo;
    private String content;
    private String subject;
}
