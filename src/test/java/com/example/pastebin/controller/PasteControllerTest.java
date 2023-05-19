package com.example.pastebin.controller;

import com.example.pastebin.enums.Access;
import com.example.pastebin.enums.TimeRange;
import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.PasteRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class PasteControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasteRepository pasteRepository;
    private Paste paste ;

    @BeforeEach
    void setUp() {
        paste = new Paste();
        paste.setName("имя");
        paste.setText("текст");
        paste.setExpiredDate(Instant.now().plus(55, ChronoUnit.MINUTES));
        paste.setCreatedDate(Instant.now());
        paste.setAccess(Access.PUBLIC);
        pasteRepository.save(paste);
    }

        private JSONObject getJson() throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "имя");
            jsonObject.put("text", "текст");
            jsonObject.put("timeRange", TimeRange.ONE_DAY.toString());
            jsonObject.put("access", Access.PUBLIC.toString());
            return jsonObject;
        }


    @Test
    void addPaste() throws Exception {
        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson().toString())).andExpect(status().isOk())
                .andExpect(jsonPath("$").isString()).andReturn();


        mockMvc.perform(get("/get-last10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].name").value(getJson().get("name")))
                .andExpect(jsonPath("$[1].text").value(getJson().get("text")));
    }

    @Test
    void getLast10Pastes() throws Exception {
        mockMvc.perform(get("/get-last10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPasteByNameOrText() throws Exception {
        mockMvc.perform(get("/get-paste?name=" + paste.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
//                .andExpect(jsonPath("$[1].name").value(paste.getName()))
//                .andExpect(jsonPath("$[1].text").value(paste.getText()));
    }

    @Test
    void getPasteByLink() throws Exception{
        MvcResult mvcResult = mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("timeRange", TimeRange.ONE_DAY.toString())
                        .param("access", Access.PUBLIC.toString())
                        .content(getJson().toString())).andExpect(status().isOk())
                .andExpect(jsonPath("$").isString()).andReturn();

        String mvcres = mvcResult.getResponse().getContentAsString().replaceAll("http://localhost/","");

        mockMvc.perform(get("/" + mvcres))
                .andExpect(status().isOk());

    }
}