package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponseDto;
import bootcamp.hibernate_practical.dto.CreateBookRequestDto;
import bootcamp.hibernate_practical.dto.UpdateBookRequestDto;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.mapper.BookMapper;
import bootcamp.hibernate_practical.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponseDto createBook(CreateBookRequestDto request) {
        BookMapper mapper = new BookMapper();
        Book book = mapper.toCreateEntity(request);
        bookRepository.save(book);
        return mapper.toDto(book);
    }

    public List<BookResponseDto> getAllBooks() {
        BookMapper mapper = new BookMapper();
        List<Book> bookEntities = bookRepository.findAll();
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        for (Book bookEntity : bookEntities) {
            bookResponseDtos.add(mapper.toDto(bookEntity));
        }
        return bookResponseDtos;
    }

    public BookResponseDto getBookById(Long id) {
        BookMapper mapper = new BookMapper();
        Book book = bookRepository.findById(id).orElseThrow(()-> new RuntimeException("Book not found with id:" + id));
        return mapper.toDto(book);
    }

    public BookResponseDto updateBook(Long id, UpdateBookRequestDto request) {
        BookMapper mapper = new BookMapper();
        Book book = bookRepository.findById(id).orElseThrow(()-> new RuntimeException("Book not found with id:" + id));
        Book updatedBook = mapper.toUpdateEntity(book, request);
        bookRepository.save(updatedBook);
        return mapper.toDto(updatedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()-> new RuntimeException("Book not found with id:" + id));
        bookRepository.delete(book);
    }

    public List<BookResponseDto> findByAuthor(String author) {
        BookMapper mapper = new BookMapper();
        List<Book> booksByAuthor = new ArrayList<>();
        booksByAuthor = bookRepository.findByAuthor(author);
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        for (Book book : booksByAuthor) {
            bookResponseDtos.add(mapper.toDto(book));
        }
        return bookResponseDtos;
    }

    public List<BookResponseDto> findAvailableBooks() {
        BookMapper mapper = new BookMapper();
        List<Book> availableBooks = bookRepository.findByAvailableTrue();
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();
        for (Book book : availableBooks) {
            bookResponseDtos.add(mapper.toDto(book));
        }
        return bookResponseDtos;
    }
}
