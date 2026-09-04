package com.denny.brieflyai.note;

import jakarta.validation.constraints.NotBlank;

public record CreateNoteRequest(

        @NotBlank
        String title,

        @NotBlank
        String content

) {
}