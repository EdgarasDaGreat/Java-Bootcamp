package org.example.menu;

public record MenuOption(int number,String label) {
    @Override
    public String toString() {
        return number + ". " + label;
    }
}
