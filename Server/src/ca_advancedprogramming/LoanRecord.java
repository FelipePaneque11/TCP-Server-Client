/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca_advancedprogramming;

/**
 * @author Felipe Paneque x23156635
 * 20/10/2025 Dublin Ireland
 * National college of Ireland
 * Advanced programming CA
 */
public class LoanRecord {
    // Basic value object capturing who borrowed which book and when.
    private String borrower, date, title;

    //constructor
    public LoanRecord(String borrower, String date, String title) {
        this.borrower = borrower;
        this.date = date;
        this.title = title;
    }

    //getters
    public String getBorrower() {
        return borrower;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
    
    //setters
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
