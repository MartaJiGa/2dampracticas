package com.svalero.bookapi;

import com.svalero.bookapi.domain.Author;
import com.svalero.bookapi.domain.dto.AuthorInDto;
import com.svalero.bookapi.domain.dto.AuthorOutDto;
import com.svalero.bookapi.exception.AuthorNotFoundException;
import com.svalero.bookapi.repository.AuthorRepository;
import com.svalero.bookapi.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTests {

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ModelMapper modelMapper;

    // Test que verifica que getAllAuthors devuelve una lista correctamente mapeada de AuthorOutDto
    @Test
    public void testGetAllAuthors() {
        // Crear autores de ejemplo
        Author author1 = new Author(1, "Autor Uno", true, "España", new Date());
        Author author2 = new Author(2, "Autor Dos", false, "Francia", new Date());

        List<Author> authors = List.of(author1, author2);

        // Crear los DTOs que devolverá modelMapper (sin libros para simplificar)
        AuthorOutDto dto1 = new AuthorOutDto(1, "Autor Uno", true, "España", author1.getBirthDate(), null);
        AuthorOutDto dto2 = new AuthorOutDto(2, "Autor Dos", false, "Francia", author2.getBirthDate(), null);

        // Mockear el repositorio y el mapeo
        when(authorRepository.findAll()).thenReturn(authors);
        when(modelMapper.map(author1, AuthorOutDto.class)).thenReturn(dto1);
        when(modelMapper.map(author2, AuthorOutDto.class)).thenReturn(dto2);

        // Ejecutar el método que testeamos
        List<AuthorOutDto> result = authorService.getAllAuthors();

        // Validaciones básicas
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Autor Uno", result.get(0).getAuthorName());
        assertEquals(true, result.get(0).getActiveAuthor());
        assertEquals("España", result.get(0).getNationality());

        assertEquals("Autor Dos", result.get(1).getAuthorName());
        assertEquals(false, result.get(1).getActiveAuthor());
        assertEquals("Francia", result.get(1).getNationality());

        // Verificar que se invocaron los métodos esperados
        verify(authorRepository).findAll();
        verify(modelMapper).map(author1, AuthorOutDto.class);
        verify(modelMapper).map(author2, AuthorOutDto.class);
    }
    // Test para getAuthorDetails — caso en el que existe el autor
    @Test
    public void testGetAuthorDetailsFound() {
        // Crear autor de ejemplo
        Author author = new Author(1, "Autor Test", true, "España", new Date());
        AuthorOutDto dto = new AuthorOutDto(1, "Autor Test", true, "España", author.getBirthDate(), null);

        // Mockear comportamiento del repositorio y modelMapper
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(modelMapper.map(author, AuthorOutDto.class)).thenReturn(dto);

        // Ejecutar método
        AuthorOutDto result = authorService.getAuthorDetails(1);

        // Comprobar que el resultado no es null y tiene los valores correctos
        assertNotNull(result);
        assertEquals("Autor Test", result.getAuthorName());
        assertEquals("España", result.getNationality());

        // Verificar que se llamaron los mocks correctos
        verify(authorRepository).findById(1);
        verify(modelMapper).map(author, AuthorOutDto.class);
    }

    // Test para getAuthorDetails — caso en el que NO existe el autor y lanza excepción
    @Test
    public void testGetAuthorDetailsNotFound() {
        // Mockear que no se encuentra el autor
        when(authorRepository.findById(99)).thenReturn(Optional.empty());

        // Verificar que lanza la excepción esperada
        Exception exception = assertThrows(AuthorNotFoundException.class, () -> {
            authorService.getAuthorDetails(99);
        });

        // Comprobar el mensaje de la excepción
        assertEquals("Author not found with ID: 99", exception.getMessage());

        // Verificar llamada al repositorio
        verify(authorRepository).findById(99);
        // El mapper NO debería llamarse en este caso
        verifyNoInteractions(modelMapper);
    }
    // Test para createAuthor — debe guardar un autor y devolver el DTO resultante
    @Test
    public void testCreateAuthor() {
        // Crear AuthorInDto de entrada
        AuthorInDto authorInDto = new AuthorInDto();
        authorInDto.setAuthorName("Nuevo Autor");
        authorInDto.setActiveAuthor(true);
        authorInDto.setNationality("Argentina");
        authorInDto.setBirthDate(new Date());

        // Crear Author y AuthorOutDto simulados
        Author author = new Author(null, "Nuevo Autor", true, "Argentina", authorInDto.getBirthDate());
        Author savedAuthor = new Author(1, "Nuevo Autor", true, "Argentina", authorInDto.getBirthDate());
        AuthorOutDto authorOutDto = new AuthorOutDto(1, "Nuevo Autor", true, "Argentina", authorInDto.getBirthDate(), null);

        // Mockear comportamiento del modelMapper y authorRepository
        when(modelMapper.map(authorInDto, Author.class)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);
        when(modelMapper.map(savedAuthor, AuthorOutDto.class)).thenReturn(authorOutDto);

        // Ejecutar método
        AuthorOutDto result = authorService.createAuthor(authorInDto);

        // Comprobar resultado
        assertNotNull(result);
        assertEquals("Nuevo Autor", result.getAuthorName());
        assertEquals("Argentina", result.getNationality());
        assertEquals(1, result.getId());

        // Verificar llamadas a los mocks
        verify(modelMapper).map(authorInDto, Author.class);
        verify(authorRepository).save(author);
        verify(modelMapper).map(savedAuthor, AuthorOutDto.class);
    }

    // Test para updateAuthor — debe actualizar un autor existente y devolver el DTO actualizado
    @Test
    public void testUpdateAuthorSuccess() {
        // Crear AuthorInDto con los nuevos datos
        AuthorInDto authorInDto = new AuthorInDto();
        authorInDto.setAuthorName("Autor Actualizado");
        authorInDto.setActiveAuthor(false);
        authorInDto.setNationality("Chile");
        authorInDto.setBirthDate(new Date());

        // Autor original simulado
        Author existingAuthor = new Author(1, "Antiguo Autor", true, "Perú", new Date());

        // Autor actualizado simulado
        Author updatedAuthor = new Author(1, "Autor Actualizado", false, "Chile", authorInDto.getBirthDate());

        // DTO de salida esperado
        AuthorOutDto authorOutDto = new AuthorOutDto(1, "Autor Actualizado", false, "Chile", authorInDto.getBirthDate(), null);

        // Mockear comportamientos
        when(authorRepository.findById(1)).thenReturn(Optional.of(existingAuthor));
        doAnswer(invocation -> {
            // Simular mapeo manualmente
            AuthorInDto dto = invocation.getArgument(0);
            Author author = invocation.getArgument(1);
            author.setAuthorName(dto.getAuthorName());
            author.setActiveAuthor(dto.getActiveAuthor());
            author.setNationality(dto.getNationality());
            author.setBirthDate(dto.getBirthDate());
            return null;
        }).when(modelMapper).map(authorInDto, existingAuthor);
        when(authorRepository.save(existingAuthor)).thenReturn(updatedAuthor);
        when(modelMapper.map(updatedAuthor, AuthorOutDto.class)).thenReturn(authorOutDto);

        // Ejecutar método
        AuthorOutDto result = authorService.updateAuthor(1, authorInDto);

        // Comprobar resultado
        assertNotNull(result);
        assertEquals("Autor Actualizado", result.getAuthorName());
        assertEquals("Chile", result.getNationality());
        assertEquals(1, result.getId());
        assertFalse(result.getActiveAuthor());

        // Verificar interacciones
        verify(authorRepository).findById(1);
        verify(modelMapper).map(authorInDto, existingAuthor);
        verify(authorRepository).save(existingAuthor);
        verify(modelMapper).map(updatedAuthor, AuthorOutDto.class);
    }

    // Test para updateAuthor — debe lanzar AuthorNotFoundException si el autor no existe
    @Test
    public void testUpdateAuthorNotFound() {
        // Preparar entrada
        AuthorInDto authorInDto = new AuthorInDto();
        authorInDto.setAuthorName("Autor Inexistente");

        // Simular repositorio sin autor
        when(authorRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecutar y comprobar excepción
        assertThrows(AuthorNotFoundException.class, () -> {
            authorService.updateAuthor(99, authorInDto);
        });

        // Verificar que no se guardó nada ni se mapeó
        verify(authorRepository).findById(99);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(modelMapper);
    }

    // Test para deleteAuthor — debe eliminar un autor existente
    @Test
    public void testDeleteAuthorSuccess() {
        // Simular que el autor existe
        when(authorRepository.existsById(1)).thenReturn(true);

        // Ejecutar método
        authorService.deleteAuthor(1);

        // Verificar que se llamó a existsById y deleteById
        verify(authorRepository).existsById(1);
        verify(authorRepository).deleteById(1);
    }

    // Test para deleteAuthor — debe lanzar AuthorNotFoundException si el autor no existe
    @Test
    public void testDeleteAuthorNotFound() {
        // Simular que el autor no existe
        when(authorRepository.existsById(99)).thenReturn(false);

        // Ejecutar y comprobar excepción
        assertThrows(AuthorNotFoundException.class, () -> {
            authorService.deleteAuthor(99);
        });

        // Verificar que se llamó a existsById y no a deleteById
        verify(authorRepository).existsById(99);
        verify(authorRepository, never()).deleteById(anyInt());
    }

}