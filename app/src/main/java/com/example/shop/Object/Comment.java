package com.example.shop.Object;

import java.io.Serializable;

public class Comment implements Serializable {
    public Product product;
    public Shop shop;
    public User user;
    public String id;
    public String comment;
    public double rating;
    public String date;

    public Comment(Product product, Shop shop, User user, String id, String message, double rating, String date) {
        this.product = product;
        this.shop = shop;
        this.user = user;
        this.id = id;
        this.comment = message;
        this.date = date;
        this.rating = rating;
    }

    public Comment()
    {
        shop = new Shop();
        product = new Product();
        user = new User();
    }

    public Shop getShop()
    {
        return shop;
    }

    public Product getProduct()
    {
        return product;
    }

    public User getUser()
    {
        return user;
    }
}
