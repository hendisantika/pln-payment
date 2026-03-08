# PLN Payment System

A full-stack PLN (Perusahaan Listrik Negara) electricity bill payment application built with Spring Boot and Next.js, integrated with the IAK (mobilepulsa) API for bill inquiry and payment processing.

## Tech Stack

**Backend:**
- Java 25, Spring Boot 4.0.3
- Spring Data JPA, MySQL
- Lombok, Bean Validation

**Frontend:**
- Next.js 16.1.6, React 19, TypeScript
- CSS custom properties for theming

## Features

- PLN bill inquiry (postpaid & prepaid)
- Bill payment processing with transaction tracking
- Payment history with customer filtering
- IAK API integration with mock data fallback
- Responsive UI with multi-step payment flow

## Prerequisites

- Java 25+
- Node.js 18+
- MySQL 8+

## Getting Started

### 1. Database Setup

```sql
CREATE DATABASE pln_payment;
```

The schema is auto-created by Hibernate, or you can run `backend/src/main/resources/schema.sql` manually.

### 2. Backend

```bash
cd backend
```

Configure environment variables for IAK API (optional):

```bash
export IAK_USERNAME=your_username
export IAK_API_KEY=your_api_key
```

Run the application:

```bash
./mvnw spring-boot:run
```

The backend starts on http://localhost:8080.

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts on http://localhost:3000 and proxies API requests to the backend.

## API Endpoints

| Method | Endpoint             | Description          |
|--------|----------------------|----------------------|
| POST   | `/api/pln/inquiry`   | Check PLN bill       |
| POST   | `/api/pln/pay`       | Process payment      |
| GET    | `/api/pln/history`   | Get payment history  |

### Inquiry Request

```json
{
  "customerId": "1234567890",
  "productCode": "PLNPOSTPAID"
}
```

### Payment Request

```json
{
  "billId": 1,
  "customerId": "1234567890"
}
```

## Project Structure

```
├── backend/
│   └── src/main/java/com/hendisantika/pln/
│       ├── controller/     # REST endpoints
│       ├── service/        # Business logic & IAK integration
│       ├── entity/         # JPA entities
│       ├── repository/     # Data access
│       ├── dto/            # Request/response objects
│       ├── enums/          # Bill status (UNPAID, PENDING, PAID, FAILED)
│       └── config/         # CORS, IAK config, exception handling
├── frontend/
│   └── src/
│       ├── app/            # Next.js pages (home, history)
│       ├── components/     # InquiryForm, BillDetail, PaymentResult
│       └── lib/            # API client & TypeScript interfaces
```

## Configuration

Backend configuration is in `backend/src/main/resources/application.yml`:

| Property           | Default                                    |
|--------------------|--------------------------------------------|
| Server port        | 8080                                       |
| Database URL       | jdbc:mysql://localhost:3306/pln_payment     |
| IAK API base URL   | https://testprepaid.mobilepulsa.net         |
| CORS allowed origin| http://localhost:3000                       |

## License

This project is for educational and demonstration purposes.
