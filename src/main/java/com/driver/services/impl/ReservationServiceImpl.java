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

       List<Spot> spotList = parkingLot.getSpotList();

        if(user == null || parkingLot == null || spotList == null)
        {
            throw  new Exception("Cannot make reservation");
        }

      SpotType requiredSpotType;
        if(numberOfWheels == 2)
        {
            requiredSpotType = SpotType.TWO_WHEELER;
        }
        else if(numberOfWheels == 4)
        {
            requiredSpotType = SpotType.FOUR_WHEELER;
        }
        else {
            requiredSpotType = SpotType.OTHERS;
        }
        Spot selectedSpot = null;
        int minPrice = Integer.MAX_VALUE;
            for (Spot spot : spotList) {
            if (!spot.getOccupied()) {
                if(spot.getSpotType() == SpotType.OTHERS) {
                    int parkingPrice = spot.getPricePerHour()*timeInHours;
                    if(parkingPrice < minPrice) {
                        minPrice = parkingPrice;
                        selectedSpot = spot;
                    }
                }
                if(spot.getSpotType() == SpotType.FOUR_WHEELER && numberOfWheels <=4) {
                    int parkingPrice = spot.getPricePerHour()*timeInHours;
                    if(parkingPrice < minPrice) {
                        minPrice = parkingPrice;
                        selectedSpot = spot;
                    }
                }
                if (spot.getSpotType() == SpotType.TWO_WHEELER && numberOfWheels <= 2) {
                    int parkingPrice = spot.getPricePerHour()*timeInHours;
                    if(parkingPrice < minPrice) {
                        minPrice = parkingPrice;
                        selectedSpot = spot;
                    }
                }
            }
        }

        if(selectedSpot == null) throw new Exception("Cannot make reservation");





       Reservation reservation = new Reservation(timeInHours,selectedSpot,user);
//       reservation.setNumberOfHours(timeInHours);
//       reservation.setSpot(spot);
//       reservation.setUser(user);
        selectedSpot.setOccupied(true);

       selectedSpot.getReservationList().add(reservation);
       user.getReservationList().add(reservation);

       spotRepository3.save(selectedSpot);
       userRepository3.save(user);

       return null;

    }
}
