# 🚀 **Rwanda Coding Academy Management Information System (RCA MIS)**

A comprehensive, production-ready backend system for managing educational institutions built with Spring Boot 3.x, Java 17+, and PostgreSQL.

## 📋 **Table of Contents**

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Database Setup](#-database-setup)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ **Features**

### 🔐 **Authentication & Security**
- JWT-based authentication with refresh tokens
- Role-based access control (RBAC)
- Password encryption with BCrypt
- API rate limiting
- CORS configuration
- Input validation and sanitization

### 👥 **User Management**
- Multi-role user system (Admin, Teacher, Student, Parent, Guardian)
- User profiles with detailed information
- Role-based permissions
- Account status management

### 🎓 **Academic Management**
- Academic years and terms
- Class and subject management
- Student enrollment and records
- Teacher assignments and qualifications
- Assessment and grading system

### 📊 **Student Management**
- Student profiles and academic records
- Attendance tracking
- Project portfolio management
- Performance analytics
- Report card generation

### 👨‍🏫 **Teacher Management**
- Teacher profiles and qualifications
- Subject assignments
- Class assignments
- Workload management

### 👨‍👩‍👧‍👦 **Parent/Guardian Portal**
- Access to child's academic information
- Attendance monitoring
- Performance tracking
- Communication with teachers

### 📅 **Timetable Management**
- Class schedules
- Teacher schedules
- Room assignments
- Schedule conflicts detection

### 📈 **Reports & Analytics**
- Academic performance reports
- Attendance reports
- Class rankings
- Subject performance analysis

### 💬 **Communication System**
- Internal messaging
- Notifications
- Announcements
- Email integration

## 🛠 **Technology Stack**

### **Backend Framework**
- **Spring Boot 3.5.4** - Main application framework
- **Java 17+** - Programming language
- **Spring Security 6.x** - Security framework
- **Spring Data JPA** - Data access layer
- **Spring Validation** - Input validation

### **Database**
- **PostgreSQL 15+** - Primary database
- **Flyway** - Database migration tool

### **Security**
- **JWT (JSON Web Tokens)** - Authentication
- **BCrypt** - Password hashing
- **Spring Security** - Security framework

### **Documentation**
- **OpenAPI 3.0 (Swagger)** - API documentation
- **SpringDoc** - OpenAPI integration

### **Build & Testing**
- **Maven** - Build tool
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **TestContainers** - Integration testing

### **Utilities**
- **Lombok** - Code generation
- **MapStruct** - Object mapping
- **Apache POI** - Excel operations
- **iText7** - PDF generation

## 📋 **Prerequisites**

Before running this application, ensure you have the following installed:

- **Java 17 or higher**
- **Maven 3.6+**
- **PostgreSQL 15+**
- **Git**

### **Java Version Check**
```bash
java -version
```

### **Maven Version Check**
```bash
mvn -version
```

### **PostgreSQL Version Check**
```bash
psql --version
```

## 🚀 **Quick Start**

### **1. Clone the Repository**
```bash
git clone https://github.com/rca/mis-backend.git
cd mis-backend
```

### **2. Database Setup**
```bash
# Create PostgreSQL database
createdb rca_mis_db

# Or using psql
psql -U postgres
CREATE DATABASE rca_mis_db;
\q
```

### **3. Configure Application Properties**
Edit `src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/rca_mis_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
app.jwt.secret=your-super-secret-jwt-key-here-make-it-very-long-and-secure
```

### **4. Run Database Migrations**
```bash
mvn flyway:migrate
```

### **5. Build and Run**
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### **6. Access the Application**
- **Application**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🗄 **Database Setup**

### **Manual Setup**
1. Install PostgreSQL 15+
2. Create database: `rca_mis_db`
3. Run migrations: `mvn flyway:migrate`

### **Docker Setup**
```bash
# Run PostgreSQL in Docker
docker run --name rca-mis-postgres \
  -e POSTGRES_DB=rca_mis_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15

# Run migrations
mvn flyway:migrate
```

### **Initial Data**
The system comes with pre-seeded data:
- Default roles (SUPER_ADMIN, ADMIN, TEACHER, STUDENT, PARENT, GUARDIAN)
- Sample academic years and terms
- Sample subjects and classes
- Sample users for testing

**Default Login Credentials:**
- **Super Admin**: admin@rca.ac.rw / Admin@123
- **Teacher**: teacher@rca.ac.rw / Teacher@123
- **Student**: student@rca.ac.rw / Student@123
- **Parent**: parent@rca.ac.rw / Parent@123

## ⚙ **Configuration**

### **Environment Variables**
```bash
export SPRING_PROFILES_ACTIVE=dev
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=rca_mis_db
export DB_USERNAME=postgres
export DB_PASSWORD=password
export JWT_SECRET=your-super-secret-jwt-key
```

### **Profile-Specific Configuration**
- **Development**: `application-dev.properties`
- **Production**: `application-prod.properties`
- **Testing**: `application-test.properties`

### **Key Configuration Properties**
```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/rca_mis_db
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

# JWT Configuration
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000
app.jwt.refresh-expiration=604800000

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## 📚 **API Documentation**

### **Swagger UI**
Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### **OpenAPI Specification**
Download the OpenAPI specification at:
```
http://localhost:8080/api-docs
```

### **API Endpoints Overview**

#### **Authentication**
- `POST /auth/login` - User login
- `POST /auth/refresh-token` - Refresh access token
- `POST /auth/logout` - User logout
- `POST /auth/change-password` - Change password

#### **User Management**
- `GET /users` - Get all users
- `POST /users` - Create user
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

#### **Student Management**
- `GET /students` - Get all students
- `POST /students` - Create student
- `GET /students/{id}/academic-record` - Get academic record
- `GET /students/{id}/attendance` - Get attendance

#### **Academic Management**
- `GET /classes` - Get all classes
- `GET /subjects` - Get all subjects
- `GET /assessments` - Get all assessments
- `POST /assessments/{id}/marks` - Submit marks

## 🧪 **Testing**

### **Run All Tests**
```bash
mvn test
```

### **Run Specific Test Categories**
```bash
# Unit tests only
mvn test -Dtest=*UnitTest

# Integration tests only
mvn test -Dtest=*IntegrationTest

# Security tests only
mvn test -Dtest=*SecurityTest
```

### **Test Coverage**
```bash
mvn jacoco:report
```

### **Test with TestContainers**
```bash
mvn test -Dspring.profiles.active=test
```

## 🚀 **Deployment**

### **Docker Deployment**
```bash
# Build Docker image
docker build -t rca-mis-backend .

# Run container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-db-host \
  -e DB_PASSWORD=your-db-password \
  rca-mis-backend
```

### **Production Deployment**
1. Set `SPRING_PROFILES_ACTIVE=prod`
2. Configure production database
3. Set secure JWT secret
4. Configure email settings
5. Set up SSL/TLS
6. Configure logging
7. Set up monitoring

### **Environment Variables for Production**
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_HOST=production-db-host
export DB_PASSWORD=secure-production-password
export JWT_SECRET=very-long-secure-production-secret
export MAIL_HOST=smtp.production.com
export MAIL_PASSWORD=secure-mail-password
```

## 📁 **Project Structure**

```
src/main/java/com/rca/mis/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/             # Data transfer objects
│   ├── request/     # Request DTOs
│   └── response/    # Response DTOs
├── exception/       # Custom exceptions
├── model/           # Entity models
│   ├── academic/    # Academic entities
│   ├── student/     # Student entities
│   ├── teacher/     # Teacher entities
│   └── user/        # User entities
├── repository/      # Data access layer
├── security/        # Security configuration
├── service/         # Business logic
│   └── impl/       # Service implementations
├── util/            # Utility classes
└── validation/      # Custom validators

src/main/resources/
├── application.properties    # Application configuration
├── db/migration/            # Database migrations
└── static/                  # Static resources

src/test/java/               # Test classes
```

## 🔧 **Development**

### **Code Style**
- Follow Java coding conventions
- Use meaningful variable and method names
- Include comprehensive JavaDoc
- Follow Spring Boot best practices

### **Git Workflow**
```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Make changes and commit
git add .
git commit -m "feat: add your feature description"

# Push and create pull request
git push origin feature/your-feature-name
```

### **Pre-commit Hooks**
```bash
# Install pre-commit hooks
mvn install

# Run code quality checks
mvn checkstyle:check
mvn spotbugs:check
```

## 📊 **Monitoring & Health Checks**

### **Actuator Endpoints**
- **Health Check**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`
- **Environment**: `/actuator/env`

### **Custom Health Indicators**
- Database connectivity
- External service health
- System resource usage

## 🔒 **Security Features**

### **Authentication**
- JWT token-based authentication
- Token refresh mechanism
- Secure password storage

### **Authorization**
- Role-based access control
- Method-level security
- URL-level security

### **Data Protection**
- Input validation
- SQL injection prevention
- XSS protection
- CSRF protection

## 📈 **Performance Optimization**

### **Database Optimization**
- Proper indexing strategy
- Query optimization
- Connection pooling
- Lazy loading

### **Caching Strategy**
- Redis integration (optional)
- In-memory caching
- Query result caching

### **API Optimization**
- Pagination for large datasets
- Response compression
- Rate limiting
- Request/response logging

## 🚨 **Troubleshooting**

### **Common Issues**

#### **Database Connection Issues**
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Check connection
psql -h localhost -U postgres -d rca_mis_db
```

#### **Port Already in Use**
```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

#### **JWT Token Issues**
- Ensure JWT secret is properly configured
- Check token expiration settings
- Verify token format in Authorization header

### **Logs**
```bash
# View application logs
tail -f logs/rca-mis.log

# View Spring Boot logs
tail -f logs/spring.log
```

## 🤝 **Contributing**

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

### **Development Guidelines**
- Follow the existing code style
- Add comprehensive tests
- Update documentation
- Include meaningful commit messages

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 **Support**

For support and questions:

- **Email**: dev@rca.ac.rw
- **Website**: https://www.rca.ac.rw
- **Documentation**: [Wiki](https://github.com/rca/mis-backend/wiki)
- **Issues**: [GitHub Issues](https://github.com/rca/mis-backend/issues)

## 🙏 **Acknowledgments**

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- Open source contributors for various libraries
- Rwanda Coding Academy for the vision and support

---

**Made with ❤️ by the RCA Development Team**
#   M I S - B a c k e n d - S e r v e r - D e v e l o p m e n t  
 