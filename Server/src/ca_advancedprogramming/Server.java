/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca_advancedprogramming;

import java.io.*;
import java.net.*;

/**
 * @author Felipe Paneque x23156635
 * 20/10/2025 Dublin Ireland
 * National college of Ireland
 * Advanced programming CA
 */
public class Server {

    private static ServerSocket servSock;
    private static final int PORT = 1234;
    private static int clientConnections = 0;
    
    //open the library in the server
    static final LibraryStore Store = new LibraryStore();

    public static void main(String[] args) {
        System.out.println("Opening port...\n");
        try {
            servSock = new ServerSocket(PORT);      //Step 1.
        } catch (IOException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }

        while(true){
            run();
        }
    }

    public static void run() {                     //Step 2.
        try {            
                Socket link = servSock.accept();               //Step 2.
                clientConnections++;
                String clientID = "Client " + clientConnections;
                System.out.println("Connected: " + clientID + "from " + link.getRemoteSocketAddress());
                
                Thread t = new Thread(new ClientsConnection(link, clientID, Store));
                t.start();
        } catch (IOException e) {
            System.out.println("IO Exception: " + e);
        }
    } // finish run method 
} // finish the class
