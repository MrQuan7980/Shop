package com.example.shop.Object;

import java.io.Serializable;

public class Shop implements Serializable {
    public String id; // ID của shop
    public String name; // Tên shop
    public String phone; // Số điện thoại shop
    public String email; // Email shop
    public String image; // Hình ảnh shop
    public String house; // Số nhà
    public String district; // Quận
    public String city; // Thành phố

    public Shop() {
        // Constructor rỗng
    }

    public Shop(String id, String name, String phone, String email, String image, String house, String district, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.house = house;
        this.district = district;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getHouse() {
        return house;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }
}
