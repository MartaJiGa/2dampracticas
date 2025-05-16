package com.svalero.bookapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.bookapi.controller.BookController;
import com.svalero.bookapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
public class AuthorControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService animalService;

    //region GET
    //endregion

    //region POST
    //endregion

    //region DELETE
    //endregion

    //region PUT
    //endregion
}
