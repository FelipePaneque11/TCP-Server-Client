/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca_advancedprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;


/**
 * @author Felipe Paneque x23156635
 * 20/10/2025 Dublin Ireland
 * National college of Ireland
 * Advanced programming CA
 */
public class ClientsConnection implements Runnable{
    // Socket dedicated to the connected client.
    private Socket client_link = null;
    // Human readable identifier printed to the server log.
    private final String clientID;
    // Shared in-memory library that persists across all clients.
    private final LibraryStore store;

    public ClientsConnection(Socket connection, String cID, LibraryStore store) {
        this.client_link = connection;
        this.clientID = cID;
        this.store = store;
    }

    @Override
    public void run() {
        try {
            // Configure text-based input/output helpers around the socket streams.
            BufferedReader in = new BufferedReader(new InputStreamReader(client_link.getInputStream())); //Step 3.
            PrintWriter out = new PrintWriter(client_link.getOutputStream(), true); //Step 3.

            String message;
            int n = 0;
            // Read each line sent by the client until the socket closes.
            while ((message = in.readLine()) != null) {
                String raw = message.trim();
                System.out.println("[" + clientID + "] " + raw);

                // STOP -> TERMINATE
                if ("STOP".equalsIgnoreCase(raw)) {
                    out.println("TERMINATE");
                    break;
                }

                try {
                    // Parse the instruction and send the formatted response back to the client.
                    String reply = handleCommand(raw);
                    out.println(reply);
                } catch (InvalidCommandException ice) {
                    out.println("InvalidCommandException: " + ice.getMessage());
                } catch (Exception e) {
                    out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception" + e);
        } finally {
            try {
                System.out.println("\n* TERMINATE connection with " + clientID + " ... *");
                // Ensure the socket is closed so resources are freed on both sides.
                client_link.close();				    //Step 5.
            } catch (IOException e) {
                System.out.println("Unable to disconnect!");
            }
        }
    }

    // this method handles the borrow, return, and list commands sent by clients.
    private String handleCommand(String message) throws InvalidCommandException {
        // Expect "action; borrower; date; title" with semicolons delimiting the fields.
        String[] parts = message.split(";");
        if (parts.length != 4) {
            throw new InvalidCommandException("expected 4 fields: action; borrower; date; title");
        }

        String action = parts[0].trim().toLowerCase();
        String borrower = parts[1].trim();
        String date = parts[2].trim();
        String title = parts[3].trim();

        switch (action) {
            case "borrow": {
                List<String> nowHas = store.borrow(borrower, date, title);
                return formatBorrowerList(borrower, nowHas);
            }
            case "return": {
                List<String> nowHas = store.returnBook(borrower, date, title);
                return formatBorrowerList(borrower, nowHas);
            }
            case "list": {
                List<String> nowHas = store.list(borrower);
                return formatBorrowerList(borrower, nowHas);
            }
            default:
                throw new InvalidCommandException("action must be borrow/return/list");
        }
    }

    // Format the collection of titles in a reader-friendly way for the client terminal.
    private String formatBorrowerList(String borrower, List<String> titles){
        if(titles.isEmpty()){
            return borrower + " currently has: (no books on loan)";
        }
        return borrower + " currently has: " + String.join("; ", titles);
    }
}
