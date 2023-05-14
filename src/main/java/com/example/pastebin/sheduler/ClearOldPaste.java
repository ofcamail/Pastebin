package com.example.pastebin.sheduler;

import com.example.pastebin.repository.PasteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClearOldPaste {
    private final PasteRepository pasteRepository;

    public ClearOldPaste(PasteRepository pasteRepository) {
        this.pasteRepository = pasteRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void clearTokens() {
        pasteRepository.delete();
    }
}
