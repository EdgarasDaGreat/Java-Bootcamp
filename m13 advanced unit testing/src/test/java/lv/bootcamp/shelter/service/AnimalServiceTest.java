package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.dto.AdoptionRequest;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.repository.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// TODO: add any imports you need as you write the tests

/**
 * Task: Service-layer tests with Mockito.
 *
 * Use @Mock, @InjectMocks, stubbing, verify(), and ArgumentCaptor.
 * Write Arrange-Act-Assert for each method.
 */
@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void create_shouldSaveAnimalWithAvailableStatus() {
        AnimalCreateRequest request = new AnimalCreateRequest("Rex", AnimalType.DOG, "German shepard", 3, "Friendly");
        Animal savedAnimal = new Animal(1L, "Rex", AnimalType.DOG, "German shepard", 3, "Friendly", AnimalStatus.AVAILABLE);

        when(animalRepository.save(any(Animal.class))).thenReturn(savedAnimal);

        AnimalResponse response = animalService.create(request);

        assertThat(response.id()).isEqualTo(1);
        assertThat(response.name()).isEqualTo("Rex");
        assertThat(response.status()).isEqualTo(AnimalStatus.AVAILABLE);

        ArgumentCaptor<Animal> captor = ArgumentCaptor.forClass(Animal.class);
        verify(animalRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(AnimalStatus.AVAILABLE);
    }

    @Test
    void findById_shouldThrowWhenAnimalNotFound() {
        when(animalRepository.findById(99L)).thenReturn(Optional.empty());
        AnimalNotFoundException ex = assertThrows(AnimalNotFoundException.class,  () -> animalService.findById(99L));
        assertThat(ex).hasMessageContaining("99");
    }

    @Test
    void adopt_shouldChangeStatusAndSendNotification() {
        Animal availableAnimal = new Animal(1L, "Rex", AnimalType.DOG, "German shepard", 3, "Friendly", AnimalStatus.AVAILABLE);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(availableAnimal));
        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnimalResponse response = animalService.adopt(new AdoptionRequest(1L, "John","john@example.com"));

        assertThat(response.status()).isEqualTo(AnimalStatus.ADOPTED);
        verify(notificationClient).sendAdoptionNotification(1L,"Rex","john@example.com");
    }

    @Test
    void adopt_shouldThrowWhenAnimalAlreadyAdopted() {
        Animal availableAnimal = new Animal(1L, "Rex", AnimalType.DOG, "German shepard", 3, "Friendly", AnimalStatus.ADOPTED);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(availableAnimal));

        assertThrows(IllegalStateException.class, () ->
                animalService.adopt(new AdoptionRequest(1L, "John","john@example.com")));
        verify(notificationClient, never()).sendAdoptionNotification(anyLong(), anyString(), anyString());
    }

    @Test
    void reserveMultiple_shouldNotifyWithReservedIds() {
        Animal availableAnimal1 = new Animal(1L, "Rex", AnimalType.DOG, "German shepard", 3, "Friendly", AnimalStatus.AVAILABLE);
        Animal availableAnimal2 = new Animal(2L, "Fiona", AnimalType.CAT, "Fluffy", 4, "Friendly", AnimalStatus.AVAILABLE);
        when(animalRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(availableAnimal1, availableAnimal2));

        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<AnimalResponse> responses = animalService.reserveMultiple(List.of(1L, 2L));
        for (AnimalResponse response : responses) {
            assertThat(response.status()).isEqualTo(AnimalStatus.RESERVED);
        }
    }
}
