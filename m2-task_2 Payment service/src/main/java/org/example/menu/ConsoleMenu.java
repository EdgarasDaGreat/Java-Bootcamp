package org.example.menu;

import org.example.config.AppConfig;
import org.example.model.*;
import org.example.payment.PaymentMethod;
import org.example.payment.PaymentMethodFactory;
import org.example.payment.PaymentProcessor;

import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentProcessor paymentProcessor = new PaymentProcessor();

    private Order currentOrder;

    public void start() {
        AppConfig config = AppConfig.getInstance();
        System.out.println("Welcome to " + config.getApplicationName());

        boolean running = true;
        while (running) {
            printMenu();

            int option = readInt();

            switch (option) {
                case 1 -> createOrder();
                case 2 -> addItem();
                case 3 -> viewOrder();
                case 4 -> payOrder();
                case 5 -> applyDiscount();
                case 0 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void createOrder() {
        System.out.println("Customer name:");
        String customerName = readNonEmpty();
        if (customerName == null) return;

        currentOrder = Order.builder()
                .customerName(customerName)
                .build();
        if (currentOrder == null) return;
        System.out.println("Order created for " + customerName);
    }

    private void addItem() {
        if (currentOrder == null) {
            System.out.println("No order created yet to add items to");
            return;
        }
        if (currentOrder.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Order is cancelled");
            return;
        }
        if (currentOrder.isPaid()) {
            System.out.println("Order is already paid");
            return;
        }

        System.out.println("Item name:");
        String itemName = readNonEmpty();
        if (itemName == null) return;

        System.out.println("Price:");
        double price = readDouble();
        if (price == -1) return;

        System.out.println("Quantity:");
        int quantity = readPositiveInt();
        if (quantity == -1) return;

        currentOrder.addItem(new OrderItem(itemName, price, quantity));
        System.out.println("Item added to order");
    }

    private void viewOrder() {
        if (currentOrder == null) {
            System.out.println("No order created yet to view");
            return;
        }

        System.out.println("Customer: " + currentOrder.getCustomerName());
        System.out.println("Status: " + currentOrder.getStatus());
        System.out.println("Items:");

        for (OrderItem item : currentOrder.getItems()) {
            System.out.println("- " + item);
        }

        System.out.println("Discount: " + currentOrder.getDiscount());
        System.out.println("Total: " + currentOrder.calculateTotal());
    }

    private void payOrder() {
        if (currentOrder == null) {
            System.out.println("No order created yet to pay");
            return;
        }
        if (currentOrder.isPaid()) {
            System.out.println("Order already paid");
            return;
        }
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("Order is empty");
            return;
        }
        if (currentOrder.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Order is cancelled");
            return;
        }

        System.out.println("""
                Select payment method:
                1. Credit Card
                2. PayPal
                3. Gift Card
                4. Apple pay
                """);
        int option = readInt();
        if (option == -1) return;
        if (option < 1 || option > 4) {
            System.out.println("Invalid option");
            return;
        }

        PaymentMethod paymentMethod = switch (option) {
            case 1 -> createCreditCardPayment();
            case 2 -> createPaypalPayment();
            case 3 -> createGiftCardPayment();
            case 4 -> createApplePayPayment();
            default -> throw new IllegalArgumentException("Invalid payment method");
        };

        PaymentResult result = paymentProcessor.process(currentOrder, paymentMethod);
        System.out.println(result.getMessage());
    }

    private void applyDiscount() {
        if (currentOrder == null) {
            System.out.println("No order created yet");
            return;
        }
        if (currentOrder.isPaid()) {
            System.out.println("Order is already paid");
            return;
        }
        System.out.println("""
            Select discount type:
            1. Percentage
            2. Fixed amount
            3. None
            """);
        int option = readInt();
        if (option == -1) return;
        if (option < 1 || option > 3) {
            System.out.println("Invalid option");
            return;
        }
        switch (option) {
            case 1 -> {
                System.out.println("Discount code:");
                String code = readNonEmpty();
                if (code == null) return;

                System.out.println("Percentage:");
                double percentage = readDouble();
                if (percentage == -1) return;

                currentOrder.applyDiscount(new PercentageDiscount(code, percentage));
                System.out.println("Discount applied");
            }
            case 2 -> {
                System.out.println("Discount code:");
                String code = readNonEmpty();
                if (code == null) return;

                System.out.println("Amount:");
                double amount = readDouble();
                if (amount == -1) return;

                currentOrder.applyDiscount(new FixedAmountDiscount(code, amount));
                System.out.println("Discount applied");
            }
            case 3 -> {
                currentOrder.applyDiscount(new NoDiscount());
                System.out.println("Discount removed");
            }
            default -> System.out.println("Invalid option");
        }

    }

    private PaymentMethod createCreditCardPayment() {
        System.out.println("Card number:");
        String cardNumber = readNonEmpty();
        if (cardNumber == null) return null;

        System.out.println("Card holder name:");
        String cardHolderName = readNonEmpty();
        if (cardHolderName == null) return null;

        return PaymentMethodFactory.createCreditCardPayment(cardNumber, cardHolderName);
    }

    private PaymentMethod createPaypalPayment() {
        System.out.println("PayPal email:");
        String email = readNonEmpty();
        if (email == null) return null;

        return PaymentMethodFactory.createPaypalPayment(email);
    }

    private PaymentMethod createGiftCardPayment() {
        System.out.println("Gift card code:");
        String code = readNonEmpty();
        if (code == null) return null;

        System.out.println("Gift card balance:");
        double balance = readDouble();
        if (balance == -1) return null;

        return PaymentMethodFactory.createGiftCardPayment(code, balance);
    }

    private PaymentMethod createApplePayPayment() {
        System.out.println("Apple Pay device account number:");
        String deviceAccountNumber = readNonEmpty();
        if (deviceAccountNumber == null) return null;

        return PaymentMethodFactory.createApplePayPayment(deviceAccountNumber);
    }

    private int readInt() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("q"))
                return -1;
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("That's not a whole number. Please try again. (q to abort)");
            }
        }
    }

    private int readPositiveInt() {
        while (true) {
            int value = readInt();
            if (value == -1) return -1;
            if (value > 0) return value;
            System.out.println("Please enter a positive number. (q to abort)");
        }
    }


    private double readDouble() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("q"))
                return -1;
            try {
                if (Double.parseDouble(input) > 0)
                    return Double.parseDouble(input);
                else
                    System.out.println("Please enter a positive number. (q to abort)");
            } catch (NumberFormatException e) {
                System.out.println("That's not a whole number. Please try again. (q to abort)");
            }
        }
    }

    private String readNonEmpty() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("q"))
                return null;
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field can't be empty. Please try again. (q to abort)");
        }
    }

    private void printMenu() {
        System.out.println("""
                1. Create order
                2. Add item to order
                3. View order
                4. Pay order
                5. Discount selection
                0. Exit
                """);
    }
}
