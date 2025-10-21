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

public class InvalidCommandException extends Exception{
    // Wraps validation errors so the client receives a descriptive message without crashing the thread.
    public InvalidCommandException(String message){
        super(message);
    }
}
