package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {


       User  user = userRepository3.findById(userId).get();
       ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

       Spot spot = null;
       int amount = Integer.MAX_VALUE;

       List<Spot> availablespots = parkingLot.getSpotList();

//       StringBuilder str = new StringBuilder();
//        //TWO_WHEELER, FOUR_WHEELER, OTHERS
//        int wheels = 0;
//       if(numberOfWheels == 2)
//       {
//           String s = "TWO_WHEELER";
//           wheels = 2;
//           str.append(s);
//       }
//       else if(numberOfWheels == 4)
//       {
//           String s = "FOUR_WHEELER";
//           wheels = 4;
//           str.append(s);
//       }
//       else {
//           String s = "OTHERS";
//           wheels = 5;
//           str.append(s);
//       }
//       String spottype = str.substring(0,str.length());



       for(Spot s : availablespots)
       {
           int wh = 0;
           if(s.getSpotType().equals("TWO_WHEELER"))
           {
               wh = 2;
           }
           else if(s.getSpotType().equals("FOUR_WHEELER"))
           {
               wh = 4;
           }
           else {
               wh = Integer.MAX_VALUE;
           }

           if(s.getPricePerHour() < amount && wh >= numberOfWheels)
           {
               spot = s;
           }
       }

       if(user == null || parkingLot == null || spot == null)
       {
           throw  new Exception("Cannot make reservation");
       }

       Reservation reservation = new Reservation();
       reservation.setNumberOfHours(timeInHours);
       reservation.setSpot(spot);
       reservation.setUser(user);

       spot.getReservationList().add(reservation);
       user.getReservationList().add(reservation);

       spotRepository3.save(spot);
       userRepository3.save(user);

       return reservation;

    }
}
