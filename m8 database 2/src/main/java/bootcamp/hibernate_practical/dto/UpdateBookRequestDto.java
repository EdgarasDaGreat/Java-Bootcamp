package bootcamp.hibernate_practical.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Valid
public record UpdateBookRequestDto(
        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String genre,

        @Max (2026)
        @Min(0)
        int publicationYear,

        boolean available
) {}
