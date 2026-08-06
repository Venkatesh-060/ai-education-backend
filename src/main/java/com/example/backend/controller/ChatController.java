package com.example.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.ChatRequest;
import com.example.backend.dto.ChatResponse;
import com.example.backend.model.Chat;
import com.example.backend.model.Session;
import com.example.backend.model.User;
import com.example.backend.repository.ChatRepo;
import com.example.backend.repository.SessionRepo;
import com.example.backend.repository.UserRepo;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final ChatRepo chatRepo;
    private final SessionRepo sessionRepo;
    private final UserRepo userRepo;

    public ChatController(
            ChatRepo chatRepo,
            SessionRepo sessionRepo,
            UserRepo userRepo) {
        this.chatRepo = chatRepo;
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/send")
    public ChatResponse sendMessage(@RequestBody ChatRequest request) {

        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return new ChatResponse("Message cannot be empty");
        }

        String sessionId = request.getSessionId();
if (sessionId == null || sessionId.isBlank()) {
    return new ChatResponse("Session Id Required");
}

Session session = sessionRepo.findById(sessionId)
        .orElse(null);

        String senderId = request.getSenderId();
if (senderId == null || senderId.isBlank()) {
    return new ChatResponse("Sender Id Required");
}

User user = userRepo.findById(senderId)
        .orElse(null);
        Chat chat = new Chat();
        chat.setSessionId(request.getSessionId());
        chat.setSenderId(user.getId());
        chat.setSenderName(user.getFirstName() + " " + user.getLastName());
        chat.setMessage(request.getMessage());
        chat.setMessageType(request.getMessageType());
        chat.setTimestamp(LocalDateTime.now());
        chat.setCreatedAt(LocalDateTime.now());
        chatRepo.save(chat);
        return new ChatResponse("Message Sent");
    }

    @GetMapping("/session/{sessionId}")
    public List<Chat> getSessionMessages(@PathVariable String sessionId) {

        return chatRepo.findBySessionIdOrderByTimestampAsc(sessionId);
    }

    @DeleteMapping("/{messageId}")
    public ChatResponse deleteMessage(
            @PathVariable String messageId,
            Authentication authentication) {

        if (authentication == null) {
            return new ChatResponse("Unauthorized");
        }

        boolean allowed = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_TRAINER"));

        if (!allowed) {
            return new ChatResponse("Only Trainer/Admin can delete");
        }

       if (messageId == null || messageId.isBlank()) {
    return new ChatResponse("Invalid Message Id");
}

Chat chat = chatRepo.findById(messageId)
        .orElse(null);

        if (chat == null) {
            return new ChatResponse("Message Not Found");
        }
        chatRepo.delete(chat);
        return new ChatResponse("Message Deleted");
    }
}