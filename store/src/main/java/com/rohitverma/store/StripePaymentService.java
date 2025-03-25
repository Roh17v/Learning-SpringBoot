package com.rohitverma.store;

public class StripePaymentService implements PaymentService {
    @Override
    public void processPayment(double amount)
    {
        System.out.println("STRIPE PAYMENT");
        System.out.println("Amount: " + amount);
    }
}
