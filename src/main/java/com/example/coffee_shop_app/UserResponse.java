package com.example.coffee_shop_app;

public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private double points;
    private int freeDrinks;
    private int loyaltyCount;

    public UserResponse(User user) {
        this.id           = user.getId();
        this.firstName    = user.getFirstName();
        this.lastName     = user.getLastName();
        this.points       = user.getPoints();
        this.freeDrinks   = user.getFreeDrinks();
        this.loyaltyCount = user.getLoyaltyCount();
    }

    // геттеры
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public double getPoints()    { return points; }
    public int getFreeDrinks()   { return freeDrinks; }
    public int getLoyaltyCount() { return loyaltyCount; }
}
