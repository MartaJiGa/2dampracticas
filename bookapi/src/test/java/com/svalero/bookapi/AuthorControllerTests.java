package com.svalero.bookapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.bookapi.controller.AuthorController;
import com.svalero.bookapi.domain.dto.AuthorInDto;
import com.svalero.bookapi.domain.dto.AuthorOutDto;
import com.svalero.bookapi.exception.AuthorNotFoundException;
import com.svalero.bookapi.service.AuthorService;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;

    //region GET

    @Test
    public void testGetAuthorsOk() throws Exception {
        List<AuthorOutDto> authorList = getAuthors();

        when(authorService.getAllAuthors()).thenReturn(authorList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/authors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<AuthorOutDto> result = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(result);
        assertEquals(200, response.getResponse().getStatus());
        assertEquals("Terry Pratchett", result.get(0).getAuthorName());
        assertEquals("Estados Unidos", result.get(2).getNationality());

        verify(authorService, times(1)).getAllAuthors();
    }

//    @Test
//    public void testGetAuthorValidationError() throws Exception {
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/authors/abc"))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);
//
//        assertEquals(400, result.getStatusCode());
//    }

//    @Test
//    public void testGetAuthorNotFound() throws Exception {
//        int authorId = 1;
//
//        when(authorService.getAuthorDetails(authorId)).thenThrow(new AuthorNotFoundException("Author not found with ID: " + authorId));
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/authors/{authorId}", authorId))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString();
//        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
//
//        assertNotNull(errorResponse);
//        assertEquals(404, errorResponse.getStatusCode());
//
//        verify(authorService, times(1)).getAuthorDetails(authorId);
//    }

    //endregion

    //region POST

    @Test
    public void testAddAuthorOk() throws Exception {
        LocalDate date = LocalDate.of(1948, 4, 28);
        Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        AuthorInDto authorInDTO = new AuthorInDto("Terry Pratchett", true, "Reino Unido", formattedDate);
        AuthorOutDto authorOutDto = new AuthorOutDto(1, "Terry Pratchett", true, "Reino Unido", formattedDate, List.of());

        when(authorService.createAuthor(authorInDTO)).thenReturn(authorOutDto);

        String requestBody = objectMapper.writeValueAsString(authorInDTO);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/authors", authorInDTO)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.activeAuthor").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationality").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.books").exists())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        AuthorOutDto result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(result);
        assertEquals(201, response.getResponse().getStatus());
        assertEquals("Reino Unido", result.getNationality());
        assertEquals(true, result.getActiveAuthor());

        verify(authorService, times(1)).createAuthor(authorInDTO);
    }

//    @Test
//    public void testAddAuthorValidationError() throws Exception {
//        LocalDate date = LocalDate.of(1948, 4, 28);
//        Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        AuthorInDto authorInDTO = new AuthorInDto("Terry Pratchett", true, null, formattedDate);
//
//        String requestBody = objectMapper.writeValueAsString(authorInDTO);
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/authors", authorInDTO)
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
//    }

//    @Test
//    public void testAddAuthorNotFound() throws Exception {
//        LocalDate date = LocalDate.of(1948, 4, 28);
//        Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        AuthorInDto authorInDTO = new AuthorInDto("Terry Pratchett", true, "Reino Unido", formattedDate);
//
//        when(authorService.createAuthor(authorInDTO)).thenThrow();
//
//        String requestBody = objectMapper.writeValueAsString(authorInDTO);
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/authorss", authorInDTO)
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
//
//        verify(authorService, times(1)).createAuthor(authorInDTO);
//    }

    //endregion

    //region DELETE

    @Test
    public void testRemoveAuthorOk() throws Exception {
        int authorId = 4;

        doNothing().when(authorService).deleteAuthor(authorId);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/authors/{authorId}", authorId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(204, response.getResponse().getStatus());

        verify(authorService, times(1)).deleteAuthor(authorId);
    }

//    @Test
//    public void testDeleteAuthorValidationError() throws Exception {
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/author/authorID"))
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
//    public void testRemoveAuthorNotFound() throws Exception {
//        int authorId = 8;
//
//        doThrow(new AuthorNotFoundException("Author not found with ID: " + authorId))
//                .when(authorService).deleteAuthor(authorId);
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/authors/{authorId}", authorId)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);
//
//        assertEquals(404, result.getStatusCode());
//
//        verify(authorService, times(1)).deleteAuthor(authorId);
//    }

    //endregion

    //region PUT

    @Test
    public void testModifyAuthorOk() throws Exception {
        int authorId = 1;
        LocalDate date = LocalDate.of(1948, 4, 28);
        Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        AuthorInDto authorInDTO = new AuthorInDto("Terry Pratchett", true, "Reino Unido", formattedDate);
        AuthorOutDto authorOutDto = new AuthorOutDto(1, "Terry Pratchett", true, "Reino Unido", formattedDate, List.of());

        when(authorService.updateAuthor(authorId, authorInDTO)).thenReturn(authorOutDto);

        String requestBody = objectMapper.writeValueAsString(authorInDTO);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/authors/{authorId}", authorId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("Terry Pratchett"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activeAuthor").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationality").value("Reino Unido"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(String.valueOf("1948-04-27T23:00:00.000+00:00")))
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
        AuthorOutDto result = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(result);
        assertEquals(200, response.getResponse().getStatus());
        assertEquals(1, result.getId());

        verify(authorService, times(1)).updateAuthor(authorId, authorInDTO);
    }

//    @Test
//    public void testModifyAuthorValidationError() throws Exception {
//        int authorId = 1;
//        LocalDate date = LocalDate.of(1948, 4, 28);
//        Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        AuthorInDto authorInDTO = new AuthorInDto("Terry Pratchett", true, "Reino Unido", formattedDate);
//
//        String requestBody = objectMapper.writeValueAsString(authorInDTO);
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/authors/{authorId}", authorId)
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//        String json = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        ErrorResponse result = objectMapper.readValue(json, ErrorResponse.class);
//
//        assertEquals(400, result.getStatusCode());
//    }

//    @Test
//    public void testModifyAuthorNotFound() throws Exception {
//        int authorId = 1;
//        LocalDate date = LocalDate.of(1948, 4, 28);
//        Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        AuthorInDto authorInDTO = new AuthorInDto("Terry Pratchett", true, "Reino Unido", formattedDate);
//
//        when(authorService.updateAuthor(authorId, authorInDTO))
//                .thenThrow(new AuthorNotFoundException("Author not found with ID: " + authorId));
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/authors/{authorId}", authorId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authorInDTO))
//                        .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        String jsonResponse = response.getResponse().getContentAsString(StandardCharsets.UTF_8);
//        AuthorOutDto result = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
//
//        assertNotNull(result);
//        assertEquals(404, response.getResponse().getStatus());
//
//        verify(authorService, times(1)).updateAuthor(authorId, authorInDTO);
//    }

    //endregion

    //region METHODS

    private List<AuthorOutDto> getAuthors(){
        LocalDate date1 = LocalDate.of(1948, 4, 28);
        LocalDate date2 = LocalDate.of(1965, 7, 31);
        LocalDate date3 = LocalDate.of(1948, 9, 20);

        Date formattedDate1 = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formattedDate2 = Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date formattedDate3 = Date.from(date3.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return List.of(
                new AuthorOutDto(1, "Terry Pratchett", true, "Reino Unido", formattedDate1, List.of()),
                new AuthorOutDto(2, "J.K. Rowling", true, "Reino Unido", formattedDate2, List.of()),
                new AuthorOutDto(3, "George R.R. Martin", true, "Estados Unidos", formattedDate3, List.of())
        );
    }

    //endregion
}
