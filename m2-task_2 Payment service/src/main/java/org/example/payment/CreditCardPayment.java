package org.example.payment;

import org.example.model.Order;
import org.example.model.PaymentResult;

public class CreditCardPayment extends PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        super("CreditCard");
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public PaymentResult processPayment(double amount) {
        if (cardNumber.length() < 5 || !cardNumber.matches("\\d+"))
            return new PaymentResult(false, "Invalid card number");
        if (cardHolderName.isEmpty())
            return new PaymentResult(false, "Card holder name is required");
        return new PaymentResult(true, "Paid " + amount + " using credit card ending with " + cardNumber.substring(cardNumber.length() - 4));
    }
}
