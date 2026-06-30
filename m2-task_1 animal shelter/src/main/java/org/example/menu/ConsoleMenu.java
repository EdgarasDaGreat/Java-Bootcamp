package org.example.menu;

import org.example.model.*;
import org.example.shelter.Shelter;

import java.util.Scanner;

public class ConsoleMenu {
    private final Shelter<Animal> shelter;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(Shelter<Animal> shelter) {
        this.shelter = shelter;
    }

    public void start() {
        int choice;
        do {
            printMenu();
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("""
                            
                            ====Add animal====
                            """);
                    String species = getSpecies();

                    System.out.println("Enter animal name: ");
                    String name = scanner.nextLine();

                    System.out.println("Enter animal age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter animal id (OPTIONAL): ");
                    String optionalId = scanner.nextLine();

                    while (shelter.isIdTaken(optionalId)) {
                        System.out.println("ID already taken. Please enter a different ID: ");
                        optionalId = scanner.nextLine();
                    }

                    AnimalId animalId;
                    if (!optionalId.isEmpty())
                        animalId = new AnimalId(optionalId);
                    else
                        animalId = new AnimalId();

                    if (species.equals("Bird")) {
                        Bird bird = new Bird(animalId, name, age);
                        shelter.addAnimal(bird);
                    } else if (species.equals("Cat")) {
                        Cat cat = new Cat(animalId, name, age);
                        shelter.addAnimal(cat);
                    } else {
                        Dog dog = new Dog(animalId, name, age);
                        shelter.addAnimal(dog);
                    }
                    System.out.println("""
                            
                            ====Animal added successfully!====
                            """);
                    break;
                case 2:
                    System.out.println("""
                            
                            ====List all animals====
                            """);
                    shelter.getAllAnimals().forEach(System.out::println);
                    break;
                case 3:
                    System.out.println("""
                            
                            ====Find animals by species====
                            """);
                    String speciesToFind = getSpecies();
                    System.out.println("Animals of species " + speciesToFind + ":");
                    shelter.findBySpecies(speciesToFind).forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("""
                            
                            ====List available animals====
                            """);
                    System.out.println("Available animals:");
                    shelter.findAvailableAnimals().forEach(System.out::println);
                    break;
                case 5:
                    System.out.println("""
                            
                            ====Mark animal as adopted====
                            """);
                    System.out.println("Enter animal ID: ");
                    String id = scanner.nextLine();
                    shelter.markAsAdopted(id);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }


        } while (choice != 0);

    }

    private String getSpecies() {
        while (true) {
            System.out.println("""
                    Select animal species:
                    1. Bird
                    2. Cat
                    3. Dog
                    """);
            System.out.println("Enter your choice: ");
            int speciesChoice = scanner.nextInt();
            scanner.nextLine();
            switch (speciesChoice) {
                case 1:
                    return "Bird";
                case 2:
                    return "Cat";
                case 3:
                    return "Dog";
            }
        }

    }


    private void printMenu() {
        System.out.println("""
                
                1. Add animal
                2. List all animals
                3. Find animals by species
                4. List available animals
                5. Mark animal as adopted
                0. Exit
                """);
    }
}
