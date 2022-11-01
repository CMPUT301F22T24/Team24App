package com.example.cookingapp;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Comparator;

/**
 * The Ingredient object class
 */
public class Ingredient implements Serializable {
    private String description;
    private LocalDate bestBeforeDate;
    private String location;
    private Double amount;
    private String unit;
    private String category;

    @DocumentId
    private String documentId;

    public Ingredient() {
        this.description = null;
        this.bestBeforeDate = null;
        this.location = null;
        this.amount = null;
        this.unit = null;
        this.category = null;
    }

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
    // so we can override the toString to tell ArrayAdapter how to display and ingredient object
    public String toString() {
        String date = getStringDate();
        return String.format("%s | %s | %s | %s", description, date, location, category);
    }

    // converts bestBeforeDate to clean formatted string
    public String getStringDate() {
        // TODO: handle better when date is null
        String pattern = "yyyy-MM-dd";
        if (this.bestBeforeDate != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(this.bestBeforeDate);
            return date;
        }
        return pattern;
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

    @Exclude
    public void setBestBeforeDate(LocalDate bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public void setBestBeforeDate(Timestamp bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate();
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

    public String getDocumentId() { return documentId; }

    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getUnit() {
        return this.unit;
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

    public static Comparator<Ingredient> IngredientDescriptionComparator = new Comparator<Ingredient>() {
        @Override
        public int compare(Ingredient i1, Ingredient i2) {
            String d1 = i1.getDescription();
            String d2 = i2.getDescription();
            // desc order
            return d1.compareTo(d2);
        }
    };

    public static Comparator<Ingredient> IngredientBestBeforeDateComparator = new Comparator<Ingredient>() {
        @Override
        public int compare(Ingredient i1, Ingredient i2) {
            LocalDate d1 = i1.getBestBeforeDate();
            LocalDate d2 = i2.getBestBeforeDate();
            // desc order
            return d1.compareTo(d2);
        }
    };

    public static Comparator<Ingredient> IngredientLocationComparator = new Comparator<Ingredient>() {
        @Override
        public int compare(Ingredient i1, Ingredient i2) {
            String d1 = i1.getLocation();
            String d2 = i2.getLocation();
            // desc order
            return d1.compareTo(d2);
        }
    };

    public static Comparator<Ingredient> IngredientCategoryComparator = new Comparator<Ingredient>() {
        @Override
        public int compare(Ingredient i1, Ingredient i2) {
            String d1 = i1.getCategory();
            String d2 = i2.getCategory();
            // desc order
            return d1.compareTo(d2);
        }
    };
}

