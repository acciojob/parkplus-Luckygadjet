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

        mode = mode.toUpperCase();
        PaymentMode givenpaymentmode;

       if(mode.equals("CARD"))
       {
           givenpaymentmode = PaymentMode.CARD;
       }
       else if(mode.equals("CASH"))
       {
           givenpaymentmode = PaymentMode.CASH;
       }
       else if(mode.equals("UPI")) {
           givenpaymentmode = PaymentMode.UPI;
       }
       else {
           throw  new Exception("Payment mode not detected");
       }

        int resBill = reservation.getSpot().getPricePerHour() * reservation.getNumberOfHours();

       Payment p = new Payment(true,givenpaymentmode,reservation);

       if(amountSent < resBill)
       {
           throw  new Exception("Insufficient Amount");
       }


        reservation.setPayment(p);
        reservationRepository2.save(reservation);
        //paymentRepository2.save(payment);

        return p;


    }
}
