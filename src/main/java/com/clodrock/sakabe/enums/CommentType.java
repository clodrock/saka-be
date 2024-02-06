package com.clodrock.sakabe.enums;

public enum CommentType {
    POSITIVE(1),
    NEGATIVE(2),
    COMPLAINT(3),
    SUB_COMMENT(4);

    private final int value;

    CommentType(final int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
