package com.denny.brieflyai.ai;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiSummaryService {

    private final ChatClient openAiChatClient;
    private final ChatClient ollamaChatClient;
    private final CircuitBreaker openAiCircuitBreaker;

    public AiSummaryService(
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
            CircuitBreaker openAiCircuitBreaker
    ) {
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClient = ollamaChatClient;
        this.openAiCircuitBreaker = openAiCircuitBreaker;
    }

    public String summarize(String content) {

        String prompt =
                "Summarize this note in 1-2 concise sentences:\n\n" + content;

        try {

            return openAiCircuitBreaker.executeSupplier(
                    () -> callOpenAi(prompt)
            );

        } catch (CallNotPermittedException ex) {

            System.out.println(
                    "OpenAI circuit is OPEN. Using Ollama."
            );

            return callOllama(prompt);

        } catch (Exception ex) {

            System.out.println(
                    "OpenAI failed. Falling back to Ollama: "
                            + ex.getMessage()
            );

            return callOllama(prompt);
        }
    }

    private String callOpenAi(String prompt) {
        return openAiChatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }

    private String callOllama(String prompt) {
        return ollamaChatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}