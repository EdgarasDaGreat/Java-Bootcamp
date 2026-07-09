package lv.bootcamp.shelter;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.dto.AdoptionRequest;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO: add imports as you write the test (e.g. assertThat, verify)

/**
 * Task: Integration test with @SpringBootTest.
 *
 * The full application context loads — use @MockitoBean only for the external
 * NotificationClient. Everything else (service, repository, JPA) is real.
 * @Transactional rolls back after each test.
 */
@SpringBootTest
@Transactional
class AdoptionIntegrationTest {

    @Autowired
    private AnimalService animalService;

    @MockitoBean
    private NotificationClient notificationClient;

    @Test
    void adoptionFlow_shouldPersistStatusAndNotifyExternalSystem() {
        AnimalCreateRequest request = new AnimalCreateRequest("Rex", AnimalType.DOG, "German shepard", 3, "Friendly");

        AnimalResponse response = animalService.create(request);

        assertThat(response.status()).isEqualTo(AnimalStatus.AVAILABLE);

        AdoptionRequest adoptionRequest = new AdoptionRequest(response.id(), "John", "test@gmail.com");
        animalService.adopt(adoptionRequest);

        response = animalService.findById(response.id());
        assertThat(response.status()).isEqualTo(AnimalStatus.ADOPTED);

        verify(notificationClient).sendAdoptionNotification(response.id(), "Rex", "test@gmail.com");

        response = animalService.findById(response.id());
        AnimalResponse saved = animalService.findById(response.id());
        assertThat(saved.status()).isEqualTo(AnimalStatus.ADOPTED);
    }
}
