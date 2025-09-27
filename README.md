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
  - DataStore for storing recents pdf file

### 📂 Recents
- Tracks all previously uploaded files.
- Quick re-summarize or re-quiz directly from Recents.

### 👤 Profile & Settings
- Profile screen with Gmail picture, email, and verification status.
- Settings screen for all the necessary information
- Sign-out option with Firebase integration.

---

## 🛠 Tech Stack

- **UI** → Jetpack Compose + Material 3 Expressions  
- **Architecture** → MVVM + ViewModel + StateFlow  
- **Navigation** → Navigation 3  
- **Recents** → DataStore (Preferences)  in order to store recents pdfs
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


## Splash Screen

Our **animated Splash Screen** delivers an engaging first impression of **AI Study Planner**. It elegantly showcases the app title:

**AI - Study Planner**  
*“Learning Reinvented”*

The splash screen also handles smart navigation:

- If the user is **already logged in**, they are seamlessly redirected to the **Home Screen**.  
- If the user is **not logged in**, they are taken to the **Onboarding Screen**.  

This animated entry point sets the tone for an intuitive and modern learning experience right from the start.


![SplashScreen-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/7add203f-88df-491f-b954-a6c31a701a3f)

---
## 📱 Onboarding & Login Screens (Video 📽️)

Below is a quick preview of the onboarding flow combined with the login screen:

**Description:**  
The onboarding flow introduces new users to the main features of the AI Study Planner application.  
Once onboarding is complete, the login screen allows users to securely sign in and access their personalized study dashboard.  

- **Onboarding Screens:** Highlight app benefits and setup steps.  
- **Login Screen:** Provides secure authentication (email/password or third-party login).


// TODO File size is big so please do wait
---

## 🤖 AI Tip of the Day (Video 📽️)

This feature provides users with a fresh **AI-generated tip** every time the application is launched, powered by **Gemini AI**.  
The tips are designed to keep users motivated and improve their study habits with smart, personalized advice.

---
**Description:**  
- Displayed inside a **glowing animated RGB card** that continuously pulses with light.  
- Includes a **refresh button** with an RGB glow effect.  
- On pressing the refresh button, a **new AI tip is instantly generated**.  

![Tipoftheday-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/5048e68e-e1d3-4dad-87f6-77540a130899)


---

## ⚡ Expandable Floating Action Button (Video 📽️)

A floating action button that expands into quick options for generating summaries:


**Features:**  
- 📺 **Summarize with YouTube link**  
- 📄 **Summarize with PDFs**  
- 📝 **Generate summary with text input**  

This makes accessing AI-powered summarization tools faster and more convenient.


![ExpandableFAB](https://github.com/user-attachments/assets/6ffeb51d-b036-4295-908e-06afeb5b8df8)

---

## 📄 Summarize with PDF Upload  (Video 📽️)

Upload any PDF and get a concise AI-generated summary in seconds.


**Highlights:**  
- Quick PDF upload  
- Instant AI-powered summary  
- Saves time by extracting key insights

  
![PDFSummarization-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/a322743c-090f-4ae4-9da0-8592ac843b22)

---

## 📺 Summarize with YouTube

Paste a YouTube link and instantly get a clear AI-generated summary of the video.


**Highlights:**  
- Supports any public YouTube video  
- Fast, accurate AI summaries (only if transcribe is implemented) 
- Saves time by skipping long videos

  ![YTsummarizatoin-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/a7e87844-f173-4ae6-90cf-3b60b86e8098)


---

## 📝 Quiz from PDF Upload (Part one) (📽️ Video)

Upload a study PDF and let the app turn it into a quiz for active learning.


**Highlights:**  
- 📄 Upload study PDFs with ease  
- 🎞️ Animated progress screen (with PDF Loading effect) while processing  
- 📊 Quiz is displayed to the user for practice and revision

  
![uploadingPDFforquizz-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/23d3d001-39e2-4784-92f8-b1a954170142)

---

## 🧩 Interactive Quiz Experience (quizz part 2) ((📽️ video)

Take quizzes in a smooth, engaging way with animated transitions.


**Highlights:**  
- 📋 MCQ cards with clean design  
- 🎞️ Seamless animation when moving to the next question  
- ✅ MCQ selector with instant feedback feel  
- ⬅️ Previous & Next buttons for navigation  
- 🔄 Next button turns into **Submit** on the last question in order to submit question
- 📊 Shows quiz summary at the end

  
![DoingQuizzs](https://github.com/user-attachments/assets/709cae8b-46be-4aba-aaef-cb63aec9a2e7)

---

## 🎥 Quiz Completion & Retake Flow

After completing *The Canterville Ghost Quiz*, the app displays the **score percentage** and **correct answers count**.  
It provides two options:  

- 🔄 **Retake Quiz** → Starts the quiz again from the beginning.  
- 🏠 **Exit Quiz** → Returns to the home screen.


  ![Retake quizz](https://github.com/user-attachments/assets/ea070718-18d1-465d-a68b-b9f3772314c1)


---

## 📑 PDF Summarization from Recents

This demo shows how users can quickly work with files from the **Recents list**:

- 📂 Tap on a recent file (e.g., a PDF).  
- 📜 A modal bottom sheet appears with two options:  
  - ✨ **Summarize PDF** → Generates a concise summary of the document.  
  - 📝 **Generate Quiz** → Creates a quiz based on the PDF content.  

In this example, the PDF is uploaded and summarized instantly, making it easier for users to grasp key insights without reading the entire document.  


![Recents_PDFummary-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/6fe151fa-927c-445a-81c0-83c139c4392e)

---


## 📝 Generate Quiz from PDF (Recents)

This demo shows how users can create quizzes directly from the **Recents list**:

- 📂 Tap on a recent file (e.g., a PDF).  
- 📜 A modal bottom sheet appears with two options:  
  - ✨ **Summarize PDF** → Generates a concise summary of the document.  
  - 📝 **Generate Quiz** → Creates an interactive quiz based on the PDF content.  

In this example, the user selects **Generate Quiz**, and the app builds a quiz dynamically from the uploaded PDF, helping with active learning and quick revision.  

![generatemcqwithrecents-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/aa857f4b-9d48-4dbd-b9fd-fce17cbc8821)

---

## 🗑️ Clear All Recents

This demo showcases how users can manage their **Recents list** with ease:

- 📝 The Recents section displays recently opened PDF files.  
- 🗑️ By tapping on the **Clear Recents** button, the entire list is cleared instantly.  
- ⚡ This helps keep the interface clean and focused, removing clutter from previously opened documents.  


![Clear Recents](https://github.com/user-attachments/assets/8f1e389e-65c0-48c2-9d1b-88e7c493d145)

---

## 👤 Profile Screen

The profile screen provides quick access to user account details and actions:

- 🖼️ Displays the user's Google profile picture.  
- 📧 Shows the linked Gmail ID below the profile picture.  
- 🚪 Includes a **Log Out** button for convenient account management.
  
<img width="226" height="491" alt="Profile screen" src="https://github.com/user-attachments/assets/5e4a6e0a-d676-4634-928e-58aeac670427" />

---

## ⚙️ Settings Screen

The settings screen provides application-related information and account actions:

- ℹ️ Includes an **About** section with details about the app.  
- 🚪 Contains a **Sign Out** button.  
- ⚠️ On tapping **Sign Out**, an **alert dialog** appears to confirm the action and prevent accidental logouts.  
- 🔑 The settings screen is accessible from the **gear icon** on the top app bar of the Profile screen.
- ℹ️ Many more necessary information/app details   

![settings and log out](https://github.com/user-attachments/assets/06fb9ad2-b320-460a-a8a1-d995e25ebfcf)

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

- App may crash if Gemini Free Tier quota is exceeded (better generate your own API key).
- Requires API key refresh in Firebase AI Logic.

---
## The screenshot below shows the home screen of the AI Study Planner application

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!  

Fork the repo and submit a PR on my linkedn or Email.

---

## 📜 License

This project is licensed under the **MIT License**.  
See the [LICENSE](./LICENSE) file for details.
