# Shelfie

<p align="center">
  <img
    src="docs/images/banner.png"
    width="100%"
    alt="Shelfie — read when you want, listen when you can't"
  />
</p>

<h3 align="center">Read when you want. Listen when you can't.</h3>

<p align="center">
  An offline-first Android reader for <strong>FB2</strong> and <strong>EPUB</strong>, built around one idea:
  <br/>
  <strong>reading and listening should feel like the same experience.</strong>
</p>

<p align="center">
  Kotlin · Jetpack Compose · TTS · Offline-first
</p>

---

# Your books. Your device. Your library.

Shelfie keeps the core reading experience local.

Import books from device storage, Android document providers, other applications, or ZIP archives.
Preview them before adding them to your library and keep your reading progress without requiring an
account or internet connection.

<p align="center">
  <img
    src="docs/screenshots/catalogue.png"
    width="390"
    alt="Shelfie book catalogue"
  />
</p>

The library is designed to stay simple: your books, your progress, and a quick way back into
reading.

---

# Reading that follows you

Reading does not always mean looking at a screen.

Shelfie lets you switch from visual reading to Text-to-Speech without treating it like a separate
audiobook session.

When playback starts, narration begins from the current reading position.
The spoken sentence is highlighted, the page follows automatically, and progress remains shared
between reading and listening.

<p align="center">
  <img
    src="docs/screenshots/reader-tts.png"
    width="390"
    alt="Shelfie reader with synchronized Text-to-Speech"
  />
</p>

Walk somewhere. Make coffee. Commute. Lock the phone.

Shelfie keeps reading.

---

# Keep listening outside the app

Playback is not tied to the reader screen.

Shelfie uses an Android foreground media service and media session integration so Text-to-Speech can
continue while the app is in the background.

System media controls expose playback directly from Android.

<p align="center">
  <img
    src="docs/screenshots/background-playback.png"
    width="390"
    alt="Shelfie background playback in Android system media controls"
  />
</p>

Reading progress continues to stay synchronized while the UI is gone.

---

# Make the page yours

A reader should adapt to the person reading it — not the other way around.

Typography and layout can be changed without leaving the book. Settings are applied immediately
while the current page remains visible.

<p align="center">
  <img
    src="docs/screenshots/reader-settings.png"
    width="390"
    alt="Shelfie reader appearance settings"
  />
</p>

Shelfie currently supports:

* font size and line height
* reader padding and spacing
* light, dark, and system themes
* Material You dynamic colors
* adjustable TTS playback speed
* sleep timer

---

# More than a reader UI

Shelfie started as a small personal reader, but the interesting part quickly became everything
happening underneath the page.

A single reading session coordinates document parsing, persistent progress, scrolling, foreground
playback, TTS sentence tracking, runtime settings, lifecycle changes, and background Android
services.

The goal is to make all of that complexity disappear for the reader.

---

## One progress model for reading and listening

Visual reading and TTS do not maintain two independent positions.

The same book state is used to:

* resume visual reading
* start narration from the visible text
* track spoken content
* automatically follow playback
* persist progress
* return control to the reader afterwards

This allows the transition between reading and listening to feel continuous instead of behaving like
two separate modes.

---

## Structured FB2 and EPUB parsing

Shelfie contains its own parsing layer for **FB2** and **EPUB**.

Source documents are converted into format-independent book structures instead of being mapped
directly to Compose UI.

This keeps document semantics separate from presentation and creates a foundation for richer
features such as:

* chapter navigation
* hierarchical document structure
* links and references
* richer text formatting
* improved TTS segmentation
* additional book formats

FB2 XML and EPUB XHTML eventually converge into the same internal representation, allowing the
reader to work independently of the original file format.

---

## Offline-first by design

Books, settings, and reading progress are stored locally.

The core application does not depend on a backend, user account, or network connection.

That makes offline support part of the architecture rather than a fallback mode.

---

## Android media integration

Text-to-Speech runs independently from the reader UI through a foreground service.

Playback state is exposed through Android media APIs, allowing narration to continue while Shelfie
is backgrounded and making playback controls available through the system UI.

---

# Built with Android in mind

Shelfie is written entirely in Kotlin and built around modern Android APIs.

### Core stack

**Kotlin**
**Jetpack Compose**
**Material 3 / Material You**
**Coroutines & Flow**
**MVI-style state management**
**Hilt + KSP**
**Room**
**DataStore**
**Navigation Compose**
**AndroidX Media / MediaSession**
**Android TextToSpeech**
**Jsoup**
**Kotlin Serialization**

Minimum Android SDK: **26**

---

# A real product, not a code sample

Shelfie is an actively developed personal project that I use myself.

It was not built specifically as a take-home assignment or portfolio mockup. The project grew from a
simple personal need into a place where I can explore product decisions, Android architecture,
document parsing, media playback, and interaction design in one application.

That also means it is intentionally unfinished.

Current development is focused on improving:

* FB2 and EPUB parsing
* document and chapter structure
* navigation inside books
* reader performance
* import workflows
* TTS behavior
* overall product polish

The codebase continues to evolve as those problems become clearer.

---

# Why I built Shelfie

I like software where technical decisions are visible in the final experience.

A parser should not matter to the reader — until a badly structured book suddenly works.

A foreground service should not matter — until the screen turns off and narration keeps going.

State management should not matter — until reading, scrolling, settings, playback, and persistence
all stay synchronized.

Shelfie is my way of building that kind of software:
**a product where engineering exists to make the experience feel simple.**

---

# Source code

This repository is publicly available for **portfolio and technical evaluation purposes**.

**All rights reserved.**

Commercial use, redistribution, or incorporation of the source code into other products requires
explicit permission from the author.