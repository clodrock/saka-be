package com.clodrock.sakabe.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ExceptionMessage {
    private final String message;
    private final String trxId;

    @Builder.Default
    private final Boolean error = true;
}