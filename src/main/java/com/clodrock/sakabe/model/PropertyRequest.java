package com.clodrock.sakabe.model;

import com.clodrock.sakabe.validator.ValidProperty;
import jakarta.validation.constraints.NotNull;

public record PropertyRequest(@NotNull(message = "Key cannot be null!") String key,
                              @NotNull(message = "Value cannot be null!") String value,
                              @ValidProperty String propertyType) {
}
