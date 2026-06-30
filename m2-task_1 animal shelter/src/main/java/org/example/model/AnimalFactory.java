package org.example.model;

public final class AnimalFactory {
    private AnimalFactory() {}

    public static Animal createAnimal(AnimalSpecies species, String name, int age, AnimalId animalId){
        return switch(species) {
            case DOG -> new Dog(animalId,name,age);
            case CAT -> new Cat(animalId,name,age);
            case BIRD -> new Bird(animalId,name,age);
            case HAMSTER -> new Hamster(animalId,name,age);
        };
    }
}
