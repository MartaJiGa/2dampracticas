package com.svalero.bookapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.bookapi.controller.BookController;
import com.svalero.bookapi.domain.Book;
import com.svalero.bookapi.domain.dto.AuthorOutDto;
import com.svalero.bookapi.domain.dto.BookInDto;
import com.svalero.bookapi.domain.dto.BookOutDto;
import com.svalero.bookapi.exception.BookNotFoundException;
import com.svalero.bookapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.ErrorResponse;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    //region GET

//    @Test
//    public void testGetBookOK() throws Exception {
//        int bookId = 1;
//        BookOutDto mockBook = new BookOutDto(2, "100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, new AuthorOutDto());
//
//        when(bookService.getBookDetails(bookId)).thenReturn(mockBook);
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", bookId)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        Book result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
//
//        assertNotNull(result);
//        assertEquals(200, response.getResponse().getStatus());
//        assertEquals("100 años de soledad", result.getTitle());
//        assertEquals("Realismo mágico", result.getGenre());
//        assertEquals(true, result.getAvailable());
//
//        verify(bookService, times(1)).getBookDetails(bookId);
//    }
//
//    @Test
//    public void testGetBookValidationError() throws Exception {
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", "libro")
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        if (!jsonResponse.isBlank()) {
//            ErrorResponse result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
//            assertNotNull(result);
//        }
//        assertEquals(400, response.getResponse().getStatus());
//    }
//
@Test
public void testGetBookNotFound() throws Exception {
    int bookId = 8;

    // Simula la excepción sin modificar el controlador
    when(bookService.getBookDetails(bookId))
            .thenThrow(new com.svalero.bookapi.exception.BookNotFoundException("Book not found with ID: " + bookId));

    mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", bookId))
            .andExpect(status().isNotFound())
            .andReturn();

    verify(bookService, times(1)).getBookDetails(bookId);
}


//
//    //endregion
//
//    //region POST
//
//    @Test
//    public void testAddBookOk() throws Exception {
//        int bookId = 1;
//        BookInDto bookInDto = new BookInDto("100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, 2);
//        BookOutDto mockBook = new BookOutDto(bookId, "100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, new AuthorOutDto());
//
//        when(bookService.createBook(bookInDto)).thenReturn(mockBook);
//
//        String requestBody = objectMapper.writeValueAsString(bookInDto);
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/books", bookInDto)
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationDate").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.available").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.author").exists())
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        Book result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
//
//        assertNotNull(result);
//        assertEquals(201, response.getResponse().getStatus());
//        assertEquals(new Date(1967, 5, 30), result.getPublicationDate());
//
//        verify(bookService, times(1)).createBook(bookInDto);
//    }
//
//    @Test
//    public void testAddBookValidationError() throws Exception {
//        BookInDto bookInDto = new BookInDto("100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), null, 2);
//
//        String requestBody = objectMapper.writeValueAsString(bookInDto);
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/books", bookInDto)
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        ErrorResponse result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
//
//        assertNotNull(result);
//        assertEquals(400, result.getStatusCode());
//        assertEquals("Errores de validación", result.getDetailMessageCode());
//    }
//
//    @Test
//    public void testAddBookNotFound() throws Exception {
//        long bookId = 3;
//        BookInDto bookInDto = new BookInDto("100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, 2);
//
//        when(bookService.createBook(bookInDto)).thenThrow(new BookNotFoundException("Book not found with ID: " + bookId));
//
//        String requestBody = objectMapper.writeValueAsString(bookInDto);
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/books", bookInDto)
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        ErrorResponse result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
//
//        assertNotNull(result);
//        assertEquals(404, result.getStatusCode());
//    }
//
//    //endregion
//
//    //region DELETE
//
//    @Test
//    public void testDeleteBookOk() throws Exception {
//        int bookId = 1;
//
//        doNothing().when(bookService).deleteBook(bookId);
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/books/{bookId}", bookId)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isNoContent())
//                .andReturn();
//
//        assertEquals(204, response.getResponse().getStatus());
//
//        verify(bookService, times(1)).deleteBook(bookId);
//    }
//
//    @Test
//    public void testDeleteBookValidationError() throws Exception {
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/books/$Fd"))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);
//
//        assertEquals(400, result.getStatusCode());
//    }
//
//    @Test
//    public void testDeleteBookNotFound() throws Exception {
//        int bookId = 8;
//
//        doThrow(new BookNotFoundException("Book not found with ID: " + bookId))
//                .when(bookService).deleteBook(bookId);
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/books/{bookId}", bookId)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);
//
//        assertEquals(404, result.getStatusCode());
//
//        verify(bookService, times(1)).deleteBook(bookId);
//    }

    //endregion

    //region PUT

//    @Test
//    public void testModifyBookOk() throws Exception {
//        int bookId = 1;
//        BookInDto bookInDto = new BookInDto("100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, 2);
//        BookOutDto mockBook = new BookOutDto(bookId, "100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, new AuthorOutDto());
//
//        when(bookService.updateBook(bookId, bookInDto)).thenReturn(mockBook);
//
//        String requestBody = objectMapper.writeValueAsString(bookInDto);
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/books/{bookId}", bookId)
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("100 años de soledad"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Realismo mágico"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationDate").value(new Date(1967, 5, 30)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(new AuthorOutDto()))
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        Book result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
//
//        assertNotNull(result);
//        assertEquals(200, response.getResponse().getStatus());
//
//        verify(bookService, times(1)).updateBook(bookId, bookInDto);
//    }

    //endregion
}
