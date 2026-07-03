package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.dto.BookResponseDto;
import bootcamp.hibernate_practical.dto.CreateBookRequestDto;
import bootcamp.hibernate_practical.dto.UpdateBookRequestDto;
import bootcamp.hibernate_practical.service.BookService;
import jakarta.validation.Valid;
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
    public BookResponseDto createBook(@Valid @RequestBody CreateBookRequestDto createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @GetMapping
    public List<BookResponseDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponseDto getBookById(@Valid @PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponseDto updateBook(@Valid @PathVariable Long id,@Valid @RequestBody UpdateBookRequestDto updateBookRequest) {
        return bookService.updateBook(id, updateBookRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@Valid @PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/author/{author}")
    public List<BookResponseDto> getBooksByAuthor(@Valid @PathVariable String author) {
        return bookService.findByAuthor(author);
    }

    @GetMapping("/available")
    public List<BookResponseDto> getAvailableBooks() {
        return bookService.findAvailableBooks();
    }

}
