package com.example.pastebin.service;

import com.example.pastebin.dto.PasteDTO;
import com.example.pastebin.enums.Access;
import com.example.pastebin.exception.ForbiddenException;
import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.PasteRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasteServiceTest {
    @Mock
    private PasteRepository pasteRepository;
    @InjectMocks
    private PasteService pasteService;
    private PasteDTO pasteDTO;
    private Paste paste;

    @BeforeEach
    void setUp() {
        Instant timeCreate = Instant.now();
        pasteDTO = new PasteDTO();
        pasteDTO.setName("name");
        pasteDTO.setText("text");
        paste = pasteDTO.toModel();
        paste.setAccess(Access.PUBLIC);
        paste.setCreatedDate(timeCreate);
        paste.setExpiredDate(timeCreate.plus(10, ChronoUnit.MINUTES));
        paste.setHash(RandomStringUtils.randomAlphabetic(8));
    }

    @Test
    void testAddPaste() {
        pasteRepository.save(paste);
        verify(pasteRepository, only()).save(paste);
    }

    @Test
    void testMappingDtoWhenNameNull() {
        pasteDTO.setName(null);
        assertThrows(ForbiddenException.class, () -> pasteDTO.toModel());
    }

    @Test
    void testMappingDtoWhenTextNull() {
        pasteDTO.setText(null);
        assertThrows(ForbiddenException.class, () -> pasteDTO.toModel());
    }

    @Test
    void testMappingDtoWhenNameIsBlank() {
        pasteDTO.setName(" ");
        assertThrows(ForbiddenException.class, () -> pasteDTO.toModel());
    }

    @Test
    void testMappingDtoWhenTextIsBlank() {
        pasteDTO.setText(" ");
        assertThrows(ForbiddenException.class, () -> pasteDTO.toModel());
    }

    @Test
    void getLast10Pastes() {
        List<Paste> list = new ArrayList<>();
        list.add(paste);
        when(pasteRepository.findTop10ByAccessAndExpiredDateIsAfterOrderByCreatedDateDesc(eq(Access.PUBLIC), any(Instant.class))).thenReturn(list);
        int size = pasteService.getLast10Pastes().size();
        assertEquals(list.size(), size);
        verify(pasteRepository, times(1)).findTop10ByAccessAndExpiredDateIsAfterOrderByCreatedDateDesc(eq(Access.PUBLIC), any(Instant.class));
    }

    @Test
    public void getPasteByNameOrText_throwsBadParamException_whenNameAndTextAreNull() {
        assertThrows(ForbiddenException.class, () -> pasteService.getPasteByNameOrText(null, null));
        assertThrows(ForbiddenException.class, () -> pasteService.getPasteByNameOrText("", ""));
    }


    @Test
    public void getPasteByNameOrText() {
        List<Paste> pasteList = new ArrayList<>();
        pasteList.add(paste);
        when(pasteRepository.findAllByNameOrText(paste.getName(), paste.getText())).thenReturn(pasteList);
        List<PasteDTO> pasteDTOList = pasteService.getPasteByNameOrText("name", "text");
        assertEquals(1, pasteDTOList.size());
        verify(pasteRepository).findAllByNameOrText(paste.getName(), paste.getText());
    }

    @Test
    void testGetPasteByLink() {
        when(pasteRepository.findPasteByHashAndExpiredDateIsAfter(eq(paste.getHash()),any(Instant.class))).thenReturn(Optional.of(paste));
        System.out.println();
        PasteDTO result = pasteService.getPasteByLink(paste.getHash());
        Assertions.assertNotNull(result);
        assertEquals(paste.getName(), result.getName());
    }
}