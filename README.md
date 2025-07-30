
This project implements the backend for a Restaurant Review Platform, a web-based application designed to enable users to discover local restaurants, read authentic reviews, and share their own dining experiences. Built with Spring Boot, it leverages Elasticsearch for powerful search capabilities, including geospatial and fuzzy matching, and integrates with Keycloak for robust authentication and authorization.

This repository focuses solely on the backend implementation.

Features
Restaurant Management:

Create, retrieve, update, and delete restaurant details.

Comprehensive restaurant profiles including name, cuisine type, contact information, address, operating hours, and photos.

Automatic geolocation resolution for addresses.

Review System:

Submit detailed reviews with text content, 1-5 star ratings, and associated photos.

Users can only submit one review per restaurant.

Reviews can be updated within a 48-hour window.

Retrieve individual reviews or list all reviews for a restaurant with sorting and pagination.

Automatic recalculation of restaurant average ratings based on submitted reviews.

Advanced Search:

Powerful search functionality powered by Elasticsearch.

Fuzzy search for restaurant names and cuisine types.

Geospatial search to find restaurants within a specified radius of a location.

Filtering by minimum rating and pagination for results.

Photo Uploads:

Securely upload and retrieve photos associated with restaurants and reviews.

Unique ID generation for each photo.

Public access for photo retrieval.

Authentication & Authorization:

Integrated with Keycloak using OAuth2 and OpenID Connect.

Secure API endpoints requiring authentication for creation, update, and deletion operations.

User details extracted from JWT tokens.

Robust Error Handling:

Custom exception hierarchy for application-specific errors (e.g., EntityNotFoundException, ReviewNotAllowedException, StorageException).

Global error handling with consistent JSON responses.

Input validation using Jakarta Bean Validation.

Technologies Used
Backend: Java 21, Spring Boot 3.4.2

Build Tool: Apache Maven

Data Store & Search Engine: Elasticsearch 8.12.0

Identity & Access Management: Keycloak 23.0

Object Mapping: MapStruct (with Lombok integration)

Code Generation: Project Lombok

Containerization: Docker & Docker Compose

Web Framework: Spring Web

Security: Spring Security, Spring OAuth2 Resource Server

Data Access: Spring Data Elasticsearch

Validation: Jakarta Validation
