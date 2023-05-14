package com.example.pastebin.dto;

import com.example.pastebin.enums.Access;
import com.example.pastebin.enums.TimeRange;
import com.example.pastebin.exception.ForbiddenException;
import com.example.pastebin.model.Paste;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedPasteDTO {
    private String name;
    private String text;
    private TimeRange timeRange;
    private Access access;

    public Paste toModel() {
        Paste paste = new Paste();
        if (name == null || name.isBlank()) throw new ForbiddenException();
        else paste.setName(name);
        if (text == null || text.isBlank()) throw new ForbiddenException();
        else paste.setText(text);
        paste.setAccess(access);
        return paste;
    }
}
