package com.example.cookingapp;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;


public class Ingredient implements Serializable {
    private String description;
    private LocalDate bestBeforeDate;
    private String location;
    private Double amount;
    private String unit;
    private String category;

    public Ingredient(String description, LocalDate bestBeforeDate, String location, Double amount, String unit, String category) {
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    public Ingredient(String description, String location, Double amount, String unit, String category) {
        this.description = description;
        this.bestBeforeDate = null;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    // ArrayAdapter uses toString to display stuff to the list View
    // so we can override the toString to tell Arrayadapter how to display and ingredient object
    public String toString() {
        String date = getStringDate();
        return String.format("%s | %s | %s | %s", description, date, location, category);
    }

    // converts bestBeforeDate to clean formatted string
    public String getStringDate() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(this.bestBeforeDate);
        return date;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBestBeforeDate() {
        return bestBeforeDate;
    }

    public void setBestBeforeDate(LocalDate bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
