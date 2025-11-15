
# 🏘️ Local Community Helper

*A platform that connects community users with nearby service providers.*

---

## 🚀 Overview

**Local Community Helper** is a Java-based application designed to bridge the gap between **local helpers** (like plumbers, electricians, tutors, tiffin providers, barbers, etc.) and **community residents** who need quick and reliable services.

This project manages:

* User registration
* Helper registration
* Search by category
* Contact details viewing
* Logging & system tracking
* Basic service request workflow

The objective is to create a **simple, easy-to-use** system that digitalizes small community services and brings visibility to local workers.

---

## 🎯 Features

### 👤 User Features

* Register as a normal user
* Login with credentials
* Search helpers by category
* View helper profiles
* Raise service requests

### 🧑‍🔧 Helper Features

* Register as a service provider
* Add skills, category, and availability
* Update basic details
* Receive user service requests (future feature)

### 🔐 Admin Features (Future Enhancements)

* View all users & helpers
* Manage categories
* Remove spam/fake entries
* Monitor system logs

### ⚙️ System Functionalities

* Input validation
* Proper layered architecture
* Logging all runtime operations
* Modular and extendable code structure

---

## 🏗️ Project Architecture

```
User / Helper
      │
      ▼
Controller (Handles Inputs)
      │
      ▼
Service Layer (Business Logic)
      │
      ▼
DAO Layer (Data Storage Logic)
      │
      ▼
Local File Storage / Logs
```

Each operation flows through **Controller → Service → DAO**, ensuring clean separation of concerns.

---

## 📂 Project Structure

```
LocalCommunityHelper/
│
├── HelperConnect/
│   ├── src/
│   │   ├── com/local/helper/
│   │   │   ├── controller/   # Handles user & helper actions 
│   │   │   ├── model/        # POJO classes like User, Helper
│   │   │   ├── service/      # Business logic & validations
│   │   │   ├── dao/          # File/data access logic
│   │   │   └── utils/        # Helper utilities, validators
│   │   └── resources/        # Configs
│   │
├── logs/                     # Runtime logs + archived logs (.gz)
│
└── README.md                 # This file
```

---

## 🔧 Workflow Explanation

### **1. User Registration**

* User enters basic information
* Controller validates input
* Service checks for duplicates
* DAO stores data
* Operation logged

### **2. Helper Registration**

* Helper selects category (plumber, electrician, etc.)
* Enters availability + contact details
* Stored in database/file system

### **3. Login Process**

* Credentials verified
* Role-based options enabled (User/Helper/Admin)

### **4. Searching Helpers**

* Search by category
* Matches returned to user

### **5. Logging**

All actions and errors are stored in:

```
/logs/local-helper-dev.log
/logs/local-helper-dev.log.YYYY-MM-DD.gz
```

---

## 🛠️ Technology Stack

| Component     | Technology                     |
| ------------- | ------------------------------ |
| Language      | Java                           |
| Architecture  | MVC / Layered Architecture     |
| Data Handling | File-based storage (DAO layer) |
| Logging       | Java Util Logging              |
| IDE           | IntelliJ IDEA                  |

---

## ▶️ How to Run the Project Locally

### **1. Prerequisites**

* Java JDK 8+
* IntelliJ IDEA or any Java IDE

### **2. Setup Steps**

1. Clone this repository:

   ```
   git clone https://github.com/tripathi1036/LocalCommunityHelper.git
   ```
2. Open project in IntelliJ
3. Go to the **HelperConnect** directory
4. Run the main application file
5. Logs will appear in:

```
/logs/local-helper-dev.log
```

---

## 📌 Future Enhancements

* Add MySQL/PostgreSQL database
* Convert project to Spring Boot REST API
* Add JWT authentication
* Create Android/iOS mobile app
* Introduce helper rating system
* Add real-time notifications
* Add geo-location based “near me” search
* Admin dashboard with analytics


