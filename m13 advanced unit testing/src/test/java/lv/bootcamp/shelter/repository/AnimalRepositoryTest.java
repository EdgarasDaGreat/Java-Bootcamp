package lv.bootcamp.shelter.repository;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Task: Repository tests with @DataJpaTest.
 *
 * Use entityManager.persist() + entityManager.flush() to set up test data.
 * Each test rolls back automatically — no cleanup needed.
 */
@DataJpaTest
class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_shouldPersistAnimalAndGenerateId() {
        Animal animal = new Animal(null, "Rex", AnimalType.DOG, "German shepard", 3, "Friendly", AnimalStatus.AVAILABLE);
        Animal savedAnimal = animalRepository.save(animal);
        assertThat(savedAnimal.getId()).isNotNull();
    }

    @Test
    void findByStatus_shouldReturnOnlyMatchingAnimals() {
        entityManager.persist(new Animal(null, "Rex", AnimalType.DOG,
                "German shepard", 3, "Friendly", AnimalStatus.AVAILABLE));
        entityManager.persist(new Animal(null, "Fiona", AnimalType.CAT,
                "Fluffy", 3, "Friendly", AnimalStatus.AVAILABLE));
        entityManager.persist(new Animal(null, "Pete", AnimalType.BIRD,
                "Dragon", 3, "Friendly", AnimalStatus.ADOPTED));
        entityManager.flush();

        List<Animal> availableAnimals = animalRepository.findByStatus(AnimalStatus.AVAILABLE);

        assertThat(availableAnimals).hasSize(2);
    }

    @Test
    void findByType_shouldReturnAnimalsOfGivenType() {
        entityManager.persist(new Animal(null, "Rex", AnimalType.DOG,
                "German shepard", 3, "Friendly", AnimalStatus.AVAILABLE));
        entityManager.persist(new Animal(null, "Fiona", AnimalType.CAT,
                "Fluffy", 3, "Friendly", AnimalStatus.AVAILABLE));
        entityManager.flush();

        List<Animal> dogs = animalRepository.findByType(AnimalType.DOG);

        assertThat(dogs).hasSize(1);
    }

    @Test
    void findByNameContainingIgnoreCase_shouldMatchPartialName() {
        entityManager.persist(new Animal(null, "Rex", AnimalType.DOG,
                "German shepard", 3, "Friendly", AnimalStatus.AVAILABLE));
        entityManager.persist(new Animal(null, "Rexy Jr", AnimalType.CAT,
                "Fluffy", 3, "Friendly", AnimalStatus.AVAILABLE));
        entityManager.persist(new Animal(null, "Mia", AnimalType.BIRD,
                "Dragon", 3, "Friendly", AnimalStatus.ADOPTED));
        entityManager.flush();

        List<Animal> results = animalRepository.findByNameContainingIgnoreCase("rex");

        assertThat(results).hasSize(2);
    }
}
