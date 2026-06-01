# 🥗 Vegetarian Food Web Shop

An open-source online vegetarian food ordering platform built with **Spring Boot 3.2**, **Spring Security**, **JWT Authentication**, and **Camunda BPMN**. Features real-time order management, payment integration, and email notifications.

## ✨ Features

- **👤 User Management**
  - User registration and authentication
  - Google OAuth2 integration
  - JWT-based authorization
  - Role-based access control (RBAC)

- **🛒 Product Management**
  - Product catalog with categories
  - Product filtering and search
  - Inventory management
  - Product images and descriptions

- **📦 Order Management**
  - Shopping cart functionality
  - Order creation and tracking
  - Order status management
  - Order history

- **💳 Payment Integration**
  - VNPay payment gateway integration
  - Payment status tracking
  - Order confirmation via email

- **📧 Email Notifications**
  - Order confirmation emails
  - Gmail OAuth2 integration
  - Configurable email templates

- **📊 Reporting**
  - Revenue reports using JasperReports
  - Order analytics
  - User activity tracking

- **⚙️ Workflow Automation**
  - BPMN-based order processing
  - Camunda workflow engine integration
  - Automated business process management

## 🛠️ Tech Stack

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.2.2** - Web framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database ORM
- **Camunda 7.20.0** - BPMN workflow engine
- **JWT (Nimbus Jose)** - Token management
- **Lombok** - Code generation
- **MapStruct** - Object mapping

### Database
- **MySQL** - Production database
- **H2** - Development/Testing database

### Build & Tools
- **Maven** - Build tool
- **JUnit & Spring Test** - Testing
- **TestContainers** - Integration testing
- **JaCoCo** - Code coverage
- **Spotless** - Code formatting

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.8+
- MySQL 8.0+ (for production)
- Git

## 🚀 Getting Started

### 1. Clone Repository
```bash
git clone https://github.com/vanloi0101/Vegetarian-Food-WebShop.git
cd Vegetarian-Food-WebShop/Vegetarian-Food-WebShop
```

### 2. Configure Environment Variables

Create application configuration (or set environment variables):

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vegetarian_food
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update

# OAuth2 Configuration
app:
  oauth2:
    client-id: ${GOOGLE_CLIENT_ID}
    client-secret: ${GOOGLE_CLIENT_SECRET}
    redirect-uri: http://localhost:8080/login/oauth2/code/google
  
  # Email Configuration
  mail:
    from: ${MAIL_FROM_ADDRESS}
    oauth-access-token: ${GOOGLE_MAIL_ACCESS_TOKEN}
```

### 3. Build Project
```bash
./mvnw clean install
```

### 4. Run Application
```bash
./mvnw spring-boot:run
```

Application will start at: `http://localhost:8080`

## 📚 API Documentation

### Authentication
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/logout` - User logout
- `POST /api/v1/auth/refresh-token` - Refresh JWT token

### Products
- `GET /api/v1/products` - List all products
- `GET /api/v1/products/{id}` - Get product details
- `POST /api/v1/products` - Create product (Admin only)
- `PUT /api/v1/products/{id}` - Update product (Admin only)
- `DELETE /api/v1/products/{id}` - Delete product (Admin only)

### Orders
- `GET /api/v1/orders` - List user orders
- `GET /api/v1/orders/{id}` - Get order details
- `POST /api/v1/orders` - Create order
- `PUT /api/v1/orders/{id}/status` - Update order status
- `DELETE /api/v1/orders/{id}` - Cancel order

### Payment
- `POST /api/v1/payments/vnpay/create` - Create VNPay payment
- `GET /api/v1/payments/vnpay/callback` - VNPay payment callback

### Users (Admin only)
- `GET /api/v1/users` - List all users
- `GET /api/v1/users/{id}` - Get user details
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

## 🧪 Testing

### Run All Tests
```bash
./mvnw test
```

### Run with Coverage
```bash
./mvnw clean test jacoco:report
```

Coverage report: `target/site/jacoco/index.html`

## 🐳 Docker Support

### Build Docker Image
```bash
docker build -t vegetarian-food-webshop:latest .
```

### Run Docker Container
```bash
docker run -p 8080:8080 \
  -e GOOGLE_CLIENT_ID=your_client_id \
  -e GOOGLE_CLIENT_SECRET=your_client_secret \
  -e MYSQL_URL=jdbc:mysql://host:3306/db \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=password \
  vegetarian-food-webshop:latest
```

## 📦 Project Structure

```
src/
├── main/
│   ├── java/com/devteria/identityservice/
│   │   ├── controller/          # REST endpoints
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── entity/              # JPA entities
│   │   ├── dto/                 # Data transfer objects
│   │   ├── configuration/       # Spring configuration
│   │   ├── security/            # Security configs
│   │   ├── exception/           # Exception handling
│   │   ├── mapper/              # MapStruct mappers
│   │   └── validator/           # Input validators
│   └── resources/
│       ├── application.yml      # Application config
│       └── process/             # BPMN files
└── test/                        # Unit & Integration tests
```

## 🔐 Security Features

- **JWT Authentication** - Stateless token-based authentication
- **OAuth2 Integration** - Google Sign-In support
- **Role-Based Access Control** - Fine-grained permissions
- **Password Encryption** - BCrypt hashing
- **CSRF Protection** - Spring Security defaults
- **Secure Headers** - HSTS, X-Frame-Options, etc.

## ⚠️ Environment Variables

Required environment variables:

```
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
MYSQL_URL=jdbc:mysql://localhost:3306/vegetarian_food
MYSQL_USER=root
MYSQL_PASSWORD=your_password
MAIL_FROM_ADDRESS=your_email@gmail.com
GOOGLE_MAIL_ACCESS_TOKEN=your_google_oauth_token
JWT_SECRET=your_jwt_secret_key
```

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Loi Van**
- GitHub: [@vanloi0101](https://github.com/vanloi0101)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

For support, please open an issue on the GitHub repository or contact the maintainer.

---

**Last Updated:** June 2026