package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "spot")
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private SpotType SpotType;

    private int PricePerHour;

    private boolean Occupied;

    @OneToMany
    @JoinColumn
    private ParkingLot parkingLot;

    @OneToMany(mappedBy = "spot",cascade = CascadeType.ALL)
    List<Reservation> reservationList = new ArrayList<>();


    public Spot(){}

    public Spot(int id, com.driver.model.SpotType spotType, int pricePerHour, boolean occupied, ParkingLot parkingLot, List<Reservation> reservationList) {
        this.id = id;
        SpotType = spotType;
        PricePerHour = pricePerHour;
        Occupied = occupied;
        this.parkingLot = parkingLot;
        this.reservationList = reservationList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public com.driver.model.SpotType getSpotType() {
        return SpotType;
    }

    public void setSpotType(com.driver.model.SpotType spotType) {
        SpotType = spotType;
    }

    public int getPricePerHour() {
        return PricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        PricePerHour = pricePerHour;
    }

    public boolean isOccupied() {
        return Occupied;
    }

    public void setOccupied(boolean occupied) {
        Occupied = occupied;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}
