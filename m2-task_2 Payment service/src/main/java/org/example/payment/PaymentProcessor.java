package org.example.payment;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.PaymentResult;

public class PaymentProcessor {
    public PaymentResult process(Order order, PaymentMethod paymentMethod){
        if (paymentMethod == null)
            return new PaymentResult(false,"No payment method selected.");
        if (order == null)
            return new PaymentResult(false,"No order is selected.");
        if (order.isPaid())
            return new PaymentResult(false,"Order is already paid.");
        if (order.getStatus() == OrderStatus.CANCELLED)
            return new PaymentResult(false,"Order is cancelled.");
        if (order.getItems().isEmpty())
            return new PaymentResult(false,"Order is empty.");

        PaymentResult result = paymentMethod.pay(order.calculateTotal());

        if(result.isSuccessful()){
            order.markAsPaid();
        }

        return result;
    }
}
