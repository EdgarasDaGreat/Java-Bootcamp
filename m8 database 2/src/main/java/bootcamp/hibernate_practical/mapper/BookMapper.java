package bootcamp.hibernate_practical.mapper;

import bootcamp.hibernate_practical.dto.BookResponseDto;
import bootcamp.hibernate_practical.dto.CreateBookRequestDto;
import bootcamp.hibernate_practical.dto.UpdateBookRequestDto;
import bootcamp.hibernate_practical.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponseDto toDto (Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPublicationYear(),
                book.isAvailable()
        );
    }

    public Book toCreateEntity (CreateBookRequestDto request) {
        return new Book(
                request.title(),
                request.author(),
                request.genre(),
                request.publicationYear(),
                true);
    }

    public Book toUpdateEntity (Book book, UpdateBookRequestDto request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setPublicationYear(request.publicationYear());
        book.setAvailable(request.available());
        return book;
    }
}
