package com.denny.brieflyai.note;

import java.time.LocalDateTime;

public record NoteResponse(
        Long id,
        String title,
        String content,
        String summary,
        LocalDateTime createdAt
) {

}