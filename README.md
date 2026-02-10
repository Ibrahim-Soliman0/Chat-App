# 💬 ChatApp

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21-4472CA?style=for-the-badge&logo=javafx)
![Maven](https://img.shields.io/badge/Maven-4.0-C71A36?style=for-the-badge&logo=apache-maven)
![RMI](https://img.shields.io/badge/RMI-Callback%20Pattern-informational?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

> A modern, feature-rich desktop chat application built with Java and JavaFX. Connect, communicate, and collaborate seamlessly with real-time messaging, group chats, and intelligent offline message delivery.


---
## 📋 Table of Contents

- [What is ChatApp?](#-what-is-chatapp)
- [Key Features](#-key-features)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Technology Stack](#-technology-stack)
- [Design Patterns](#-design-patterns)
- [RMI Callback System](#-rmi-callback-system)
- [Performance & Optimization](#-performance--optimization)
- [Screenshots](#-screenshots)
- [Quick Start](#-quick-start)
- [Usage Guide](#-usage-guide)
- [Testing](#-testing)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [Team Members](#-team-members)

---

## 🚀 What is ChatApp?

ChatApp is a production-ready desktop messaging application that demonstrates modern Java development practices. It combines a responsive JavaFX frontend with a robust RMI-based backend to deliver real-time communication with offline message support, group chats, and comprehensive user management.

Whether you're looking for a feature-complete messaging platform or learning how to build scalable Java applications, ChatApp has you covered.

---

## ✨ Key Features

### 💫 Real-Time Communication
- **Instant Messaging** — Send and receive messages with zero latency using RMI callbacks
- **Group Chats** — Create group conversations and manage multiple participants
- **Status Updates** — Share your availability (Online, Away, Busy, Offline)
- **Offline Message Delivery** — Messages are stored server-side and delivered when users come online

### 👥 User Management
- **Phone Number Authentication** — Secure login with unique phone-based registration
- **Contact Management** — Add, remove, and organize your contacts
- **User Profiles** — View user statistics and information
- **Session Management** — Multiple sign-outs with session tracking

### 📁 File & Content Sharing
- **File Transfer** — Share files securely between users
- **Message History** — Access all historical conversations
- **Audio/Video Player** — Built in audio and video players for the supported formats
- **Real-Time Notifications** — Stay updated with instant visual and sound alerts

### ⚙️ Server Administration
- **Server Control Panel** — Start/stop services and monitor status
- **User Statistics** — Track user demographics (gender, country, usage patterns)
- **Broadcast Announcements** — Send server-wide messages to all users
- **Persistent Storage** — All data securely stored in MySQL

---

## 🏗️ Architecture

ChatApp follows a **Client-Server Architecture** with clear separation of concerns:
<div align="center">
<img width="600" height="600" alt="Architecture - visual selection" src="https://github.com/user-attachments/assets/56344106-0477-4a73-b5f4-b916d8a4279f" />
</div>
---

## 📂 Project Structure

```
Chat-App/                                        
│
├── 📁 client-Chat-App/                          # Client application module
│   ├── src/main/java/org/client/chatapp/
│   │   ├── config/                               # Client configuration & initialization
│   │   ├── model/                                # Local data models & objects
│   │   ├── rmi/                                  # RMI client connector & callback handler
│   │   ├── ui/                                   # User interface package
│   │   │   ├── component/                        # Reusable UI components & custom nodes
│   │   │   ├── controller/                       # JavaFX scene controllers & event handlers
│   │   │   ├── listener/                         # Custom event listeners & handlers
│   │   │   └── utils/                            # UI utilities & formatting helpers
|   |   |
│   │   └── ClientChatApp.java                    # Client application entry point
|   |   
│   ├── src/main/resources/
│   │   ├── css/                                  # Stylesheets for UI theming
│   │   ├── icons/                                # UI icons & visual assets
│   │   ├── Message Downloads/                    # Downloaded file storage
│   │   ├── org/client/chatapp/                   # FXML layout files
│   │   └── sounds/                               # Notification & alert sounds
|   |
│   └── pom.xml                                   # Maven configuration
│
├── 📁 common-module/                             # Shared data & interfaces module
│   ├── src/main/java/
│   │   ├── dto/                                  # Data Transfer Objects for network communication
│   │   ├── model/                                # Shared data models
│   │   │   └── enums/                            # Shared enumerations (Status, MessageType, etc.)
│   │   └── rmi/                                  # RMI remote interfaces & serializable objects
|   | 
│   ├── src/main/resources/                       # Configuration resources
|   |
│   └── pom.xml                                   # Maven configuration
│
├── 📁 server-Chat-App/                           # Server application module
│   ├── src/main/java/org/server/chatapp/
│   │   ├── dao/                                  # Data Access Layer
│   │   │   ├── dao/                              # DAO interfaces for CRUD operations
│   │   │   └── implement/                        # DAO implementation classes
│   │   ├── rmi/                                  # RMI service implementations & handlers
│   │   ├── ui/                                   # Server admin dashboard UI
│   │   │   └── controller/                       # Admin panel controllers & handlers
│   │   ├── util/                                 # Server utilities & helpers
|   |   |
│   │   └── ServerChatApp.java                    # Server application entry point
|   | 
│   ├── src/main/resources/
│   │   ├── css/                                  # Admin UI stylesheets
│   │   ├── icon/                                 # Admin UI icons & assets
│   │   └── org/server/chatapp/                   # FXML layout files for admin panel
|   |
│   ├── uploads/                                  # File storage directory
│   │   ├── message files/                        # Transferred files from users
│   │   └── profiles/                             # User profile images
|   |
│   └── pom.xml                                   # Maven configuration
│
└── pom.xml                                       # Parent Maven configuration (multi-module)
```
---

## 🛠️ Technology Stack

| Layer | Technology             | Purpose |
|-------|------------------------|---------|
| **Frontend** | JavaFX 21 + FXML + CSS | Modern desktop UI |
| **Backend** | Java 21                | Core application logic |
| **Communication** | Java RMI               | Remote method invocation & callbacks |
| **Database** | MySQL 8.0 + JDBC       | Persistent data storage |
| **Build** | Maven 4.0              | Multi-module project management |
| **Security** | jBCrypt                | Password hashing & verification |
| **Connection Pool** | HikariCP               | High-performance database pooling |
| **Testing** | JUnit 5                | Unit testing |
| **Testing Database** | H2 Database | In-memory database for unit tests |

---

## 🔌 RMI Callback System

ChatApp uses a sophisticated **RMI callback mechanism** for real-time communication:

### 🔎 How It Works

1. **Client Registration** — Client registers a callback interface when logging in
2. **Server Storage** — Server maintains a list of active client callbacks
3. **Event Triggering** — When an event occurs (new message, status change), the server invokes the appropriate callback
4. **Instant Delivery** — Client UI updates immediately without polling

### 🔄 Real-Time Communication Flow

```
User Sends Message
        ↓
Client RMI Call → Server Service
        ↓
Server Processes & Stores
        ↓
Server Invokes Client Callback → Recipient Receives Instantly
```

### ✔️ Supported Events

- **Incoming Messages** (private & group)
- **Status Changes** (online/offline/away/busy)
- **Server Announcements**
- **New Friend Requests**

---

## 🎨 Design Patterns

| Pattern | Purpose | Location |
|---------|---------|----------|
| **MVC** | Separation of UI, logic, and data | Client & Server UI |
| **DAO** | Database abstraction layer | `server/dao/` |
| **Singleton** | Single service instances | Services & Config |
| **Observer** | Event-driven notifications | RMI Callbacks |
| **Factory** | Object creation abstraction | Service factories |

---

## ⚡ Performance & Optimization

- **Connection Pooling** — HikariCP manages database connections efficiently
- **Multithreading** — Server handles multiple clients concurrently
- **Async Operations** — Message sending, file uploading and downloading are processed asynchronously
- **UI Threading** — All UI updates on JavaFX Application Thread
- **Lazy Loading** — Contact lists and message histories loaded on demand
---

## 🖼 Screenshots

### Client Side
#### 🔐 Login Screen
<div align="center">
<img width="679" height="1022" alt="Screenshot 2026-02-11 005953" src="https://github.com/user-attachments/assets/832589a4-742d-4980-bb21-46a02c212409" />
</div>

---

#### 🏠 Main Chat Screen
<div align="center">
<img width="679" height="1022" alt="Screenshot 2026-02-11 010227" src="https://github.com/user-attachments/assets/6cc0e59c-8207-468d-a63c-62a5da5b0a91" />
</div>

---

#### 🔔 Notification Screen
<div align="center">
<img width="679" height="1022" alt="Screenshot 2026-02-11 010258" src="https://github.com/user-attachments/assets/0c867441-2f51-41bb-a27e-4c58879af53b" />
</div>
---

#### 💬 Chat Screen
<div align="center">
<img width="679" height="1022" alt="Screenshot 2026-02-11 010438" src="https://github.com/user-attachments/assets/2a636b02-887f-46b6-a50d-0ecab811478e" />
</div>

---

### Server Side
#### 🏠 Main Server Screen
<div align="center">
<img width="1580" height="1097" alt="Screenshot 2026-02-11 005825" src="https://github.com/user-attachments/assets/d33cedd4-36f2-441d-9e43-8aff26f74f64" />
</div>

---

#### 📈 Statistics Server Screen
<div align="center">
<img width="1580" height="1097" alt="Screenshot 2026-02-11 005928" src="https://github.com/user-attachments/assets/e37b27d2-ab4d-49e9-90c2-21ac25c8949d" />
</div>


---

### 📊 Database Schema Diagram
<div align="center">
<img width="600" height="600" alt="Screenshot 2026-02-11 004132" src="https://github.com/user-attachments/assets/437d51c2-912c-4a8b-afab-419db33710c2" />
</div>

---

## 🚀 Quick Start

### Prerequisites

- **Java Development Kit (JDK)** 21 or higher
- **Maven** 4.0 or higher
- **MySQL** 8.0 or higher
- **Git** for cloning the repository

### Installation & Setup

#### 1️⃣ Clone the Repository
```bash
git clone https://github.com/Ibrahim-Soliman0/Chat-App.git
```

#### 2️⃣ Configure Database

Create a MySQL database and update the connection properties:

**File:** `server-Chat-App/src/main/resources/db.properties`
```properties
db.url=jdbc:mysql://localhost:3306/chat_db
db.username=your_username
db.password=your_password
```

#### 3️⃣ Build the Project
```bash
mvn clean install
```

#### 4️⃣ Run the Server
```bash
cd server-Chat-App
mvn javafx:run
# Or use JAR:
# java -jar target/server-Chat-App-1.0-SNAPSHOT.jar
```

#### 5️⃣ Run the Client (in a new terminal)
```bash
cd client-Chat-App
mvn javafx:run
# Or use JAR:
# java -jar target/client-Chat-App-1.0-SNAPSHOT.jar
```
---

## 📖 Usage Guide

### For Users

1. **Register** — Create an account using your phone number
2. **Login** — Sign in with your credentials
3. **Add Contacts** — Search and add other users
4. **Start Chatting** — Send messages, create groups, share files
5. **Manage Status** — Update your availability status

### For Administrators

1. Launch the server application
2. Access the admin dashboard
3. Monitor online users and statistics
4. Send server-wide announcements
5. Manage user accounts and permissions

---

## 🧪 Testing

Run unit tests with:

```bash
mvn test
```

Test coverage includes:
- Service layer logic
- DAO operations
- Database mocking
---

## 🚀 Future Enhancements

- **Voice & Video Calls** — Real-time audio/video streaming
- **End-to-End Encryption** — Message encryption with keys
- **Enhanced UI** — Modern themes and dark mode
- **Chatbot Integration** — AI-powered assistance
- **Cloud Deployment** — Docker & Kubernetes support
---

## 👥 Team Members

- **Ibrahim Soliman**
- **Ahmed Mohamed Ramadan**
- **Momen Mostafa**
- **Islam Magdy**
- **Nourhan Ezzat**
---
