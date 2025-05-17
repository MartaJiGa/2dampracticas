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
import org.junit.jupiter.api.Disabled;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.startsWith;

@WebMvcTest(BookController.class)
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    //region GET

    @Test
    public void testGetBookOK() throws Exception {
        int bookId = 1;
        BookOutDto mockBook = new BookOutDto(2, "100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, new AuthorOutDto());

        when(bookService.getBookDetails(bookId)).thenReturn(mockBook);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", bookId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Book result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(result);
        assertEquals(200, response.getResponse().getStatus());
        assertEquals("100 años de soledad", result.getTitle());
        assertEquals("Realismo mágico", result.getGenre());
        assertEquals(true, result.getAvailable());

        verify(bookService, times(1)).getBookDetails(bookId);
    }

    @Test
    public void testGetBookValidationError() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", "libro")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        if (!jsonResponse.isBlank()) {
            ErrorResponse result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
            assertNotNull(result);
        }
        assertEquals(400, response.getResponse().getStatus());
    }

    @Test
    public void testGetBookNotFound() throws Exception {
        int bookId = 8;

        when(bookService.getBookDetails(bookId))
                .thenThrow(new BookNotFoundException("Book not found with ID: " + bookId));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // Validación manual adicional del código de estado
        int statusCode = response.getResponse().getStatus();
        assertEquals(404, statusCode);

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals("Book not found with ID: " + bookId, jsonResponse);

        verify(bookService, times(1)).getBookDetails(bookId);
    }

    //endregion

    //region POST

    @Test
    public void testAddBookOk() throws Exception {
        int bookId = 1;
        BookInDto bookInDto = new BookInDto("100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, 2);
        BookOutDto mockBook = new BookOutDto(bookId, "100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, new AuthorOutDto());

        when(bookService.createBook(bookInDto)).thenReturn(mockBook);

        String requestBody = objectMapper.writeValueAsString(bookInDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/books", bookInDto)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.publicationDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").exists())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Book result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(result);
        assertEquals(201, response.getResponse().getStatus());
        assertEquals(new Date(1967, 5, 30), result.getPublicationDate());

        verify(bookService, times(1)).createBook(bookInDto);
    }

    @Test
    public void testAddBookValidationError_MissingFields() throws Exception {
        BookInDto bookInDto = new BookInDto();  // Todos los campos nulos o vacíos

        String requestBody = objectMapper.writeValueAsString(bookInDto);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);

        // Verificamos que la respuesta no esté vacía
        assertNotNull(jsonResponse, "El cuerpo de la respuesta no debería ser nulo");
        assertEquals(400, response.getResponse().getStatus());
    }

    @Test
    public void testAddBookNotFound() throws Exception {
        long bookId = 3;
        BookInDto bookInDto = new BookInDto("100 años de soledad", "Realismo mágico", new Date(1967, 5, 30), true, 2);

        when(bookService.createBook(bookInDto))
                .thenThrow(new BookNotFoundException("Book not found with ID: " + bookId));

        String requestBody = objectMapper.writeValueAsString(bookInDto);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals("Book not found with ID: " + bookId, jsonResponse);
        assertEquals(404, response.getResponse().getStatus());
    }

    //endregion

    //region DELETE

    @Test
    public void testDeleteBookOk() throws Exception {
        int bookId = 1;

        doNothing().when(bookService).deleteBook(bookId);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/books/{bookId}", bookId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(204, response.getResponse().getStatus());

        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    public void testDeleteBookValidationError() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/books/$Fd"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertNotNull(json);  // Confirmamos que algo se ha devuelto
        assertEquals(400, response.getResponse().getStatus());
    }

    //
    @Test
    public void testDeleteBookNotFound() throws Exception {
        int bookId = 8;

        doThrow(new BookNotFoundException("Book not found with ID: " + bookId))
                .when(bookService).deleteBook(bookId);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/books/{bookId}", bookId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals("Book not found with ID: " + bookId, json);
        assertEquals(404, response.getResponse().getStatus());

        verify(bookService, times(1)).deleteBook(bookId);
    }

    //endregion

    //region PUT

    @Test
    public void testModifyBookOk() throws Exception {
        int bookId = 1;

        LocalDate localDate = LocalDate.of(1967, 6, 30);
        Date publicationDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        String expectedDate = localDate.atStartOfDay(ZoneId.systemDefault()).format(formatter);

        BookInDto bookInDto = new BookInDto("100 años de soledad", "Realismo mágico", publicationDate, true, 2);
        BookOutDto mockBook = new BookOutDto(bookId, "100 años de soledad", "Realismo mágico", publicationDate, true, new AuthorOutDto());

        when(bookService.updateBook(bookId, bookInDto)).thenReturn(mockBook);

        String requestBody = objectMapper.writeValueAsString(bookInDto);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/books/{bookId}", bookId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("100 años de soledad"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Realismo mágico"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true))
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Book result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(result);
        assertEquals(200, response.getResponse().getStatus());

        verify(bookService, times(1)).updateBook(bookId, bookInDto);
    }

    @Test
    public void testModifyBookValidationError() throws Exception {
        int bookId = 1;

        // Cuerpo inválido: campos obligatorios sin valor
        BookInDto bookInDto = new BookInDto();
        String requestBody = objectMapper.writeValueAsString(bookInDto);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/books/{bookId}", bookId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertNotNull(json);  // El cuerpo debería contener mensajes de validación
        assertEquals(400, response.getResponse().getStatus());
    }

    @Test
    public void testModifyBookNotFound() throws Exception {
        int bookId = 999; // ID que simulamos como inexistente

        BookInDto bookInDto = new BookInDto(
                "Libro fantasma",
                "Terror",
                new Date(),
                true,
                2
        );

        when(bookService.updateBook(bookId, bookInDto))
                .thenThrow(new BookNotFoundException("Book not found with ID: " + bookId));

        String requestBody = objectMapper.writeValueAsString(bookInDto);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/books/{bookId}", bookId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals("Book not found with ID: " + bookId, jsonResponse);
        assertEquals(404, response.getResponse().getStatus());

        verify(bookService, times(1)).updateBook(bookId, bookInDto);
    }

    //endregion
}
