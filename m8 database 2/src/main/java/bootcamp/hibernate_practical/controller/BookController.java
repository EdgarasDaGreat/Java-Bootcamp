package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.dto.BookResponseDto;
import bootcamp.hibernate_practical.dto.CreateBookRequestDto;
import bootcamp.hibernate_practical.dto.UpdateBookRequestDto;
import bootcamp.hibernate_practical.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody CreateBookRequestDto createBookRequest) {
        return ResponseEntity.ok().body(bookService.createBook(createBookRequest));
    }

    @GetMapping
    public List<BookResponseDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponseDto updateBook(@PathVariable Long id,@Valid @RequestBody UpdateBookRequestDto updateBookRequest) {
        return bookService.updateBook(id, updateBookRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/author/{author}")
    public List<BookResponseDto> getBooksByAuthor(@PathVariable String author) {
        return bookService.findByAuthor(author);
    }

    @GetMapping("/available")
    public List<BookResponseDto> getAvailableBooks() {
        return bookService.findAvailableBooks();
    }

}
