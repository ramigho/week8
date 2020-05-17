package com.example.week8;

public class Bottle {
    private String name;
    private String manufacturer;
    private double total_energy;
    private double size;
    private double prize;

    public Bottle(int i){
        if (i == 0) {
            name = "Pepsi Max";
            manufacturer = "Pepsi";
            total_energy =  0.3;
            size = 0.5;
            prize = 1.8;
        } else if (i == 1) {
            name = "Pepsi Max";
            manufacturer = "Pepsi";
            total_energy =  0.3;
            size = 1.5;
            prize = 2.2;
        } else if (i == 2) {
            name = "Coca-Cola Zero";
            manufacturer = "Coca-Cola";
            total_energy =  0.3;
            size = 0.5;
            prize = 2.0;
        } else if (i == 3) {
            name = "Coca-Cola Zero";
            manufacturer = "Coca-Cola";
            total_energy =  0.3;
            size = 1.5;
            prize = 2.5;
        } else if (i == 4) {
            name = "Fanta Zero";
            manufacturer = "Fanta";
            total_energy =  0.3;
            size = 0.5;
            prize = 1.95;
        }
    }


    public String getName(){
        return name;
    }

    public String getManufacturer(){
        return manufacturer;
    }

    public double getEnergy(){
        return total_energy;
    }

    public double getSize() {
        return size;
    }

    public double getPrize() {
        return prize;
    }

}