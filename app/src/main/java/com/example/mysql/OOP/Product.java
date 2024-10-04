package com.example.mysql.OOP;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class Product implements Serializable
{
    public String id, title, image, introduction, genre, dateTime;
    public double money;

    public int quantity;
    public String shopId, shopImage, shopName, phoneShop, cityShop, soldShop, id_rep_comment;
    public String userId, productId, nameUser, imageUser,  comment, titleComment, commentTime, star, commentId;

}
