<div align="center">

# Tech Events

### An Android application for discovering and tracking technology events, powered by an AI recommendation engine.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=flat-square&logo=android&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%7C%20Clean-22C55E?style=flat-square)
![Python](https://img.shields.io/badge/Backend-Python%20%7C%20FastAPI-FFD43B?style=flat-square&logo=python&logoColor=black)
![Machine Learning](https://img.shields.io/badge/AI-Machine%20Learning-FF6F00?style=flat-square&logo=tensorflow&logoColor=white)

</div>

---

## Overview

**Tech Events** is a native Android application designed to help users discover and track technology events. Beyond standard CRUD operations, the app features a custom **AI-powered Recommendation Engine** — integrated via a Python backend — that suggests relevant events based on semantic similarity and user context.

This project was built to demonstrate proficiency in modern Android development (Jetpack Compose, Coroutines, Clean Architecture) and cross-platform system integration with Machine Learning models.

---

## Features

- **Pagination & Filtering** — Smooth navigation through large datasets using Jetpack Paging 3, with real-time filtering by modality (Online/In-Person) and full-text search queries.
- **AI-Powered Recommendations** — Real-time event suggestions driven by a Python backend using **TF-IDF vectorization** and **Cosine Similarity**.
- **Modern UI/UX** — Fully declarative interface built with Jetpack Compose, including a custom Splash Screen, Type-Safe Navigation, and responsive carousels.
- **Graceful Degradation** — Core functionality and app stability are maintained even when the AI recommendation engine is unreachable.
- **Robust State Management** — Strict Unidirectional Data Flow (UDF) using Kotlin `StateFlow` and `Sealed Interfaces` to prevent inconsistent UI states.

---

## Architecture & Tech Stack

The Android client follows **Clean Architecture** principles to ensure separation of concerns, testability, and scalability.

### Android Client (Frontend)

| Layer | Technologies |
|---|---|
| **UI** | Jetpack Compose, Material Design 3, Navigation Compose (Type-Safe) |
| **Presentation** | MVVM, `MutableStateFlow`, `Sealed Interfaces` (Loading / Success / Error) |
| **Domain** | Pure Kotlin business logic, Repository interfaces (Inversion of Control) |
| **Data** | Repository implementations, Retrofit2, OkHttp3 |
| **Concurrency** | Kotlin Coroutines & Flows — parallel execution via `async/await` to fetch event details and AI recommendations simultaneously |

### Recommendation Engine (Backend)

| Component | Technologies |
|---|---|
| **Framework** | FastAPI / Uvicorn |
| **Machine Learning** | `scikit-learn` — TF-IDF feature extraction, Cosine Similarity |
| **Data Processing** | `pandas` — dataset processing and vectorization |

---

## Screenshots

<p align="center">
  <img src="link_to_splash_screen.png" width="250" alt="Splash Screen"/>
  <img src="link_to_events_list.png" width="250" alt="Events List"/>
  <img src="link_to_event_details.png" width="250" alt="Event Details & AI Recommendations"/>
</p>



---

## Getting Started

To run this project locally, you need to start both the Python recommendation engine and the Android client.

### Prerequisites

- Python 3.9+
- Android Studio (latest stable)
- Android device or emulator (API 26+)

### 1. Backend — AI Recommendation Engine

```bash
cd backend

# Create and activate a virtual environment
python -m venv venv
source venv/bin/activate       # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Start the server
uvicorn main:app --reload
```

The API will be available at `http://localhost:8000`.

> You can explore the auto-generated interactive docs at `http://localhost:8000/docs`.

### 2. Android Client

1. Open the project in **Android Studio**.
2. Navigate to the `ApiClient` configuration (or your network module).
3. Set the `BASE_URL` to your local machine's address:
   - **Android Emulator:** `http://10.0.2.2:8000/`
   - **Physical device:** use your machine's local IP (e.g., `http://192.168.x.x:8000/`)
4. Sync Gradle and run the application.

---

## Author

**Jessica Cafezeiro**
Software Engineer bridging Mobile Development and Data Science.
