# 📘 AI Study Planner

![Made with Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-3DDC84?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

> **AI Study Planner** is a modern Android app built with **Jetpack Compose** and **Firebase AI Logic (Gemini API)** that helps students summarize YouTube videos, extract insights from PDFs, and auto-generate quizzes for smarter study sessions.  
> Designed with **Material 3 Expressions**, a clean **MVVM architecture**, and Firebase integrations.

---

## ✨ Features

### 🎉 Onboarding & Authentication
- Splash screen with login check.
- 3-step onboarding flow introducing app features.
- Google Sign-In powered by Credential Manager API.
- Firebase Authentication with App Check enabled.

### 🧠 AI-Powered Tools
- **AI Tip of the Day** → Daily tips with animated gradient refresh button.
- **Summarize YouTube Video** → Paste a link → AI summary with RGB animated borders + clipboard copy.
- **Summarize PDF** → Upload a PDF → Extract content → Summarize using Gemini via Firebase AI Logic.
- **Generate Quiz from PDF**:
  - Upload a PDF, extract text, auto-generate MCQs.
  - Progress bar for question navigation.
  - Smooth Material animations.
  - Final score + percentage + retake/exit options.

### 📂 Recents
- Tracks all previously uploaded files.
- Quick re-summarize or re-quiz directly from Recents.

### 👤 Profile & Settings
- Profile screen with Gmail picture, email, and verification status.
- Settings screen with:
  - Language selection
  - App version
  - Report an issue
- Sign-out option with Firebase integration.

---

## 🛠 Tech Stack

- **UI** → Jetpack Compose + Material 3 Expressions  
- **Architecture** → MVVM + ViewModel + StateFlow  
- **Navigation** → Navigation 3  
- **Storage** → DataStore (Preferences)  
- **Async** → Kotlin Coroutines + Flows  
- **Firebase**:
  - Authentication (Google Sign-In)
  - Firebase AI Logic (Gemini Free Tier)
  - App Check
  - Crashlytics (with action + error logs)
- **Other**:
  - Intents for file picking & sharing
  - BottomSheetScaffold
  - Custom expandable Floating Action Button (FAB)
  - Designed with Figma + Google Stitch

---

## 📱 Screens Overview

- **Onboarding** (3 steps, Google Sign-In on last step)  
- **Home** (AI Tip, Recents, FAB for features)  
- **Summarization** (YouTube, PDF)  
- **Quiz** (MCQ flow with animations)  
- **Profile** (user info + settings)  
- **Settings** (language, version, issues, sign-out)  

---

## ⚙️ Setup & Installation

### 1. Clone Repository
```bash
git clone https://github.com/YOUR-USERNAME/AI-Study-Planner.git
cd AI-Study-Planner
```


### 2. Firebase Setup

- Go to [Firebase Console](https://console.firebase.google.com/)  
- Create a new Firebase project.  
- Enable:
  - Authentication (Google Sign-In)
  - App Check
  - Firebase AI Logic
  - Crashlytics  
- Download `google-services.json` and place it in your **app module**.  
- **IMPORTANT** → Add this file to `.gitignore`.

---

### 3. API Key (Firebase AI Logic)

- The app uses Gemini Free Tier via Firebase AI Logic.  
- If quota expires → update your API key or create a new Firebase project.

---

### 4. Run the App

- Open in **Android Studio (Giraffe or newer)**.  
- Sync Gradle.  
- Run on device (**6.8-inch screen recommended**).  

---

## 📊 Crashlytics & Logging

- Every user action is logged, for example:

  ```kotlin
  FirebaseCrashlytics.getInstance().log("Button Clicked: Summarize")

- Crashes are automatically detected and reported.
- Non-crash logs trace user actions before bugs.

---

## 📱 Compatibility

- Best experience on **6.8-inch screens or larger**.
- Smaller screens may have minor misalignments.

---

## 🚨 Known Issues

- App may crash if Gemini Free Tier quota is exceeded.
- Requires API key refresh in Firebase AI Logic.

---

## 📦 Roadmap

- [ ] Add Dark Mode toggle
- [ ] Offline AI (ML Kit on-device)
- [ ] Export quiz results as PDF
- [ ] Push notifications for reminders

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!  
Fork the repo and submit a PR.

---

## 📜 License

This project is licensed under the **MIT License**.  
See the [LICENSE](./LICENSE) file for details.
