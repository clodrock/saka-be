package com.clodrock.messagingstompwebsocket.enums;

public enum CommentType {
    POSITIVE(1),
    NEGATIVE(2),
    COMPLAINT(3);

    private final int value;

    CommentType(final int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
