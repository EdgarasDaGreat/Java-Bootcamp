package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponseDto;
import bootcamp.hibernate_practical.dto.CreateBookRequestDto;
import bootcamp.hibernate_practical.dto.UpdateBookRequestDto;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.mapper.BookMapper;
import bootcamp.hibernate_practical.repository.BookRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.mapper = new BookMapper();
    }

    public BookResponseDto createBook(CreateBookRequestDto request) {
        Book book = mapper.toCreateEntity(request);
        bookRepository.save(book);
        return mapper.toDto(book);
    }

    public List<BookResponseDto> getAllBooks() {
        List<Book> bookEntities = bookRepository.findAll();
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        for (Book bookEntity : bookEntities) {
            bookResponseDtos.add(mapper.toDto(bookEntity));
        }
        return bookResponseDtos;
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id: " + id + " not found"));
        return mapper.toDto(book);
    }

    public BookResponseDto updateBook(Long id, UpdateBookRequestDto request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id: " + id + " not found"));
        Book updatedBook = mapper.toUpdateEntity(book, request);
        bookRepository.save(updatedBook);
        return mapper.toDto(updatedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository
                .findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id: " + id + " not found"));
        bookRepository.delete(book);
    }

    public List<BookResponseDto> findByAuthor(String author) {
        List<Book> booksByAuthor = new ArrayList<>();
        booksByAuthor = bookRepository.findByAuthor(author);
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        for (Book book : booksByAuthor) {
            bookResponseDtos.add(mapper.toDto(book));
        }
        return bookResponseDtos;
    }

    public List<BookResponseDto> findAvailableBooks() {
        List<Book> availableBooks = bookRepository.findByAvailableTrue();
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        for (Book book : availableBooks) {
            bookResponseDtos.add(mapper.toDto(book));
        }
        return bookResponseDtos;
    }
}
