package com.techelevator;

import java.math.BigDecimal;

public class Candy extends Item{
    protected static String type, message;

    public Candy(String position, String name, BigDecimal price) {
        super(position, name, price);
        type = "Candy";
        message = "Munch Munch, Yum!";
        stock = 5;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

}
