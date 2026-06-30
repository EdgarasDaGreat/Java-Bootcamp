package org.example.model;

public final class Cat extends Animal{
    public Cat(AnimalId id, String name, int age){
        super(id,name,age);
    }

    @Override
    public AnimalSpecies getSpecies(){
        return AnimalSpecies.CAT;
    }
}
