# Version Control Practices - Ocean View Resort Management System

This document outlines the version control practices, repository structure, branching workflows, and commit history for the Ocean View Resort Management System project.

## Repository Structure

The project follows a standard Java Web Application structure, managed with Apache Ant and NetBeans IDE.

- **`/src/java`**: Contains the Java source code organized by packages (`dao`, `model`, `servlet`, `util`).
- **`/web`**: Contains the web resources, including HTML, JSP, CSS, and configuration files (`WEB-INF`, `META-INF`).
- **`schema.sql`**: The database schema definition for the system.
- **`build.xml`**: The Apache Ant build script.
- **`/nbproject`**: NetBeans IDE project configuration files.
- **`.gitattributes`**: Git configuration for path-specific settings (e.g., LF normalization).

## Branching Workflow

The project utilizes a Gitflow-inspired branching strategy to ensure a clean and organized development process.

- **`main`**: The primary branch containing production-ready code. Every commit on this branch is stable.
- **`develop`**: The main development branch where features and fixes are integrated before being merged into `main`.
- **Feature Branches**: (Implicit) Temporary branches used for developing new features or bug fixes, which are merged into `develop`.

## Commit History

The following is the commit history for the project, demonstrating professional and descriptive commit messages.

| Hash | Date | Message | Author |
|------|------|---------|--------|
| `b34b10c` | 2026-03-02 | Add .gitignore and remove build artifacts | NadeekaSA |
| `34e8c5d` | 2026-02-28 | Initial commit | NadeekaSA |

## Git Best Practices

1.  **Atomic Commits**: Each commit represents a single logical change or improvement.
2.  **Descriptive Messages**: Commit messages clearly explain *what* was changed and *why*.
3.  **Line Normalization**: The repository is configured with `* text=auto` in `.gitattributes` to handle cross-platform line endings (LF) consistently.
4.  **Separation of Concerns**: Source code, web assets, and database schemas are clearly separated within the directory structure.
