package com.techelevator;

import java.math.BigDecimal;

public abstract class Item {
    protected String name, position;
    protected String type, message;
    protected int stock;
    protected BigDecimal price;

    public Item(String position, String name, BigDecimal price) {
        stock = 5;
        this.name = name;
        this.price = price;
        this.position = position;
    }

    public Item() {
        stock = 5;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public int getStock() {
        return stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getMessage() {
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void decrementStock() {
        stock--;
    }

}
