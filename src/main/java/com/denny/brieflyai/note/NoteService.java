package com.denny.brieflyai.note;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteResponse createNote(CreateNoteRequest request) {
        Note note = new Note();
        note.setContent(request.content());
        note.setTitle(request.title());
        Note savedNote = noteRepository.save(note);
        return NoteMapper.toResponse(savedNote);
    }

    public List<NoteResponse> getAllNotes() {
        List<Note> noteList = noteRepository.findAll();
        return NoteMapper.toResponse(noteList);
    }

    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));

        return NoteMapper.toResponse(note);
    }
}