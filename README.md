# Teya Home - iTunes Top Albums App

## Overview
Android app displaying top 100 albums from iTunes RSS feed API.

## Tech Stack
- **Language**: Kotlin
- **Architecture**: MVI with custom delegate adapter
- **UI**: Android Views + ViewBinding
- **Navigation**: Navigation Component
- **Min SDK**: 24, Target SDK: 36

## Features
- Album list with artwork, name, artist
- Album detail screen
- Rating system
- Shimmer loading states
- Unit tests

## Setup
1. Open in Android Studio
2. Sync Gradle
3. Run on device/emulator

## Missing (Would Add in Production)
- Pagination (iTunes API limitation)
- Project flavors (dev/staging/prod)
- Crashlytics logging
- Feature branch workflow (currently direct to master)