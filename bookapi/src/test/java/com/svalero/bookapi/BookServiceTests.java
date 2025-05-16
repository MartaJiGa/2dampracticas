package com.svalero.bookapi;

import com.svalero.bookapi.domain.Author;
import com.svalero.bookapi.domain.Book;
import com.svalero.bookapi.domain.dto.AuthorOutDto;
import com.svalero.bookapi.domain.dto.BookInDto;
import com.svalero.bookapi.domain.dto.BookOutDto;
import com.svalero.bookapi.exception.AuthorNotFoundException;
import com.svalero.bookapi.exception.BookNotFoundException;
import com.svalero.bookapi.repository.AuthorRepository;
import com.svalero.bookapi.repository.BookRepository;
import com.svalero.bookapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ModelMapper modelMapper;

    // Testea que getAllBooks devuelva una lista de libros correctamente mapeados
    @Test
    public void testGetAllBooks() {
        // Crear autores (entidad Author)
        Author author1 = new Author();
        author1.setId(1);
        author1.setAuthorName("Miguel de Cervantes");

        Author author2 = new Author();
        author2.setId(2);
        author2.setAuthorName("George Orwell");

        // Crear libros con fechas usando Date.valueOf(LocalDate)
        Book book1 = new Book(
                1,
                "El Quijote",
                "Novela",
                Date.valueOf(LocalDate.of(1605, 1, 16)),
                true,
                author1
        );

        Book book2 = new Book(
                2,
                "1984",
                "Distopía",
                Date.valueOf(LocalDate.of(1949, 6, 8)),
                true,
                author2
        );

        List<Book> mockBookList = List.of(book1, book2);

        // Crear los DTOs para mockear el ModelMapper
        AuthorOutDto authorOutDto1 = new AuthorOutDto();
        authorOutDto1.setId(1);
        authorOutDto1.setAuthorName("Miguel de Cervantes");

        AuthorOutDto authorOutDto2 = new AuthorOutDto();
        authorOutDto2.setId(2);
        authorOutDto2.setAuthorName("George Orwell");

        BookOutDto bookOutDto1 = new BookOutDto(
                1,
                "El Quijote",
                "Novela",
                Date.valueOf(LocalDate.of(1605, 1, 16)),
                true,
                authorOutDto1
        );

        BookOutDto bookOutDto2 = new BookOutDto(
                2,
                "1984",
                "Distopía",
                Date.valueOf(LocalDate.of(1949, 6, 8)),
                true,
                authorOutDto2
        );

        when(bookRepository.findAll()).thenReturn(mockBookList);
        when(modelMapper.map(book1, BookOutDto.class)).thenReturn(bookOutDto1);
        when(modelMapper.map(book2, BookOutDto.class)).thenReturn(bookOutDto2);

        List<BookOutDto> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("El Quijote", result.get(0).getTitle());
        assertEquals("1984", result.get(1).getTitle());

        verify(bookRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(book1, BookOutDto.class);
        verify(modelMapper, times(1)).map(book2, BookOutDto.class);
    }

    // Testea que getBookDetails devuelva un libro cuando existe
    @Test
    public void testGetBookDetails_Found() {
        Integer bookId = 1;

        // Crear el autor
        Author author = new Author();
        author.setId(1);
        author.setAuthorName("Miguel de Cervantes");

        // Crear el libro con fecha correcta
        Book mockBook = new Book(
                bookId,
                "El Quijote",
                "Novela",
                Date.valueOf(LocalDate.of(1605, 1, 16)),
                true,
                author
        );

        // Crear el AuthorOutDto
        AuthorOutDto authorOutDto = new AuthorOutDto();
        authorOutDto.setId(1);
        authorOutDto.setAuthorName("Miguel de Cervantes");

        // Crear el BookOutDto con fecha correcta
        BookOutDto mockBookOutDto = new BookOutDto(
                bookId,
                "El Quijote",
                "Novela",
                Date.valueOf(LocalDate.of(1605, 1, 16)),
                true,
                authorOutDto
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(modelMapper.map(mockBook, BookOutDto.class)).thenReturn(mockBookOutDto);

        BookOutDto result = bookService.getBookDetails(bookId);

        assertNotNull(result);
        assertEquals("El Quijote", result.getTitle());
        assertEquals("Miguel de Cervantes", result.getAuthor().getAuthorName());

        verify(bookRepository, times(1)).findById(bookId);
        verify(modelMapper, times(1)).map(mockBook, BookOutDto.class);
    }

    // Testea que getBookDetails lance excepción cuando el libro no existe
    @Test
    public void testGetBookDetails_NotFound() {
        Integer bookId = 99;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookDetails(bookId);
        });

        assertEquals("Book not found with ID: " + bookId, exception.getMessage());

        verify(bookRepository, times(1)).findById(bookId);
        verifyNoInteractions(modelMapper);
    }

    // Testea la creación de un libro y que se mapean correctamente los objetos
    @Test
    public void testCreateBook() {
        Date pubDate = Date.valueOf(LocalDate.of(2025, 5, 16));
        BookInDto bookInDto = new BookInDto("Nuevo Libro", "Ficción", pubDate, true, 1);

        Book mappedBook = new Book();
        mappedBook.setTitle("Nuevo Libro");
        mappedBook.setGenre("Ficción");
        mappedBook.setPublicationDate(pubDate);
        mappedBook.setAvailable(true);
        mappedBook.setId(null);

        Book savedBook = new Book();
        savedBook.setId(10);
        savedBook.setTitle("Nuevo Libro");
        savedBook.setGenre("Ficción");
        savedBook.setPublicationDate(pubDate);
        savedBook.setAvailable(true);

        AuthorOutDto authorOutDto = new AuthorOutDto();
        BookOutDto expectedDto = new BookOutDto(10, "Nuevo Libro", "Ficción", pubDate, true, authorOutDto);

        // Simula el mapeo de BookInDto a Book
        doAnswer(invocation -> {
            BookInDto source = invocation.getArgument(0);
            Book destination = invocation.getArgument(1);
            destination.setTitle(source.getTitle());
            destination.setGenre(source.getGenre());
            destination.setPublicationDate(source.getPublicationDate());
            destination.setAvailable(source.getAvailable());
            destination.setId(null);
            return null;
        }).when(modelMapper).map(eq(bookInDto), any(Book.class));

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        when(modelMapper.map(savedBook, BookOutDto.class)).thenReturn(expectedDto);

        BookOutDto result = bookService.createBook(bookInDto);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getTitle(), result.getTitle());
        assertEquals(expectedDto.getGenre(), result.getGenre());
        assertEquals(expectedDto.getPublicationDate(), result.getPublicationDate());
        assertEquals(expectedDto.getAvailable(), result.getAvailable());

        verify(modelMapper).map(eq(bookInDto), any(Book.class));
        verify(bookRepository).save(any(Book.class));
        verify(modelMapper).map(savedBook, BookOutDto.class);
    }

    // Test para actualizar un libro cuando el autor NO cambia
    @Test
    public void testUpdateBook_Success_NoAuthorChange() {
        Integer bookId = 1;

        Author existingAuthor = new Author(1, "Autor Existente", true, "España", Date.valueOf(LocalDate.of(1970,1,1)));
        Book existingBook = new Book(bookId, "Título Antiguo", "Género Antiguo", Date.valueOf(LocalDate.of(2000,1,1)), true, existingAuthor);

        BookInDto bookInDto = new BookInDto("Título Nuevo", "Género Nuevo", Date.valueOf(LocalDate.of(2020,1,1)), false, 1);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Book.class), eq(BookOutDto.class))).thenReturn(new BookOutDto());

        BookOutDto result = bookService.updateBook(bookId, bookInDto);

        assertNotNull(result);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
        verify(modelMapper).map(existingBook, BookOutDto.class);

        assertEquals(existingAuthor, existingBook.getAuthor());
        assertEquals("Título Nuevo", existingBook.getTitle());
        assertEquals("Género Nuevo", existingBook.getGenre());
        assertFalse(existingBook.getAvailable());
    }

    // Test para actualizar un libro cuando el autor cambia correctamente
    @Test
    public void testUpdateBook_Success_AuthorChanged() {
        Integer bookId = 1;

        Author oldAuthor = new Author(1, "Autor Viejo", true, "España", Date.valueOf(LocalDate.of(1950,1,1)));
        Author newAuthor = new Author(2, "Autor Nuevo", true, "Francia", Date.valueOf(LocalDate.of(1960,1,1)));
        Book existingBook = new Book(bookId, "Título", "Género", Date.valueOf(LocalDate.of(2000,1,1)), true, oldAuthor);

        BookInDto bookInDto = new BookInDto("Título", "Género", Date.valueOf(LocalDate.of(2020,1,1)), true, 2);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(2)).thenReturn(Optional.of(newAuthor));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Book.class), eq(BookOutDto.class))).thenReturn(new BookOutDto());

        BookOutDto result = bookService.updateBook(bookId, bookInDto);

        assertNotNull(result);
        verify(bookRepository).findById(bookId);
        verify(authorRepository).findById(2);
        verify(bookRepository).save(existingBook);
        verify(modelMapper).map(existingBook, BookOutDto.class);

        assertEquals(newAuthor, existingBook.getAuthor());
    }

    // Test para cuando el libro a actualizar NO existe, debe lanzar excepción
    @Test
    public void testUpdateBook_BookNotFound() {
        Integer bookId = 999;
        BookInDto bookInDto = new BookInDto();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(bookId, bookInDto);
        });

        assertEquals("Book not found with ID: " + bookId, exception.getMessage());

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
        verifyNoInteractions(modelMapper);
    }

    // Test para cuando el autor nuevo NO existe, debe lanzar excepción
    @Test
    public void testUpdateBook_AuthorNotFound() {
        Integer bookId = 1;
        Integer newAuthorId = 999;
        Author oldAuthor = new Author(1, "Autor Viejo", true, "España", Date.valueOf(LocalDate.of(1950,1,1)));
        Book existingBook = new Book(bookId, "Título", "Género", Date.valueOf(LocalDate.of(2000,1,1)), true, oldAuthor);
        BookInDto bookInDto = new BookInDto("Título", "Género", Date.valueOf(LocalDate.of(2020,1,1)), true, newAuthorId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(newAuthorId)).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () -> {
            bookService.updateBook(bookId, bookInDto);
        });

        assertEquals("Author not found with ID: " + newAuthorId, exception.getMessage());

        verify(bookRepository).findById(bookId);
        verify(authorRepository).findById(newAuthorId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(modelMapper);
    }

    // Test que verifica que un libro se elimina correctamente si existe
    @Test
    public void testDeleteBook_Success() {
        Integer bookId = 1;

        // Simulamos que el libro existe
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Llamamos al método deleteBook
        bookService.deleteBook(bookId);

        // Verificamos que se llamó existsById y deleteById con el id correcto
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    // Test que verifica que se lanza BookNotFoundException si el libro no existe al intentar eliminarlo
    @Test
    public void testDeleteBook_NotFound() {
        Integer bookId = 99;

        // Simulamos que el libro NO existe
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Comprobamos que se lanza la excepción BookNotFoundException
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.deleteBook(bookId);
        });

        assertEquals("Book not found with ID: " + bookId, exception.getMessage());

        // Verificamos que se llamó existsById pero no deleteById
        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).deleteById(anyInt());
    }

}