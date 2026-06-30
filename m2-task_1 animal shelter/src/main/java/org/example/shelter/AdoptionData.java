package org.example.shelter;

import org.example.model.Animal;

import java.time.LocalDate;

public record AdoptionData(Animal animal, LocalDate adoptionDate, String adopterName) {
    @Override
    public String toString() {
        return animal.getName() + "- adoption date: " + adoptionDate + " adopter: " + adopterName;
    }
}
