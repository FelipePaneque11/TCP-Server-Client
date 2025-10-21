/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca_advancedprogramming;

import java.util.*;
/**
 * @author Felipe Paneque x23156635
 * 20/10/2025 Dublin Ireland
 * National college of Ireland
 * Advanced programming CA
 */
public class LibraryStore {
    private final Map<String, LinkedHashMap<String,LoanRecord>> loansByBorrower = new HashMap<>();
    
//  normalize keys (case-sensitive & trim)
    private String norm(String s) { return s == null ? "" : s.trim(); }
    
    //add loans
    public synchronized List<String> borrow(String borrower, String date, String title) throws InvalidCommandException {
        borrower = norm(borrower);
        date = norm(date);
        title = norm(title);
        
        if(borrower.isEmpty() || date.isEmpty() || title.isEmpty()){
            throw new InvalidCommandException("borrow requires borrower, date, and title");
        }
        
        //
        loansByBorrower.putIfAbsent(borrower, new LinkedHashMap<>());
        LinkedHashMap<String, LoanRecord> shelf = loansByBorrower.get(borrower);
        //se ja existe mesmo titulo, apenas garante a presença
        shelf.put(title.toLowerCase(), new LoanRecord(borrower,date,title));
        return orderedTitles(shelf);
    }
    
    //return book
    public synchronized List<String> returnBook(String borrower,String date, String title) throws InvalidCommandException {
        borrower = norm(borrower);
        title = norm(title);
        
        if(borrower.isEmpty() || title.isEmpty()){
            throw new InvalidCommandException("borrow requires borrower and title");
        }
        
        LinkedHashMap<String, LoanRecord> shelf = loansByBorrower.get(borrower);
        if(shelf == null || shelf.remove(title.toLowerCase()) == null){
            throw new InvalidCommandException("borrower '" + borrower + "' does not currently hold '" + title + "'");
        }
        
        if(shelf.isEmpty()){
            loansByBorrower.remove(borrower);
        }
        return shelf == null ? Collections.emptyList() : orderedTitles(shelf);        
    }
    
    //list books 
    public synchronized List<String> list(String borrower) throws InvalidCommandException {
        borrower = norm(borrower);
        if(borrower.isEmpty()){
            throw new InvalidCommandException("list requires borrower");
        }
        LinkedHashMap<String, LoanRecord> shelf = loansByBorrower.get(borrower);
        if (shelf == null) return Collections.emptyList();
        return orderedTitles(shelf);
    }
    
    //return title in alphabetical order
    private List<String> orderedTitles(LinkedHashMap<String, LoanRecord> shelf){
        List<String> titles = new ArrayList<>();
        for(LoanRecord r : shelf.values()){
            titles.add(r.getTitle());
        }
        Collections.sort(titles, String.CASE_INSENSITIVE_ORDER);
        return titles;
    }
}

//structure:
//Main HashMap "loansByBorrower" will contain an internal HashMap "shelf" with each client
//each client has their own shelf with their own books:
//
//loansByBorrower = {
//  "Felipe" = { 
//      "clean code" -> LoanRecord("Felipe", "20/10/2025", "Clean Code"),
//      "java concurrency" -> LoanRecord("Felipe", "22/10/2025", "Java Concurrency in Practice")
//  },
//  "Stefany" = {
//      "design patterns" -> LoanRecord("Stefany", "19/10/2025", "Design Patterns")
//  }
//}

