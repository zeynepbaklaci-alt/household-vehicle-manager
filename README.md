# Household Vehicle Manager

## Overview
Household Vehicle Manager is a full-stack application developed to manage a family’s vehicles and their related fuel records.
The project consists of a **JavaFX desktop application (frontend)** and a **Spring Boot REST API (backend)**, both developed by the author as part of a final academic practice.

The system allows users to register vehicles, track fuel fills with odometer data, and calculate fuel costs.

## Architecture
The application follows a client-server architecture:

- **Frontend**: JavaFX desktop application
- **Backend**: Spring Boot REST API
- **Database**: PostgreSQL

The JavaFX client communicates with the backend via RESTful HTTP requests.

## Technologies Used
### Frontend
- Java
- JavaFX

### Backend
- Java
- Spring Boot
- Spring Data JPA
- REST API

### Database
- PostgreSQL

## Main Features
- Add and list vehicles
- Register fuel fill records per vehicle
- Automatic calculation of total fuel cost
- Odometer-based mileage tracking
- Validation of business rules

## Application Screens
- Vehicle List
- Add Vehicle Dialog
- Vehicle Detail View
- Add Fuel Fill Dialog

## Validation Rules
- Vehicle plate must be unique
- Odometer value cannot decrease
- Fuel liters and price per liter must be greater than zero

## Project Scope
This project is an educational full-stack prototype.
Both the frontend and backend were designed and implemented by the author.

## Author
ZEYNEP BAKLACI  
Product Software Engineer – Barcelona