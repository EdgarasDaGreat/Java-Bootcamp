package org.example.model;

public final class Hamster extends Animal {
    public Hamster(AnimalId id, String name, int age) {
        super(id, name, age);
    }

    @Override
    public AnimalSpecies getSpecies() {
        return AnimalSpecies.HAMSTER;
    }
}
