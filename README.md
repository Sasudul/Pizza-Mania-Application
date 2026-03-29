# Pizza Mania - Mobile Ordering Application

Pizza Mania is an Android-based pizza ordering application built with **Java**, **XML**, and **SQLite**.  
It provides a complete mobile ordering experience with customer authentication, cart management, location-based delivery support, and admin/staff management features.

## Overview

Pizza Mania is designed to simulate a real-world pizza ordering system.  
The application supports different user roles and provides branch-aware menu handling, order management, and local data persistence using SQLite.

## Features

### Customer Features
- User registration and login
- Secure session management using SharedPreferences
- Browse branch-based pizza menus
- Add, update, and remove items from cart
- Checkout and order placement
- View order history
- Save and manage delivery address
- Location detection using GPS/Fused Location Provider
- Profile management with image upload

### Admin / Staff Features
- Admin dashboard
- Add employee/staff details
- View staff records
- Manage branches and menu-related data
- Handle delivery and order assignment workflows

### Application Highlights
- Built with Android Studio
- Developed in Java with XML layouts
- Uses SQLite for local storage
- Role-based application flow
- Modern and mobile-friendly user interface
- Branch-aware menus for different locations

## Tech Stack

- **Language:** Java
- **UI:** XML
- **Database:** SQLite
- **Platform:** Android
- **Tools:** Android Studio, Gradle

  
## Database Tables

The application uses SQLite tables such as:

- **Users** - Stores customer details
- **Staff** - Stores staff and delivery personnel data
- **Branches** - Stores branch information
- **Menu** - Stores pizza items, descriptions, categories, and prices
- **Orders** - Stores order details, status, and pricing

## Installation

### Prerequisites
- Android Studio
- Android SDK
- Java JDK
- An Android device or emulator

### Steps to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/Sasudul/Pizza-Mania-Application.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies.
4. Run the app on an emulator or connected Android device.

## Contributors

Contributors of this project are:

1. **Sasudul** (`sasudul`)
2. **Sanugi** (`sanugi06`)
3. **Ishara** (`IsharaLakshan2002`)
4. **Branjana** (`sureshbranjana`)
