# Crypto Portfolio Application

A Java-based application for managing and tracking a portfolio of cryptocurrency assets, including stocks and options. The application simulates real-time market data and calculates portfolio values.

## Features

- Real-time market data simulation with configurable parameters
- Portfolio position tracking and management
- Support for multiple security types:
  - Stocks (equity positions)
  - Call Options (with strike price and maturity)
  - Put Options (with strike price and maturity)
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

## Quick Start
### Prerequisites
- Java 8 JDK
- Gradle 7.0+
- Git
### Building and Running
1. Clone the repository:
```bash
git clone <repository-url>
cd /Users/nickhu/dev/java/cryptoportfolio
 ```

2. Build the project:
```bash
./gradlew clean build
 ```

3. Run the application:
```bash
./gradlew run
 ``