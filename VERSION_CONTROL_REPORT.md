# Version Control Practices Report: Ocean View Resort Management System

## 1. Introduction
This report details the version control practices implemented in the Ocean View Resort Management System project. It showcases the repository structure, branching workflows, and commit history that reflect professional software development standards.

## 2. Repository Structure
The project follows a standard Java Web Application structure, ensuring a clear separation of concerns between the backend logic, data access, and frontend presentation.

### Core Directory Layout:
- `src/java/com/oceanview/`: Contains the Java source code organized into logical packages:
    - `dao/`: Data Access Objects for database interactions (`GuestDAO`, `ReservationDAO`, etc.).
    - `model/`: Plain Old Java Objects (POJOs) representing the domain entities (`Guest`, `Room`, `Reservation`, etc.).
    - `servlet/`: Controller layer handling HTTP requests and business flow.
    - `util/`: Utility classes such as `DBConnection` for database management.
- `web/`: Contains the web-facing assets:
    - `admin/`: Restricted administrative interface components.
    - `receptionist/`: Interface for hotel staff.
    - `css/`: Stylesheets for consistent UI across the application.
    - `index.jsp`: Entry point for the application.
- `schema.sql`: Database schema definition for environment reproducibility.
- `build.xml`: Ant build configuration for automated compilation and deployment.
- `.gitattributes`: Configures Git path-specific settings (e.g., line ending normalization).

## 3. Branching Workflow
The project utilizes a structured branching strategy to manage development and production-ready code effectively.

### Key Branches:
- **`main`**: Represents the stable, production-ready state of the application. Currently contains the initial baseline.
- **`develop`**: The primary integration branch for ongoing features and fixes. Recent enhancements to the schema and reservation logic were integrated here first.
- **Feature Branches**: (Implicit) New features or specific fixes are developed in isolated branches before being merged into `develop` or `main`.

## 4. Commit History and Professional Practices
The commit history demonstrates a disciplined approach to version control:

### Descriptive Commit Messages:
Commits are labeled with clear, concise messages that explain the "what" and "why" of the changes:
- `34e8c5d Initial commit`: Establishes the foundational project structure.
- `84dc91c Add RESERVED and BOOKED statuses to schema`: Clearly defines a schema update to support enhanced reservation states.

### Atomic Commits:
Changes are grouped logically. For example, the commit `84dc91c` updates both the database schema and the corresponding application logic to handle the new statuses, ensuring the repository remains in a consistent state.

### Metadata Tracking:
Git is used to track author contributions (`NadeekaSA`) and timestamps, providing a clear audit trail of the project's evolution.

## 5. Conclusion
By adhering to these version control practices, the Ocean View Resort project ensures code maintainability, team collaboration efficiency, and a reliable path from development to deployment.
