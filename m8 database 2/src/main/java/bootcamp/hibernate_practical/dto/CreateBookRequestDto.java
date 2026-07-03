package bootcamp.hibernate_practical.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Valid
public record CreateBookRequestDto(
        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String genre,

        @Max(2026)
        @Min(0)
        int publicationYear
) {}
