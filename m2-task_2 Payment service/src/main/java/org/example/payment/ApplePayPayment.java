package org.example.payment;

import org.example.model.PaymentResult;

public class ApplePayPayment extends PaymentMethod {
    private final String deviceAccountNumber;

    public ApplePayPayment(String deviceAccountNumber) {
        super("ApplePay");                          // feeds the base class providerName
        this.deviceAccountNumber = deviceAccountNumber;
    }

    @Override
    protected PaymentResult processPayment(double amount) {
        if (deviceAccountNumber.length() < 4)
            return new PaymentResult(false, "Invalid Apple Pay device");
        return new PaymentResult(true, "Paid " + amount + " using Apple Pay");
    }
}