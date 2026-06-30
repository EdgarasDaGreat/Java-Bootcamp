package org.example.menu;

import org.example.model.*;
import org.example.shelter.AdoptionData;
import org.example.shelter.Shelter;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final Shelter<Animal> shelter;
    private final Scanner scanner = new Scanner(System.in);
    List<MenuOption> menuOptions = List.of(
            new MenuOption(1, "Add animal"),
            new MenuOption(2, "List all animals"),
            new MenuOption(3, "Find animals by species"),
            new MenuOption(4, "List available animals"),
            new MenuOption(5, "Mark animal as adopted"),
            new MenuOption(6, "History of adoptions"),
            new MenuOption(0, "Exit")
    );

    private void printMenu() {
        System.out.println("Animal Shelter Menu:");
        menuOptions.forEach(option -> System.out.println(option.toString()));
    }

    public ConsoleMenu(Shelter<Animal> shelter) {
        this.shelter = shelter;
    }

    public void start() {
        int choice;
        do {
            printMenu();
            System.out.println("Enter your choice: ");
            choice = readInt();
            switch (choice) {
                case 1:
                    System.out.println("""
                            ====Add animal====
                            """);
                    AnimalSpecies species = getSpecies();

                    System.out.println("Enter animal name: ");
                    String name = readNonEmpty();

                    System.out.println("Enter animal age: ");
                    int age = readInt();

                    String optionalId;
                    do {
                        System.out.println("Enter animal id (OPTIONAL): ");
                        optionalId = scanner.nextLine().trim();
                        if (shelter.isIdTaken(optionalId)) {
                            System.out.println("ID already taken. Please enter a different ID: ");
                        } else break;
                    } while (shelter.isIdTaken(optionalId));

                    AnimalId animalId;
                    if (!optionalId.isEmpty())
                        animalId = new AnimalId(optionalId);
                    else
                        animalId = new AnimalId();

                    Animal animal = AnimalFactory.createAnimal(species, name, age, animalId);
                    shelter.addAnimal(animal);

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
                    AnimalSpecies speciesToFind = getSpecies();
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
                    String toBeAdoptedId;
                    while (true) {
                        System.out.println("Enter animal id: ");
                        toBeAdoptedId = readNonEmpty();
                        if (!shelter.isIdTaken(toBeAdoptedId)) {
                            System.out.println("No animal with that id found. Please enter a valid id: ");
                        } else break;
                    }
                    System.out.println("Enter adopter name: ");
                    String adopterName = readNonEmpty();
                    shelter.markAsAdopted(toBeAdoptedId, adopterName);
                    System.out.println("""
                            ====Animal has been marked as adopted!====
                            """);
                    break;
                case 6:
                    System.out.println("""
                            ====History of adoptions====
                            """);
                    List<AdoptionData> adoptionHistory = shelter.getAdoptedAnimals();
                    if (adoptionHistory.isEmpty()) {
                        System.out.println("No animals have been adopted yet.");
                    }
                    else
                        adoptionHistory.forEach(System.out::println);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }


        } while (choice != 0);

    }

    private AnimalSpecies getSpecies() {
        List<MenuOption> speciesOptions = List.of(
                new MenuOption(1, "Bird"),
                new MenuOption(2, "Cat"),
                new MenuOption(3, "Dog"),
                new MenuOption(4, "Hamster")
        );
        while (true) {
            speciesOptions.forEach(option -> System.out.println(option.toString()));
            System.out.println("Enter your choice: ");
            int speciesChoice = readInt();
            switch (speciesChoice) {
                case 1:
                    return AnimalSpecies.BIRD;
                case 2:
                    return AnimalSpecies.CAT;
                case 3:
                    return AnimalSpecies.DOG;
                case 4:
                    return AnimalSpecies.HAMSTER;
            }
        }

    }

    private int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                if (Integer.parseInt(input) >= 0)
                    return Integer.parseInt(input);
                else
                    System.out.println("Please enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("That's not a whole number. Please try again.");
            }
        }
    }

    private String readNonEmpty() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field can't be empty. Please try again.");
        }
    }


}
