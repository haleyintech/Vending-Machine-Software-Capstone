package com.techelevator;

import java.math.BigDecimal;

public class Drink extends Item{
    protected static String type, message;

    public Drink(String position, String name, BigDecimal price) {
        super(position, name, price);
        type = "Drink";
        message = "Glug Glug, Yum!";
        stock = 5;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
