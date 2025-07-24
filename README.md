# Teya Home - iTunes Top Albums App

## Overview
Android app displaying top 100 albums from iTunes RSS feed API.

## Tech Stack
- **Language**: Kotlin
- **Architecture**: MVI with custom delegate adapter
- **UI**: Android Views + ViewBinding
- **Navigation**: Navigation Component
- **Min SDK**: 24, Target SDK: 36

## Implementation Details

### Code Organisation & Readability
Clean Architecture with clear layer separation (data/domain/presentation). MVI pattern with predictable data flow through StateViewModel.

### App Architecture
MVI (Model-View-Intent) with custom StateViewModel and delegate adapter. Dependency injection with Hilt.

### Error Handling & User Feedback
Error handling via runCatching with ShowError/HideError events. User notifications through ShowAlertMessage. Connection monitoring.

### Reusability
Custom delegate adapter (DelegateListAdapter) for reusable list components. Base classes for unification. Core utilities.

### API Integration & Data Management
Retrofit for iTunes API, Room for local caching. Repository pattern with single data access point. Flow for reactive UI updates.

### Completeness
Full cycle: data loading → caching → display → detail screen → rating. Navigation, pull-to-refresh, loading states with shimmer effects.

### Best Practices
CLEAN, MVI, Unit tests with MockK, coroutines and Flow, ViewBinding, SOLID principles, modern Android components.

## Setup
1. Open in Android Studio
2. Sync Gradle
3. Run on device/emulator

## Missing (Would Add in Production)
- Pagination (iTunes API limitation)
- Project flavors (dev/staging/prod)
- Crashlytics logging
- Feature branch workflow (currently direct to master)