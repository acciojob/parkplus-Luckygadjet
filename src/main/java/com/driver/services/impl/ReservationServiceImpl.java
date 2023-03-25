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

        try {
            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new Exception("Cannot make reservation");
            }

            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();


//        Getting the spot list from parkingLot
            List<Spot> spotList = parkingLot.getSpotList();
            if (spotList.size() == 0) throw new Exception("Cannot make reservation");


            SpotType requiredSpotType;

//        Getting required spot type
            if (numberOfWheels > 4) requiredSpotType = SpotType.OTHERS;
            else if (numberOfWheels > 2) requiredSpotType = SpotType.TWO_WHEELER;
            else requiredSpotType = SpotType.TWO_WHEELER;

            int minPrice = Integer.MAX_VALUE;
            Spot selectedSpot = null;

//        Finding the spot which satisfy the condition
//        1. total price should be minimum
//        2. Vehicle can be spot in equal and greater type

            for (Spot spot : spotList) {
                if (!spot.getOccupied()) {
                    if (spot.getSpotType() == SpotType.OTHERS) {
                        int parkingPrice = spot.getPricePerHour() * timeInHours;
                        if (parkingPrice < minPrice) {
                            minPrice = parkingPrice;
                            selectedSpot = spot;
                        }
                    }
                    if (spot.getSpotType() == SpotType.FOUR_WHEELER && numberOfWheels <= 4) {
                        int parkingPrice = spot.getPricePerHour() * timeInHours;
                        if (parkingPrice < minPrice) {
                            minPrice = parkingPrice;
                            selectedSpot = spot;
                        }
                    }
                    if (spot.getSpotType() == SpotType.TWO_WHEELER && numberOfWheels <= 2) {
                        int parkingPrice = spot.getPricePerHour() * timeInHours;
                        if (parkingPrice < minPrice) {
                            minPrice = parkingPrice;
                            selectedSpot = spot;
                        }
                    }
                }
            }

            if (selectedSpot == null) throw new Exception("Cannot make reservation");

            Reservation reservation = new Reservation(timeInHours, selectedSpot, user);
            selectedSpot.setOccupied(Boolean.TRUE);
            selectedSpot.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(selectedSpot);

            return reservation;
        }
        catch (Exception e)
        {
            return null;
        }

    }
}
