# 📘 Advanced Programming – Library Borrowing System (Version B)

**National College of Ireland – Advanced Programming CA (October 2025)**  
Author: **Felipe Paneque (x23156635)**  
Location: Dublin, Ireland  

---

## 🧠 Overview
This project implements a **multi-threaded TCP Client–Server Library Borrowing System** in Java.  
It allows multiple clients to connect simultaneously, borrow and return books, and view loan records — all managed through synchronized shared memory on the server.

---

## ⚙️ Features

### 🧩 TCP Communication
- Implemented using **`ServerSocket`** and **`Socket`** for reliable client–server communication.
- Each connected client runs in its own **`Thread`** using **`Runnable`**, enabling concurrent sessions.

### 🔁 Streams
- **`BufferedReader`** and **`PrintWriter`** handle input/output streams for text-based message exchange between clients and server.

### 🧵 Multithreading
- The server spawns a new thread (`ClientsConnection`) for every client connection.
- Each thread processes commands independently, maintaining real-time communication with the shared library store.

### 🔒 Synchronisation
- Shared memory is managed by a central **`LibraryStore`** class.
- Methods are declared **`synchronized`** to ensure thread safety when multiple clients borrow or return books concurrently.

### ⚠️ Exception Handling
- Extensive use of **`try/catch/finally`** blocks for stream and socket management.
- Includes a **custom `InvalidCommandException`** to handle malformed client commands (e.g. missing parameters or wrong format).

### 🌐 HTTP Integration (GET)
- The client automatically imports borrower and book data from a public `.txt` file via **`HTTP GET`** using **`URL`** and **`URLConnection`**.
- Each valid entry is sent to the server as a `borrow; borrower; date; title` command at startup.

---

## 🧩 Supported Commands

| Command | Description |
|----------|--------------|
| `borrow; <name>; <date>; <title>` | Borrow a book |
| `return; <name>; -; <title>` | Return a borrowed book |
| `list; <name>; -; -` | List all books currently borrowed by the user |
| `STOP` | End the client connection gracefully |
| `import; <url>` | (Optional) Manually import data from a public `.txt` file |


## 🧠 Technologies Used

- **Java 17**
- **TCP Sockets:** `ServerSocket`, `Socket`
- **Streams:** `BufferedReader`, `PrintWriter`
- **Multithreading:** `Thread`, `Runnable`
- **Synchronisation:** `synchronized`
- **Exception Handling:** `try/catch/finally`
- **Custom exceptions:** `InvalidCommandException`
- **HTTP GET:** `URL`, `URLConnection`

---

## 🚀 How to Run

1. **Start the Server**
   ```bash
   javac Server.java ClientsConnection.java LibraryStore.java LoanRecord.java InvalidCommandException.java
   java Server

2. **Start the Client**
   ```bash
   javac Client.java


## 🗂️ Project Structure

/ServerApplication
├── Server.java
├── ClientsConnection.java
├── LibraryStore.java
├── LoanRecord.java
└── InvalidCommandException.java

/ClientApplication
├── Client.java

## 🧾 Academic Context
**This project was developed as part of the Advanced Programming Continuous Assessment (CA) for the
BSc (Hons) in Computer Science at the National College of Ireland (NCI).**

It demonstrates key concepts of advanced Java programming:
Socket-based client/server communication
Thread management and synchronization
Exception handling
Network I/O streams
HTTP integration via URL connections

👨‍💻 Author

Felipe Traskinas Malta Paneque
National College of Ireland
📍 Dublin, Ireland
