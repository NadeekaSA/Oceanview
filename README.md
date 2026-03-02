# Oceanview Hotel Management System

Oceanview is a comprehensive Hotel Management System (HMS) built with Java Jakarta EE. It provides a robust set of tools for hotel administrators and receptionists to manage rooms, guests, reservations, and financial reports efficiently.

## 🚀 Key Features

- **User Authentication & Authorization**: Secure login system with role-based access control (ADMIN and RECEPTIONIST).
- **Guest Management**: Maintain a detailed database of guests, including contact information and identification cards.
- **Room Management**: Track room availability, rates, and maintenance status across different room types (Single, Double, Suite).
- **Reservation System**: Manage the entire booking lifecycle from pending status to check-in, check-out, or cancellation.
- **Financial Reporting**: Generate reports on occupancy and income to monitor hotel performance.
- **Dynamic Dashboard**: Real-time statistics for occupied rooms, available rooms, pending reservations, and total guests.
- **Data Export**: Export reports and data to Excel format for offline analysis.

## 🛠️ Technology Stack

- **Backend**: Java 17, Jakarta Servlet API, Jakarta Server Pages (JSP).
- **Database**: MySQL 8.0+.
- **Build Tool**: Apache Ant.
- **Web Server**: Apache Tomcat 10.1+.
- **Frontend**: HTML5, CSS3 (Custom styles), JavaScript (AJAX).

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
- [Java Development Kit (JDK) 17 or higher](https://adoptium.net/)
- [MySQL Server 8.0+](https://dev.mysql.com/downloads/installer/)
- [Apache Tomcat 10.1+](https://tomcat.apache.org/download-10.cgi)
- [Apache Ant](https://ant.apache.org/bindownload.cgi)

## 🗄️ Database Setup

1. Open your MySQL client (Command Line, MySQL Workbench, etc.).
2. Execute the `schema.sql` file located in the project root:
   ```bash
   mysql -u your_username -p < schema.sql
   ```
   *Note: This will create a database named `oceanview_db` and initialize tables with default data.*
3. Update the database credentials in `src/java/com/oceanview/util/DBConnection.java` if necessary.

## 🏗️ Build and Deployment

### 1. Build the Project
Use Apache Ant to compile and package the application:
```bash
ant clean
ant compile
ant dist
```
The build process will generate a `.war` file in the `dist/` directory (e.g., `dist/Oceanview.war`).

### 2. Deploy to Tomcat
1. Copy the generated `Oceanview.war` file from the `dist/` folder.
2. Paste it into the `webapps/` directory of your Apache Tomcat installation.
3. Start (or restart) the Tomcat server.
4. The application will be accessible at: `http://localhost:8080/Oceanview/`

## 🔑 Default Credentials

- **Admin Account**:
  - Username: `admin`
  - Password: `admin123`

---

