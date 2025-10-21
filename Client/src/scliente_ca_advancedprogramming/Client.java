package scliente_ca_advancedprogramming;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */



import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Felipe Paneque x23156635
 * 20/10/2025 Dublin Ireland
 * National college of Ireland
 * Advanced programming CA
 */
public class Client {
    private static final int PORT = 1234;
    private static InetAddress host;
    private static final String importUrl =  "https://gist.githubusercontent.com/FelipePaneque11/1bc0479cb3331ebcf8bae9bc5ec715f6/raw/de71d64f20ef4e1a70f0764ce10cb4b723e40d1d/loans.txt";
    
    public static void main(String[] args) {
        try {
            host = InetAddress.getLocalHost();
        } catch (IOException e) {
            System.out.println("Unable to get local host");
            System.exit(1);
        }  
            run();
    }

    private static void run() {
        try (
            Socket socket = new Socket(host, PORT);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter( new OutputStreamWriter(socket.getOutputStream()),true) // autoFlush on println()            
        ){
            
            //auto-import from the loan.txt when client starts
            if(importUrl != null && !importUrl.isEmpty()){
                System.out.println("Importing loans from: " + importUrl);
                doHttpImport(importUrl, out, in);
            }
            
            System.out.println("Connected. Type messages (STOP to quit):");
            String line;
            while ((line = keyboard.readLine()) != null) {
                String trimmed = line.trim();
                
                //manual import still works if necessary
                if(trimmed.toLowerCase().startsWith("import;")){
                    String[] parts = trimmed.split(";", 2);
                    if(parts.length < 2 || parts[1].trim().isEmpty()){
                        System.out.println("Client> import requires a public URL(Raw).");
                        continue;
                    }
                    String url = parts[1].trim();
                    doHttpImport(url,out,in);
                    continue;
                }
                
                
                out.println(line); // send
                if ("STOP".equalsIgnoreCase(line)) { // match the prompt text
                    String reply = in.readLine();
                    System.out.println("SERVER> " + reply);
                    break;
                }
                String reply = in.readLine(); // receive
                if (reply == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }
                System.out.println("SERVER> " + reply);
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
    
    private static void doHttpImport(String urlString, PrintWriter out, BufferedReader in) {
        System.out.println("Client> Importing from: " + urlString);
        int imported = 0;
        int skipped = 0;

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                String currentBorrower = null;
                int lineNo = 0;

                while ((line = r.readLine()) != null) {
                    lineNo++;
                    String raw = line.trim();

                    // ignore empty lines and comments
                    if (raw.isEmpty() || raw.startsWith("#") || raw.startsWith("//")) {
                        continue;
                    }

                     // detect borrower section
                    if (raw.toLowerCase().startsWith("borrower=")) {
                        currentBorrower = raw.substring("borrower=".length()).trim();
                        if (currentBorrower.isEmpty()) {
                            System.out.println("Client> Skipped line " + lineNo + ": empty borrower");
                            skipped++;
                            currentBorrower = null; // invalida seção até aparecer um borrower válido
                        }
                        continue; // vai para a próxima linha
                    }

                    // a partir daqui, esperamos "date; title"
                    if (currentBorrower == null) {
                        System.out.println("Client> Skipped line " + lineNo + ": missing borrower=<Name> section above");
                        skipped++;
                        continue;
                    }

                    String[] parts = raw.split(";");
                    if (parts.length != 2) {
                        System.out.println("Client> Skipped line " + lineNo + ": expected 'date; book title'");
                        skipped++;
                        continue;
                    }

                    String date = parts[0].trim();
                    String title = parts[1].trim();
                    if (date.isEmpty() || title.isEmpty()) {
                        System.out.println("Client> Skipped line " + lineNo + ": empty date/title");
                        skipped++;
                        continue;
                    }

                    // envia para o servidor no formato da Versão B
                    String msg = "borrow; " + currentBorrower + "; " + date + "; " + title;
                    out.println(msg);

                    String reply = in.readLine();
                    if (reply == null) {
                        System.out.println("Client> Server closed the connection during import.");
                        break;
                    }
                    System.out.println("SERVER> " + reply);
                    imported++;
                }
            }

            System.out.println("Client> Imported: " + imported + "; Skipped: " + skipped);

        } catch (Exception e) {
            System.out.println("Client> HTTP import error: " + e.getMessage());
        }
    }

}
