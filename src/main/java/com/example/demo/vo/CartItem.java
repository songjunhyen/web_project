package com.example.demo.vo;

import lombok.Data;

@Data
public class CartItem {
    private int productid;
    private String name; 
    private String color;
    private String size;
    private int count;
    private int price;

    public CartItem(int productid, String name, String color, String size, int count, int price) {
        this.productid = productid;
        this.name = name; 
        this.color = color;
        this.size = size;
        this.count = count;
        this.price = price;
    }
}