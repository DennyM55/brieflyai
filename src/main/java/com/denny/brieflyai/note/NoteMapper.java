package com.denny.brieflyai.note;

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
}