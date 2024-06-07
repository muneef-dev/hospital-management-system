# Hospital Management System
The Hospital Management System is a comprehensive application developed using Java, JavaFX, and MySQL, aimed at streamlining administrative and operational tasks within healthcare facilities. It prioritizes user-friendly interfaces and efficient programming to effectively meet the hospital's needs.

## Table of Contents

- [Introduction](#introduction)
- [Functionality](#functionality)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [Dependencies](#dependencies)
- [Authors](#authors)

## Introduction

The Hospital Management System is a standalone application developed to automate various administrative and operational tasks within a healthcare facility. Its functionalities include patient management, appointment scheduling, billing management, inventory management, and reporting. The system aims to provide a user-friendly interface and efficient programming to meet the hospital's needs effectively.

## Functionality

### User Authentication and Authorization

- Implement a secure login system for hospital staff with role-based access control.
- Differentiate access levels for administrators, doctors, nurses, and other staff members.

### Patient Management

- Allow the registration of new patients with essential details such as name, contact, address, phone number, etc.
- Provide the ability to update and maintain existing patient information.
- Generate and manage unique patient IDs.
- Search for existing patients by ID or name.

### Appointment Management

- Implement a user-friendly interface for scheduling and managing appointments.
- Enable staff to view available appointment slots for doctors and specialists.
- Allow appointment rescheduling and cancellation by staff.

### Billing and Insurance

- Automate the billing process, generating invoices for services provided.
- Ensure transparency in billing by providing detailed invoices.
- Calculate patient billing based on services rendered and medications prescribed.

### Inventory Management

- Implement alerts for low inventory levels to facilitate timely restocking.
- Enable administrators to update inventory status manually.

### Reporting and Analytics

- Generate reports on patient demographics, billing, and admissions.
- Analyze billing and revenue data for financial performance monitoring.

### Security Measures

- Implement secure password policies and mechanisms for user authentication.

## Usage

To use this application:

1. Clone the repository to your local machine.
2. Import the project into your preferred Java IDE.
3. Set up a MySQL database using the provided SQL scripts.
4. Modify the database connection details in the configuration files.
5. Build and run the application.
6. Access the application through the provided GUI and explore its functionalities.

## Database Schema

The database schema consists of several entities including User, Patient, Department, Doctor, Nurse, Appointment, Room, Diagnosis, Admission, Inventory Item, Medication, and Bill. Each entity has specific attributes and relationships defined within the schema.

## Dependencies

This project utilizes the following dependencies:

- MySQL Connector/J: JDBC driver for connecting Java applications to MySQL databases.
- JFoenix: JavaFX Material Design Library.
- ControlsFX: Additional JavaFX UI Controls.
- JavaMail API: Java API for sending and receiving email.
- Apache Commons: Various reusable Java components.
- Lombok: Java library to reduce boilerplate code.
- JasperReports: Reporting tool for generating reports in Java applications.
- ZXing: Library for generating QR codes.

## Authors

This project was developed by [@Muneef-dev](https://github.com/muneef-dev).
