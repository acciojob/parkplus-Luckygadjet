package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int pricePerHour = reservation.getSpot().getPricePerHour();
        int totalparkingHours = reservation.getNumberOfHours();
        int totalAmount = pricePerHour * totalparkingHours;

        if(amountSent < totalAmount)
        {
            throw  new Exception("Insufficient Amount");
        }

        String paymentMode = mode.toUpperCase();

        List<String> paymentmodes = new ArrayList<>();
        paymentmodes.add("CASH");
        paymentmodes.add("CARD");
        paymentmodes.add("UPI");

        if(paymentmodes.contains(paymentMode) == false)
        {
            throw  new Exception("Payment mode not detected");
        }

        Payment payment = reservation.getPayment();
        if(paymentMode == "CARD")
        {
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else if(paymentMode == "CASH")
        {
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else {
            payment.setPaymentMode(PaymentMode.UPI);
        }

        payment.setPaymentCompleted(Boolean.TRUE);
        payment.setReservation(reservation);

        reservationRepository2.save(reservation);
        paymentRepository2.save(payment);

        return payment;


    }
}
