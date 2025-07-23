# Teya Home - iTunes Top Albums App

## Overview

Your task is to build a mobile application that lists the top 100 albums using the iTunes RSS feed API.

The app should display a scrollable list of albums and allow users to view more details about each album. The goal is to demonstrate your ability to build a well-structured app following good practices.

We believe this assignment can be completed in approximately 4 hours. When submitting, please let us know how much time you spent on the task. Spending more than the recommended time is fine; just let us know.

You may use any of the following technologies to implement the app: Flutter, Kotlin (Android), Swift (iOS), Kotlin Multiplatform (KMP), or React Native.

## Requirements

### API Integration
- Use the iTunes Top Albums API: `https://itunes.apple.com/us/rss/topalbums/limit=100/json`
- This API is free and does not require an API key—you can query it without any authentication

### Core Features
- Display a list of albums with at least:
  - Album name
  - Album artwork (image is required)
  - Artist name
- Tapping on a list item should open a detail screen with more information about the album
- You can include any relevant data available in the API, such as genre, release date, etc.

### Testing
- Please include at least one test: unit, widget or integration

## What We Will Evaluate

- **Code organisation and readability**
- **App architecture**
- **Error handling and user feedback**
- **Reusability**
- **API integration and data management**
- **Completeness (does everything work?)**
- **Best practices**

## Current Implementation Status

This project is implemented using **Kotlin (Android)** with the following architecture:

### Technology Stack
- **Language**: Kotlin
- **Architecture**: MVI (Model-View-Intent) with custom delegate adapter pattern
- **UI**: Android Views with ViewBinding
- **Navigation**: Android Navigation Component
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14)