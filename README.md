# PaperLedger

A professional paper (simulated) stock trading Android application powered by the **Alpaca Broker API**. PaperLedger lets users create a brokerage account, link a bank via ACH, fund it, and trade US equities — all in a sandbox environment with no real money at risk.

---

## Screenshots


---

## Features

- **Onboarding** — 3-page horizontal pager introducing the app
- **Account creation** — 5-step sign-up flow (Contact → Identity → Disclosures → Documents → Trusted Contact) backed by the Alpaca Broker API
- **ACH bank linking** — link a bank account with routing and account numbers
- **Funding** — deposit funds from a linked ACH relationship into the trading account
- **Watchlists** — create, view, and delete watchlists; add assets to watchlists from the market watch screen
- **Market Watch (Assets)** — paginated, searchable list of all US equity assets
- **Trade screen** — live view of open positions and pending orders, with real-time polling every 5 seconds
- **Place order** — submit market or limit orders with optional take-profit and stop-loss brackets
- **Close positions / cancel orders** — close individual or all positions; cancel individual or all pending orders
- **Charts** — embedded TradingView chart via WebView with symbol switching
- **ACH Funding screen** — view all linked bank accounts and full transfer history
- **Settings** — navigate to funding details; dark mode toggle (UI only)
- **Light & Dark theme** — full Material3 dual-theme support (MetaTrader 5 inspired palette)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Architecture | Clean Architecture + MVI |
| DI | Dagger Hilt |
| Networking | Retrofit 2 + OkHttp 3 + Gson |
| Local DB | Room (v2.8.4) |
| Session storage | Jetpack DataStore (Preferences) |
| Navigation | Navigation Compose (v2.9.6) |
| Paging | Paging 3 + Paging Compose |
| Charts | TradingView Lightweight Charts (v3.8.0) via AndroidView WebView |
| Image icons | Material Icons Extended |
| Build system | Gradle KTS + KSP |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |
| Broker API | [Alpaca Broker API](https://alpaca.markets/docs/api-references/broker-api/) (Sandbox) |

---

# Architecture

PaperLedger is built using **Clean Architecture** and follows a **single-activity, MVI-based architecture**. The project is organized into clearly separated layers to ensure scalability, maintainability, and testability.

The application communicates with the Alpaca Broker API, persists data locally using Room, and exposes state to the UI through immutable MVI state models.

## Project Structure

```text
app/
└── src/main/java/com/paperledger/app
    │
    ├── core/
    │   ├── constants/
    │   ├── navigation/
    │   ├── ui/
    │   ├── events/
    │   ├── routes/
    │   └── util/
    │
    ├── data/
    │   ├── local/
    │   │   ├── datastore/
    │   │   ├── database/
    │   │   ├── dao/
    │   │   └── entities/
    │   │
    │   ├── remote/
    │   │   ├── api/
    │   │   └── dto/
    │   │
    │   ├── paging/
    │   ├── mappers/
    │   └── repository/
    │
    ├── domain/
    │   ├── model/
    │   ├── repository/
    │   └── usecase/
    │
    ├── di/
    │   ├── AppModule.kt
    │   ├── NetworkModule.kt
    │   └── RepositoryModule.kt
    │
    └── presentation/
        ├── MainActivity.kt
        ├── AuthViewModel.kt
        │
        ├── theme/
        │
        └── features/
            ├── onboarding/
            ├── signup/
            ├── funding/
            ├── watchlists/
            ├── assets/
            ├── trade/
            ├── charts/
            ├── settings/
            └── ...
```

## Layer Overview

### Presentation Layer

The presentation layer contains all UI-related code and is implemented using Jetpack Compose.

Each feature follows the MVI pattern and contains:

* Screen
* ViewModel
* State
* Event
* Effect (where applicable)

ViewModels expose immutable state objects that are observed by Compose screens.

### Domain Layer

The domain layer contains the application's business logic and acts as the bridge between presentation and data layers.

It includes:

* Domain models
* Repository contracts
* Use cases

Each user action is represented by a dedicated use case, ensuring business rules remain independent of framework-specific code.

### Data Layer

The data layer is responsible for retrieving, caching, and synchronizing data.

It contains:

* Retrofit API services
* Request and response DTOs
* Repository implementations
* Room database components
* DataStore session storage
* Paging sources
* Data mappers

Repository implementations coordinate communication between local and remote data sources and expose clean domain models to the rest of the application.

## Data Flow

PaperLedger follows a unidirectional data flow:

```text
User Action
    ↓
UI Event
    ↓
ViewModel
    ↓
Use Case
    ↓
Repository
    ↓
Remote API / Local Database
    ↓
Repository Result
    ↓
ViewModel State
    ↓
Compose UI
```

This architecture ensures a predictable state flow, easier debugging, and improved separation of concerns.

## Local-First Synchronization

For trading-related data such as:

* Positions
* Orders
* Watchlists
* Account information

repositories combine Room database streams with remote API refreshes to provide fast UI updates while maintaining consistency with the backend.

`distinctUntilChanged()` is used to prevent unnecessary recompositions and duplicate UI updates.

Open positions and pending orders are automatically refreshed every five seconds while the trade screen is active, providing near real-time account information without requiring manual refresh actions.

## Session Management

PaperLedger uses Jetpack DataStore to persist onboarding progress and account identifiers.

When the application launches, the stored values determine which screen should be displayed next:

```text
Onboarding
    ↓
Sign Up
    ↓
ACH Linking
    ↓
Funding
    ↓
Main Application
```

Completed steps are skipped automatically on subsequent launches, allowing users to resume directly from where they left off.

```
```


## Project Setup

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11
- An [Alpaca](https://alpaca.markets) account — sign up for free, then create a **Broker API** sandbox app to get your API key and secret

### 1. Clone the repository

```bash
git clone https://github.com/god-s-only/paperledger.git
cd PaperLedger
```

### 2. Add your API credentials

Create or edit `local.properties` in the project root and add:

```properties
ALPACA_API_KEY=your_alpaca_api_key_here
ALPACA_API_SECRET=your_alpaca_api_secret_here
```

> **Important:** `local.properties` is listed in `.gitignore`. Never commit your API credentials.

### 3. Sync and run

Open the project in Android Studio, let Gradle sync, then run on a device or emulator (API 24+).

---

## API Reference

All requests go to the **Alpaca Broker API sandbox**:
Base URL: https://broker-api.sandbox.alpaca.markets
Auth:     Basic <base64(API_KEY:API_SECRET)>

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/v1/accounts` | Create brokerage account |
| PATCH | `/v1/accounts/{id}` | Update account |
| GET | `/v1/accounts/{id}` | Get account info |
| POST | `/v1/accounts/{id}/ach_relationships` | Link bank account |
| GET | `/v1/accounts/{id}/ach_relationships` | List ACH relationships |
| POST | `/v1/accounts/{id}/transfers` | Request a fund transfer |
| GET | `/v1/accounts/{id}/transfers` | List transfers |
| GET | `/v1/assets` | List all assets |
| GET | `/v1/trading/accounts/{id}/watchlists` | Get watchlists |
| POST | `/v1/trading/accounts/{id}/watchlists` | Create watchlist |
| DELETE | `/v1/trading/accounts/{id}/watchlists/{wid}` | Delete watchlist |
| GET | `/v1/trading/accounts/{id}/positions` | Get open positions |
| DELETE | `/v1/trading/accounts/{id}/positions` | Close all positions |
| DELETE | `/v1/trading/accounts/{id}/positions/{symbol}` | Close one position |
| GET | `/v1/trading/accounts/{id}/orders` | Get pending orders |
| POST | `/v1/trading/accounts/{id}/orders` | Place an order |
| DELETE | `/v1/trading/accounts/{id}/orders` | Cancel all orders |
| DELETE | `/v1/trading/accounts/{id}/orders/{oid}` | Cancel one order |

---

## Known Limitations

- **Sandbox only** — the app is hardcoded to Alpaca's sandbox environment. No real money is ever involved.
- **US equities only** — asset filtering is set to `us_equity` class.
- **No sign-in** — once an account is created and tokens are in DataStore, the app auto-resumes. Clearing app data resets the flow to sign-up.
- **Client-side asset pagination** — the full asset list is fetched once from the API and paginated in-memory; there is no server-side cursor pagination for assets.
- **Onboarding illustrations** — the three onboarding pages use placeholder boxes; replace `PlaceholderImageComposable` in `OnboardingScreen.kt` with your own illustrations.
- **Dark mode toggle** — the Settings screen has a dark mode switch in the UI, but it is not yet wired to a persistent theme data store.

---
