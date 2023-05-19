package com.example.pastebin.service;

import com.example.pastebin.dto.CreatedPasteDTO;
import com.example.pastebin.dto.PasteDTO;
import com.example.pastebin.enums.Access;
import com.example.pastebin.enums.TimeRange;
import com.example.pastebin.exception.ForbiddenException;
import com.example.pastebin.exception.NotFoundException;
import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.PasteRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasteService {
    private final PasteRepository pasteRepository;

    @Autowired
    public PasteService(PasteRepository pasteRepository) {
        this.pasteRepository = pasteRepository;
    }

    public String addPaste(CreatedPasteDTO pasteDTO) {
        Paste paste = pasteDTO.toModel();
        paste.setHash(RandomStringUtils.randomAlphabetic(8));
        if (!pasteDTO.getTimeRange().equals(TimeRange.NO_TIME_LIMIT))
            paste.setExpiredDate(Instant.now().plus(pasteDTO.getTimeRange().getTime(), ChronoUnit.MINUTES));
        else paste.setExpiredDate(null);
        pasteRepository.save(paste);
        return paste.getHash();
    }

    public List<PasteDTO> getLast10Pastes() {
        return pasteRepository.findTop10ByAccessAndExpiredDateIsAfterOrderByCreatedDateDesc(Access.PUBLIC, Instant.now()).stream().map(PasteDTO::fromModel).collect(Collectors.toList());
    }

    public List<PasteDTO> getPasteByNameOrText(String name, String text) {
        if ((name == null || name.isBlank()) && (text == null || text.isBlank())) throw new ForbiddenException();
        return pasteRepository.findAllByNameOrText(name, text)
                .stream()
                .map(PasteDTO::fromModel)
                .collect(Collectors.toList());
    }

    public PasteDTO getPasteByLink(String hash) {
        return PasteDTO.fromModel(pasteRepository.findPasteByHashAndExpiredDateIsAfter(hash, Instant.now()).orElseThrow(NotFoundException::new));
    }
}
