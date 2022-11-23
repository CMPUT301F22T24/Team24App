package com.example.cookingapp;

import java.io.Serializable;
import java.time.LocalDate;

public class ShoppingList implements Serializable {
    private String description;
    private LocalDate bestBeforeDate;
    private String location;
    private Double amount;
    private String unit;
    private String category;
    private Boolean isChecked;



    public ShoppingList() {
        this.description = null;
        this.bestBeforeDate = null;
        this.location = null;
        this.amount = null;
        this.unit = null;
        this.category = null;
        this.isChecked = null;
    }

    public ShoppingList(String description, LocalDate bestBeforeDate, String location, Double amount, String unit, String category,Boolean isChecked) {
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
        this.isChecked = isChecked;

    }

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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}//ShoppingList
