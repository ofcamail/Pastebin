package com.example.pastebin.dto;

import com.example.pastebin.exception.ForbiddenException;
import com.example.pastebin.model.Paste;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasteDTO {
    private String name;
    private String text;

    public static PasteDTO fromModel(Paste paste) {
        PasteDTO pasteDTO = new PasteDTO();
        pasteDTO.setName(paste.getName());
        pasteDTO.setText(paste.getText());
        return pasteDTO;
    }

    public Paste toModel() {
        Paste paste = new Paste();
        if (name == null || name.isBlank()) throw new ForbiddenException();
        else paste.setName(name);
        if (text == null || text.isBlank()) throw new ForbiddenException();
        else paste.setText(text);
        return paste;
    }
}

