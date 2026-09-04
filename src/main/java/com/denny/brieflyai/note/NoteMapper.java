package com.denny.brieflyai.note;

import java.util.List;

public final class NoteMapper {

    private NoteMapper() {
    }

    public static NoteResponse toResponse(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getSummary(),
                note.getCreatedAt()
        );
    }

    public static List<NoteResponse> toResponse(List<Note> noteList) {
        return noteList.stream()
                .map(NoteMapper::toResponse)
                .toList();
    }
}