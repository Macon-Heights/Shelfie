# Shelfie

### Read when you want. Listen when you can't.

Shelfie is an offline-first Android book reader that lets you seamlessly switch
between reading and listening while keeping a single shared reading progress.

Built with Kotlin and Jetpack Compose as an actively developed personal product.

<!--
HERO SCREENSHOT:
Reader with TTS currently playing.
Show highlighted sentence + player controls.

Save as:
docs/screenshots/reader-tts.png
-->

<p align="center">
  <img src="docs/screenshots/reader-tts.png" width="300" alt="Shelfie reader with TTS playback"/>
</p>

## ✨ What is Shelfie?

Shelfie treats reading and listening as two ways of consuming the same book,
rather than two separate experiences.

Start reading on screen, switch to Text-to-Speech when you are walking,
commuting or doing something else, and return to the same place when you
want to read again.

Books and reading progress are stored locally, so the core experience works
without an account, backend or internet connection.

## 📚 Features

- 📖 Comfortable FB2 reader
- 🔊 Built-in Text-to-Speech playback
- 🔄 Shared progress between reading and listening
- ✨ Sentence highlighting during TTS playback
- 📜 Automatic scrolling synchronized with speech
- 🎧 Background playback
- 🔔 Media notification with play / pause controls
- ⏱️ Sleep timer
- ⚡ Adjustable playback speed
- 💾 Automatic reading progress persistence
- 📂 Import from device storage and document providers such as Google Drive
- 📤 Open books shared from other Android applications
- 🗜️ Automatic FB2 extraction from ZIP archives
- 👀 Book preview before importing
- 🌗 Light, dark and system themes
- 🎨 Material You dynamic colors
- 🔤 Customizable reader font and text size

Currently, **FB2 is the first supported book format**.

## 📱 User experience

<!--
SCREENSHOTS:
1. Catalogue with several books and different reading progress
2. Import preview with cover + title + author + description
3. Reader settings displayed over the book

Save as:
docs/screenshots/catalogue.png
docs/screenshots/import-preview.png
docs/screenshots/reader-settings.png
-->

<p align="center">
  <img src="docs/screenshots/catalogue.png" width="30%" alt="Shelfie catalogue"/>
  &nbsp;
  <img src="docs/screenshots/import-preview.png" width="30%" alt="Shelfie book import preview"/>
  &nbsp;
  <img src="docs/screenshots/reader-settings.png" width="30%" alt="Shelfie reader settings"/>
</p>

The UI is built around keeping the reading experience uninterrupted.

Reader settings are applied immediately while the book remains visible,
loading states use skeleton animations, and importing a book includes a
preview step before it becomes part of the library.

## 🎧 Reading and listening are one experience

Shelfie maintains a single position for both visual reading and TTS playback.

When playback starts, Shelfie begins reading from the visible part of the book.
While TTS is active, the current sentence is highlighted and the reader
automatically follows the narration.

Stop listening and continue reading from the same progress.

<!--
SCREENSHOT:
Android notification shade while Shelfie is playing in background.
IMPORTANT: remove personal notifications first.

Save as:
docs/screenshots/background-playback.png
-->

<p align="center">
  <img src="docs/screenshots/background-playback.png" width="300" alt="Shelfie background playback notification"/>
</p>

Playback continues outside the reader through an Android foreground media
service, allowing the book to keep playing while Shelfie is in the background.

## 🛠 Tech stack

Shelfie is currently a single-module Android application with feature-oriented
code organization.

- **Kotlin**
- **Jetpack Compose**
- **Material 3 / Material You**
- **Coroutines & Flow**
- **MVI-style UI state management**
- **Hilt + KSP**
- **Room**
- **DataStore**
- **Navigation Compose**
- **AndroidX Media / MediaSession**
- **Android TextToSpeech**
- **Jsoup**
- **Kotlin Serialization**

The project currently targets modern Android while keeping the minimum SDK at 26.

## 🚧 Project status

Shelfie is currently in **pre-alpha and active development**.

It is already usable for importing, reading and listening to FB2 books, and I
use the application myself as part of the development process.

The project is intentionally developed iteratively: product behavior and user
experience are validated first, while architecture and infrastructure evolve
alongside the product.

Planned areas include support for more book formats and a broader social
reading experience.

## 🔒 Source code

This repository is publicly available for portfolio and evaluation purposes.

**All rights reserved.** Commercial use, redistribution, or incorporation of
the source code into other products requires explicit permission from the author.