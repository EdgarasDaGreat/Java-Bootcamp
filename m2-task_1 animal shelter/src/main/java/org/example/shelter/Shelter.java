package org.example.shelter;

import org.example.model.AdoptionStatus;
import org.example.model.Animal;
import org.example.model.AnimalSpecies;

import java.util.ArrayList;
import java.util.List;

public class Shelter<T extends Animal> {
    private final List<T> animals = new ArrayList<>();

    public void addAnimal(T animal) {
        animals.add(animal);
    }

    public List<T> getAllAnimals() {
        return animals;
    }

    public List<T> findBySpecies(AnimalSpecies species) {
        List<T> animalsOfSpecies = new ArrayList<>();
        for (T animal : animals) {
            if (animal.getSpecies() == species) {
                animalsOfSpecies.add(animal);
            }
        }
        return animalsOfSpecies;
    }

    public List<T> findAvailableAnimals() {
        List<T> availableAnimals = new ArrayList<>();
        for (T animal : animals) {
            if (animal.getAdoptionStatus() == AdoptionStatus.AVAILABLE) {
                availableAnimals.add(animal);
            }
        }
        return availableAnimals;
    }

    public void markAsAdopted(String id) {
        for (T animal : animals) {
            if (animal.getId().toString().equals(id)) {
                animal.markAsAdopted();
            }
        }
    }

    public boolean isIdTaken(String id) {
        for (T animal : animals) {
            if (animal.getId().toString().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
