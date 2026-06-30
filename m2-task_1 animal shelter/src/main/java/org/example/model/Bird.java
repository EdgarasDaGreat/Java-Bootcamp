package org.example.model;

public final class Bird extends Animal{
    public Bird(AnimalId id, String name, int age){
        super(id,name,age);
    }

    @Override
    public AnimalSpecies getSpecies(){
        return AnimalSpecies.BIRD;
    }
}
