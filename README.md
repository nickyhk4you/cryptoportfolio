# Crypto Portfolio Application

A Java-based application for managing and tracking a portfolio of cryptocurrency assets, including stocks and options. The application simulates real-time market data and calculates portfolio values using option pricing models.

## Features

- Real-time market data simulation with configurable parameters
- Portfolio position tracking and management
- Support for multiple security types:
  - Stocks (equity positions)
  - Call Options (with strike price and maturity)
  - Put Options (with strike price and maturity)
- Option pricing using Black-Scholes model
- Automatic Net Asset Value (NAV) calculation
- Position value tracking with real-time updates
- H2 in-memory database for security storage
- CSV-based position loading

## Technical Stack

- Java 8
- Spring Framework 5.3.9
- H2 Database
- Gradle Build Tool
- Google Guava Library
- Protocol Buffers (for data serialization)
- JUnit and Cucumber for testing

## Project Structure

- `src/main/java/com/cryptoportfolio/` - Main application code
  - `model/` - Security models (Stock, CallOption, PutOption)
  - `service/` - Business logic and services
  - `config/` - Application configuration
  - `exception/` - Custom exceptions
- `src/main/resources/` - Configuration files and data
  - `schema.sql` - Database schema definition
  - `data.sql` - Sample data for the database
  - `positions.csv` - Initial portfolio positions
- `src/test/` - Test code and resources
  - `java/com/cryptoportfolio/` - Test code
  - `resources/features/` - Cucumber feature files

## Quick Start

### Prerequisites
- Java 8 JDK
- Gradle 7.0+
- Git

### Building the Project
1. Clone the repository:
```bash
git clone <repository-url>
cd cryptoportfolio
```

2. Build the project:
```bash
./gradlew clean build
```

### Running the Application
Run the application with Gradle:
```bash
./gradlew run
```

The application will start and display portfolio updates in the console, showing:
- Market data updates for securities
- Option price calculations
- Portfolio NAV (Net Asset Value)
- Individual position values

### Running Tests

Run all tests (unit tests + integration tests):
```bash
./gradlew test
```

Run Cucumber tests specifically:
```bash
./gradlew cucumber
```

## Sample Output

When running the application, you'll see output similar to:

```
Market Data Update:
AAPL change to 114.04
TELSA change to 454.43

Portfolio Details:
symbol                           price             qty           value
AAPL-OCT-2020-110-C               9.22       -20000.00      -184400.00
AAPL                            114.04         1000.00       114040.00
AAPL-OCT-2020-110-P               4.63        20000.00        92600.00
TELSA-NOV-2020-400-C             66.18        10000.00       661800.00
TELSA                           454.43         -500.00      -227215.00
TELSA-DEC-2020-400-P             13.97       -10000.00      -139700.00
Total Portfolio Value: 317125.00
```

## Configuration

You can modify the application behavior by editing:

- `application.properties` - Configure database and market data parameters
- `schema.sql` - Adjust the database schema
- `positions.csv` - Change the initial portfolio positions

