package com.clodrock.sakabe.model;

import com.clodrock.sakabe.enums.CommentInteractionType;

import java.util.UUID;

public record EmojiRequest(Long commentId, String emojiCode, UUID userId, CommentInteractionType interactionType) {
}
