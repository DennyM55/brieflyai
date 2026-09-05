package com.denny.brieflyai.ai;

import com.denny.brieflyai.note.Note;
import com.denny.brieflyai.note.NoteRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncAiSummaryService {

    private final AiSummaryService aiSummaryService;
    private final NoteRepository noteRepository;

    public AsyncAiSummaryService(
            AiSummaryService aiSummaryService,
            NoteRepository noteRepository
    ) {
        this.aiSummaryService = aiSummaryService;
        this.noteRepository = noteRepository;
    }

    @Async
    public void summarizeNote(Long noteId, String content) {

        try {

            String summary =
                    aiSummaryService.summarize(content);

            Note note = noteRepository.findById(noteId)
                    .orElseThrow();

            note.setSummary(summary);

            noteRepository.save(note);

        } catch (Exception ex) {

            System.err.println(
                    "Background summarization failed for note "
                            + noteId + ": " + ex.getMessage()
            );
        }
    }
}