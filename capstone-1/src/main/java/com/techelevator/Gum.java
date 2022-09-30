package com.techelevator;

import java.math.BigDecimal;

public class Gum extends Item{
    protected static String type, message;

    public Gum(String position, String name, BigDecimal price) {
        super(position, name, price);
        type = "Gum";
        message = "Chew Chew, Yum!";
        stock = 5;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
