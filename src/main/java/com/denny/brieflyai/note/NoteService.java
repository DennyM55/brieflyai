package com.denny.brieflyai.note;

import com.denny.brieflyai.ai.AiSummaryService;
import com.denny.brieflyai.ai.AsyncAiSummaryService;
import com.denny.brieflyai.exception.NoteNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final AsyncAiSummaryService asyncAiSummaryService;

    public NoteService(NoteRepository noteRepository, AsyncAiSummaryService asyncAiSummaryService) {
        this.noteRepository = noteRepository;
        this.asyncAiSummaryService = asyncAiSummaryService;
    }

    public NoteResponse createNote(CreateNoteRequest request) {

        Note note = new Note();

        note.setTitle(request.title());
        note.setContent(request.content());

        Note savedNote = noteRepository.save(note);

        asyncAiSummaryService.summarizeNote(
                savedNote.getId(),
                savedNote.getContent()
        );

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