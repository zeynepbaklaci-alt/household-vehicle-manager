package com.example.household_app.ui.vehicle;

public class VehicleDto {


    private String id;
    private String plate;
    private String brand;
    private String model;
    private String fuelType;
    private Integer initialOdometer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Integer getInitialOdometer() {
        return initialOdometer;
    }

    public void setInitialOdometer(Integer initialOdometer) {
        this.initialOdometer = initialOdometer;
    }
}