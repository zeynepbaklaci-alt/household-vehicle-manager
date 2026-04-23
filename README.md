# Household Vehicle Manager

## Overview

Household Vehicle Manager is a full-stack application designed to manage vehicles within a household and track their related lifecycle events.

The system allows users to:
- Register and manage vehicles
- Track fuel consumption and costs
- Register insurance policies and ITV inspections
- Automatically generate and manage time-based reminders
- View pending notifications on a dashboard

The project is implemented as a **JavaFX desktop application (frontend)** and a **Spring Boot REST API (backend)**, developed end-to-end by the author as part of an academic full‑stack practice project.

---

## Architecture

The application follows a client-server architecture:

- **Frontend**: JavaFX desktop application
- **Backend**: Spring Boot REST API
- **Database**: PostgreSQL
- **Authentication**: JWT-based authentication

The JavaFX client communicates with the backend through RESTful HTTP endpoints.

---

## Technologies Used

### Frontend
- Java
- JavaFX
- Java HTTP Client

### Backend
- Java
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- RESTful APIs

### Database
- PostgreSQL

---

## Main Features

### Vehicle Management
- Add, list, update, and delete vehicles
- Household-based authorization
- Validation for required fields and odometer input

### Fuel Tracking
- Register fuel fill entries per vehicle
- Odometer-based mileage tracking
- Automatic calculation of total fuel cost
- Edit and delete fuel records

### Insurance & ITV Management
- Register insurance policies per vehicle
- Register ITV inspection records
- Validation of date and business rules
- History view for insurance and ITV data

### Reminder System
- Automatic creation of reminders for:
    - Insurance expiration
    - ITV expiration
- Time-based reminder scheduling (e.g. 30 days before expiration)
- Pending reminders shown on dashboard
- “Dismiss” lifecycle with `sent` and `sentAt` tracking
- Color-coded urgency:
    - Red: ≤ 3 days
    - Orange: ≤ 7 days
    - Green: > 7 days
- Human-readable messages (e.g. *“Expires in 3 days”*)

### Dashboard
- Centralized reminder dashboard
- Real-time pending notification badge
- Reminder history view

---

## Validation Rules

- All forms require mandatory fields to be filled
- Odometer values must be numeric and non-negative
- Insurance end date must be after start date
- ITV validity date must be after inspection date
- Duplicate reminders are prevented
- Unauthorized access is blocked by household membership checks

---

## Application Screens

- Vehicle List View
- Add / Edit Vehicle Dialog
- Vehicle Detail View
- Fuel Management Tab
- Insurance Management Tab
- ITV Management Tab
- Reminder Dashboard
- Reports View

---

## Logging & Error Handling

- UI-level validation prevents invalid requests
- User-friendly error dialogs in the frontend
- Backend authorization and input validation
- Production-safe logging (debug prints removed)

---

## Project Scope

This project is an educational full-stack prototype focusing on:

- Clean architecture
- Separation of concerns
- Secure authorization
- Time-based business logic
- Real-world UX considerations

Both frontend and backend were fully designed and implemented by the author.

---

## Application Screenshots

### Vehicle List
screenshots/vehicle-list.png

### Vehicle Detail View
screenshots/vehicle-detail.png

### Fuel Management
screenshots/fuel-tab.png

### Insurance Management
screenshots/insurance-tab.png

### ITV Management
screenshots/itv-tab.png

### Reminder Dashboard
screenshots/reminder-dashboard.png
---

## Author
ZEYNEP BAKLACI  
Product Software Engineer – Barcelona