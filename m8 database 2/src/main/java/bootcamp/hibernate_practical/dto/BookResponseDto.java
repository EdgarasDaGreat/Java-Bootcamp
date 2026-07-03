package bootcamp.hibernate_practical.dto;

import java.time.LocalDate;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        String genre,
        int publicationYear,
        boolean available
){}
