package lv.bootcamp.shelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalNotFoundException;
import lv.bootcamp.shelter.service.AnimalService;
import lv.bootcamp.shelter.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Task: REST controller tests with MockMvc and @WebMvcTest.
 * <p>
 * Stub the service with @MockitoBean. Use mockMvc.perform() to make requests
 * and chain .andExpect() calls to verify status, JSON content, and error responses.
 */
@WebMvcTest(AnimalController.class)
@Import(SecurityConfig.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AnimalService animalService;

    @Test
    void findAll_shouldReturnListOfAnimals() throws Exception {
        AnimalResponse response1 = new AnimalResponse(1L, "Rex",
                AnimalType.DOG, "German", 3, "Friendly", AnimalStatus.AVAILABLE);
        AnimalResponse response2 = new AnimalResponse(2L, "Fiona",
                AnimalType.CAT, "idk", 3, "fluffy", AnimalStatus.AVAILABLE);

        when(animalService.findAll()).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[1].name").value("Fiona"));
    }

    @Test
    void findById_shouldReturn404WhenNotFound() throws Exception {
        when(animalService.findById(99L)).thenThrow(new AnimalNotFoundException(99L));
        mockMvc.perform(get("/api/animals/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201WithCreatedAnimal() throws Exception {
        AnimalResponse response1 = new AnimalResponse(1L, "Rex",
                AnimalType.DOG, "German", 3, "Friendly", AnimalStatus.AVAILABLE);

        when(animalService.create(any(AnimalCreateRequest.class))).thenReturn(response1);

        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Rex",
                                "type": "DOG",
                                "breed": "German shepard",
                                "age": 3,
                                "description": "Friendly"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void create_shouldReturn400WhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "type": "DOG",
                                "breed": "German shepard",
                                "age": 3,
                                "description": "Friendly"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400WhenTypeIsNull() throws Exception {
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Rex",
                                "type": null,
                                "breed": "German shepard",
                                "age": 3,
                                "description": "Friendly"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
