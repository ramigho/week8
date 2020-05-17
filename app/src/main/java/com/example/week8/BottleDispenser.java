package com.example.week8;

import java.util.ArrayList;

public class BottleDispenser {

    private String lastBottle;
    private double lastPrice;
    private double lastSize;

    private int bottles;
    protected double money;
    ArrayList<Bottle> bottleList = new ArrayList<Bottle>();

    private BottleDispenser() {
        bottles = 5;
        money = 0;
        for (int i = 0; i < bottles; i++) {
            Bottle unit = new Bottle(i);
            bottleList.add(unit);
        }
    }
    // Singleton-pattern
    private static BottleDispenser disp = null;

    public static BottleDispenser getInstance() {
        if (disp == null) {
            disp = new BottleDispenser();
        }
        return disp;
    }

    public void addMoney(double moneyAdded) {
        money = money + moneyAdded;
    }

    public void emptyMoney() {
        money = 0;
    }

    public int getAvailability(int choice, int volume){
        String name;
        double size;

        if (choice == 0){
            name = "Pepsi Max";
        } else if (choice == 1){
            name = "Coca-Cola Zero";
        } else {
            name = "Fanta Zero";
        }
        if (volume == 0){
            size = 0.5;
        } else {
            size = 1.5;
        }

        int retIndex = -1;
        for (int i=0; i<bottleList.size(); i++){
            if (bottleList.get(i).getName() == name && bottleList.get(i).getSize() == size) {
                retIndex = i;
                lastBottle = bottleList.get(retIndex).getName();
                lastPrice = bottleList.get(retIndex).getPrize();
                lastSize = bottleList.get(retIndex).getSize();
            }
        }
        return retIndex;
    }

    public boolean checkMoney(int i){
        if (money >= bottleList.get(i).getPrize()){
            return true;
        } else {
            return false;
        }
    }
    public void purchase(int i){
        money = money - bottleList.get(i).getPrize();
        bottleList.remove(i);
    }

    public String getLastBottle(){
        return lastBottle;
    }

    public double getLastPrice(){
        return lastPrice;
    }

    public double getLastSize(){
        return lastSize;
    }

}
