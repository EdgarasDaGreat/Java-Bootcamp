package lv.bootcamp.shelter.task5;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Task 5: Nested test classes for CSV parsing
 * <p>
 * Practice:
 * - @Nested to organize by scenario
 * - @DisplayName for readable output
 * - Testing Optional results (isPresent, isEmpty)
 * - Testing file I/O with temp files
 * <p>
 * Instructions:
 * Write tests for AnimalCsvParser. Use @Nested classes to group tests by scenario.
 * For file-based tests, use Files.createTempFile() and Files.writeString() to create test data.
 */
@DisplayName("AnimalCsvParser")
class AnimalCsvParserTest {

    private AnimalCsvParser parser;

    @BeforeEach
    void setUp() {
        parser = new AnimalCsvParser();
    }

    // ==================== parseRow tests ====================

    @Nested
    @DisplayName("When parsing valid rows")
    class ValidRows {

        @Test
        @DisplayName("parses a complete row into an Animal")
        void shouldParseCompleteRow() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true,2026-01-15");
            assert(result.isPresent());
            Animal animal = result.get();
            assert(animal.getName().equals("Buddy"));
            assert(animal.getSpecies().equals("Dog"));
            assert(animal.getAge() == 3);
            assert(animal.isVaccinated());
            assert(animal.getIntakeDate().equals(LocalDate.of(2026, 1, 15)));
        }

        @Test
        @DisplayName("trims whitespace from fields")
        void shouldTrimWhitespace() {
            Optional<Animal> result = parser.parseRow("  Buddy , Dog , 3 , true , 2026-01-15 ");
            assert(result.isPresent());
            Animal animal = result.get();
            assert(animal.getName().equals("Buddy"));
            assert(animal.getSpecies().equals("Dog"));
        }

        @Test
        @DisplayName("parses vaccinated=false correctly")
        void shouldParseFalseVaccination() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,false,2026-01-15");
            assert(result.isPresent());
            Animal animal = result.get();
            assert(!animal.isVaccinated());
        }
    }

    @Nested
    @DisplayName("When parsing malformed rows")
    class MalformedRows {

        @Test
        @DisplayName("returns empty for null input")
        void shouldReturnEmptyForNull() {
            Optional<Animal> result = parser.parseRow(null);
            assert(result.isEmpty());
        }

        @Test
        @DisplayName("returns empty for blank input")
        void shouldReturnEmptyForBlank() {
            Optional<Animal> result = parser.parseRow("   ");
            assert(result.isEmpty());
        }

        @Test
        @DisplayName("returns empty when row has fewer than 5 fields")
        void shouldReturnEmptyForTooFewFields() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true");
            assert(result.isEmpty());
        }

        @Test
        @DisplayName("returns empty when name is missing")
        void shouldReturnEmptyForMissingName() {
            Optional<Animal> result = parser.parseRow(",Dog,3,true,2026-01-15");
            assert(result.isEmpty());
        }

        @Test
        @DisplayName("returns empty when age is not a number")
        void shouldReturnEmptyForBadAge() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,old,true,2026-01-15");
            assert(result.isEmpty());
        }

        @Test
        @DisplayName("returns empty when age is negative")
        void shouldReturnEmptyForNegativeAge() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,-1,true,2026-01-15");
            assert(result.isEmpty());
        }

        @Test
        @DisplayName("returns empty when date is invalid")
        void shouldReturnEmptyForBadDate() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true,not-a-date");
            assert(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("When handling edge cases")
    class EdgeCases {

        @Test
        @DisplayName("handles vaccinated field as any non-true string → false")
        void shouldTreatNonTrueAsFalse() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,maybe,2026-01-15");
            assert(result.isPresent());
            Animal animal = result.get();
            assert(!animal.isVaccinated());
        }

        @Test
        @DisplayName("handles age 0 as valid")
        void shouldAcceptAgeZero() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,0,true,2026-01-15");
            assert(result.isPresent());
            Animal animal = result.get();
            assert(animal.getAge() == 0);
        }
    }

    // ==================== parseFile tests ====================

    @Nested
    @DisplayName("When parsing a CSV file")
    class ParseFile {

        @Test
        @DisplayName("parses valid rows and counts skipped rows")
        void shouldParseFileAndCountSkipped() throws IOException {
            Path tempFile = Files.createTempFile("test-intake", ".csv");
            String content = """
                    name,species,age,vaccinated,intakeDate
                    Buddy,Dog,3,true,2026-01-15
                    Luna,Cat,2,true,2026-01-10
                    Max,Dog,5,false,2026-01-20
                    Bella,Cat,1,true,not-a-date
                    """;
            Files.writeString(tempFile, content, StandardCharsets.UTF_8);
            AnimalCsvParser.ParseResult result = parser.parseFile(tempFile);
            Files.deleteIfExists(tempFile);
            assert(result.animals().size() == 3);
            assert(result.skippedRows() == 1);
        }

        @Test
        @DisplayName("returns empty result for file with only a header")
        void shouldReturnEmptyForHeaderOnly() throws IOException {
            Path tempFile = Files.createTempFile("test-intake", ".csv");
            String content = """
                    name,species,age,vaccinated,intakeDate
                    """;
            Files.writeString(tempFile, content, StandardCharsets.UTF_8);
            AnimalCsvParser.ParseResult result = parser.parseFile(tempFile);
            Files.deleteIfExists(tempFile);
            assert(result.animals().isEmpty());
            assert(result.skippedRows() == 0);
        }

        @Test
        @DisplayName("throws IOException for non-existent file")
        void shouldThrowForMissingFile() {
            assertThrows(IOException.class, () -> parser.parseFile(Path.of("does-not-exist.csv")));
        }
    }
}
