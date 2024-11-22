package com.xcelerate.cafeManagementSystem.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.xcelerate.cafeManagementSystem.DTOs.StripeResponse;
import com.xcelerate.cafeManagementSystem.Model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    PaymentService paymentMethod;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${frontendURL}")
    private String frontendURL;

    public StripeResponse createPaymentIntent(Order order) {
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder().addPaymentMethodType(
                SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendURL + "/checkout/success")
                .setCancelUrl(frontendURL + "/checkout/cancel");

        /*  ADD PRODUCTS AS LINE ITEMS */
        for (int i = 0; i < 10; i++) {
                paramsBuilder.addLineItem(SessionCreateParams.LineItem.builder()
                    .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount((long) 100)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Product Name")
                                    .build())
                            .build()
                    )
                    .build()
                );
        }
        SessionCreateParams params = paramsBuilder.build();

        try {
            Session session =  Session.create(params);
            StripeResponse res = new StripeResponse();
            res.setPaymentURL(session.getUrl());
            return null;
        }
        catch (StripeException e) {
            System.out.println("Stripe exception occured during, payment by card.");
            return null;

        }

    }


}
