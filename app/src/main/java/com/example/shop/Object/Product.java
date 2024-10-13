package com.example.shop.Object;

import java.io.Serializable;

public class Product implements Serializable {
    public String id;
    public String title;
    public String category;
    public String image_one, image_two, image_three, image_four;
    public String introduce;
    public String posting_date;
    public int sold;
    public int quantity;
    public double money;
    public String favorite_love_date;
    public Shop shop;

    public Product()
    {
        shop = new Shop();
    }

    public Product(String id, String title, String category, String image_one, String image_two, String image_three, String image_four, String introduce, int sold, int quantity, double money, Shop shop, String posting_date) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.image_one = image_one;
        this.image_two = image_two;
        this.image_three = image_three;
        this.image_four = image_four;
        this.introduce = introduce;
        this.sold = sold;
        this.quantity = quantity;
        this.money = money;
        this.shop = shop;
        this.posting_date = posting_date;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getImage_one() {
        return image_one;
    }

    public String getImage_two()     {
        return image_two;
    }

    public String getImage_three() {
        return image_three;
    }

    public String getImage_four() {
        return image_four;
    }

    public String getIntroduce() {
        return introduce;
    }

    public int getSold() {
        return sold;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getMoney() {
        return money;
    }

    public Shop getShop() {
        return shop;
    }
    public String getPosting_date() {
        return posting_date;
    }
}
