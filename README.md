# FireNotes

FireNotes is a Firebase-backed Android note-taking app that makes it effortless to capture, sync, and revisit lightweight notes across devices. The app focuses on fast onboarding, distraction-free writing, and predictable cloud backups without the learning curve of heavyweight productivity suites.

---

## Project Snapshot
- **Type & platform:** Native Android mobile application (`minSdk 29`, `targetSdk 34`), packaged as an `.apk` (`app/release/app-release.apk`)
- **Primary audience:** Students, solo entrepreneurs, field workers, and anyone who needs a low-friction personal notes companion
- **Authentication:** Email/password with verification, password reset, and session persistence via Firebase Authentication
- **Storage:** Per-user secure collections stored in Cloud Firestore (`notes/{uid}/my_notes`)
- **UI layer:** XML layouts, Material Components, RecyclerView, FirebaseUI adapter, Lottie splash animation

---

## Table of Contents
1. [Project Type & Platforms](#project-type--platforms)
2. [Core Purpose & Features](#core-purpose--features)
3. [Market Position & Competition](#market-position--competition)
4. [Target Audience](#target-audience)
5. [User Pain Points Solved](#user-pain-points-solved)
6. [Technical Stack & Requirements](#technical-stack--requirements)
7. [Setup & Installation](#setup--installation)
8. [Usage Guide](#usage-guide)
9. [Development Status & Roadmap](#development-status--roadmap)
10. [Budget & Resource Considerations](#budget--resource-considerations)
11. [Screenshots & Demo](#screenshots--demo)
12. [Contributing](#contributing)
13. [License](#license)
14. [Contact & Support](#contact--support)

---

## Project Type & Platforms
- **Type:** Native mobile note-taking application
- **Supported OS:** Android 10 (API 29) and above; tested on emulators and arm64/armv7 physical devices
- **Packaging:** Gradle-based Android Studio project with release-ready signed APK output (`app/release`)
- **Future platform goals:** Evaluate Jetpack Compose and iOS Kotlin Multiplatform support once feature parity stabilizes

---

## Core Purpose & Features
FireNotes exists to provide a simple, trustworthy companion for quick note capture without the clutter of oversized productivity stacks.

### Primary goals
- Let users authenticate securely and own their encrypted cloud space
- Provide instant note capture/edit/delete with subtle affordances (date stamp, validation cues, responsive buttons)
- Keep the UI lightweight so it launches quickly, even on low-memory devices

### Current feature set
- **Guided onboarding:** Lottie-powered splash screen routes returning users directly to their notebook or to login/create-account screens
- **Account management:** Email/password registration, verification, login, and password reset flows with inline validation
- **Secure note storage:** Each note (title, body, timestamp) lives in a Firestore subcollection scoped to the authenticated user
- **Realtime lists:** `FirestoreRecyclerAdapter` streams updates into a RecyclerView so newly added/edited notes appear instantly
- **Editing experience:** Full-screen editor with autoset timestamps, enable/disable states to prevent accidental empty saves, delete confirmation
- **Session controls:** Overflow menu exposes logout, clearing listeners and returning to login
- **UI polish:** Custom fonts, Material buttons, and accessible contrast to keep reading and scanning effortless

---

## Market Position & Competition
| Competitor | Strengths | FireNotes differentiation |
|------------|-----------|---------------------------|
| Google Keep | Deep ecosystem integration, voice capture | FireNotes is privacy-first, Firebase-only, and easier to self-host or fork |
| Evernote | Rich formatting, document scanning | FireNotes intentionally avoids bloat, loads faster on budget phones, and is open to white-labeling |
| Simplenote | Cross-platform, tagging | FireNotes provides Firebase authentication, customizable Android-native UI, and offline-ready roadmap |
| Notion | Databases, collaboration | FireNotes focuses on the single-user jotting workflow with minimal cognitive overhead |

Positioning summary: FireNotes targets users who want ownership of their note data, quick setup, and the ability to extend/brand the codebase for specialized deployments (education, enterprises, agencies).

---

## Target Audience
- **Students & researchers:** Need class or lab snippets synced between campus devices with minimal distraction.
- **Entrepreneurs & freelancers:** Capture meeting minutes, ideas, and client reminders on the go without exposing data to large SaaS vendors.
- **Field & support teams:** Works offline-first (via Firestore caching) with eventual sync, fitting intermittent connectivity scenarios.
- **Developers & agencies:** Starting point for white-labeled note or checklist apps built atop Firebase infrastructure.

---

## User Pain Points Solved
- **Overwhelming UIs:** Competing tools often bury note creation behind multiple taps or cluttered dashboards. FireNotes opens directly into the note list with a prominent “new note” action.
- **Account setup friction:** Email/password flow includes inline validation and immediate verification prompts, reducing drop-off.
- **Data ownership ambiguity:** Notes are isolated per user UID in Firestore, making it easy to export or apply custom security rules.
- **Unreliable sync:** Firebase’s realtime listeners ensure edits propagate instantly without manual refreshing.
- **Accessibility gaps:** Consistent typography, high-contrast palette, and large touch targets improve usability for low-vision or fast-paced contexts.

---

## Technical Stack & Requirements
### Languages & frameworks
- Java 8 (Android Gradle Plugin)
- AndroidX AppCompat / Material Components
- Firebase Authentication & Cloud Firestore
- FirebaseUI Firestore for RecyclerView binding
- Lottie for lightweight animations

### Build & tooling
- Android Gradle Plugin via `gradlew`
- Gradle wrapper configured for JDK 17-compatible toolchain
- `compileSdk` **34**, `minSdk` **29**, `targetSdk` **34**

### Platform dependencies
- Google Play Services on target device/emulator
- Firebase project with Authentication and Firestore enabled
- `google-services.json` placed in `app/` with web API key + Android app ID

### Integrations & constraints
- **Realtime sync:** Depends on Firestore network availability; add offline persistence to widen compatibility (on roadmap).
- **Authentication:** Email/password only at present; social login providers can be added via Firebase Auth.
- **Notifications & widgets:** Not implemented yet; integration hooks exist through Android Services/WorkManager once needed.

---

## Setup & Installation
1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-org>/FireNotes.git
   cd FireNotes
   ```
2. **Open in Android Studio (Giraffe+ recommended)**  
   The IDE will detect the Gradle wrapper (`gradlew`) and sync dependencies automatically.
3. **Create a Firebase project**
   - Enable Authentication (Email/Password) and Cloud Firestore.
   - Download `google-services.json` and drop it in `app/`.
   - (Optional) Configure Firestore security rules to restrict each user to `notes/{uid}/my_notes/**`.
4. **Sync Gradle & build**
   - From Android Studio, run *Sync Project with Gradle Files*.
   - Use *Build > Make Project* to ensure dependencies resolve (Lottie, Firebase, Material Components).
5. **Run the app**
   - Select an emulator or device running Android 10+.
   - Click *Run* or deploy the bundled `app/release/app-release.apk`.

---

## Usage Guide
1. **Launch** the app to view the Lottie splash screen. Returning users are routed straight to their notebook; new users land on the login screen.
2. **Register** with an email and password. A verification email is sent automatically—verify before the first login.
3. **Sign in** to access the main notebook list populated dynamically from Firestore.
4. **Create or edit notes** via the floating action button. Title/content validation ensures empty notes are not saved accidentally.
5. **Delete** notes from the detail view; confirmations prevent unintended removals.
6. **Reset password** through the “Forgot Password” link, which emails a secure reset link.
7. **Log out** from the overflow menu once finished; the app clears listeners and returns to the login flow.

---

## Development Status & Roadmap
### Current stage
- Functioning MVP with authentication, CRUD notes, and cloud persistence
- UI/UX foundation in place (custom theming, fonts, animation)
- Manual QA on Android 12 emulator and Pixel hardware

### Planned roadmap
| Timeline | Goal |
|----------|------|
| Short-term (0-2 sprints) | Add note search, sorting filters, and empty-state illustrations |
| Mid-term (2-4 sprints) | Offline cache + conflict resolution, checklist/markdown formatting, biometric unlock |
| Long-term (4+ sprints) | Shareable notebooks, push reminders, Wear OS tiles, potential Jetpack Compose rewrite |

---

## Budget & Resource Considerations
- **Firebase tier:** Spark (free) accommodates initial usage; scale-up may require Blaze pay-as-you-go for higher Firestore reads/writes and Authentication MAUs.
- **Development resources:** 1 Android engineer + 1 designer for continued UX polish; occasional backend/Firebase specialist for security rules and monitoring.
- **Tooling costs:** Android Studio (free), optional Play Store listing fees, and design tooling (Figma/Adobe) if needed.
- **Infrastructure:** No custom servers; rely entirely on Firebase-managed services, keeping ops overhead minimal.

---

## Screenshots & Demo
- **Home list:** _Add `docs/images/home.png`_
- **Note editor:** _Add `docs/images/editor.png`_
- **Authentication flow:** _Add `docs/images/auth.png`_
- **Demo video:** _Link to Loom/YouTube walkthrough_

> Place your assets inside `docs/images/` and update the paths above. If you have an APK demo, link to `app/release/app-release.apk` or a public download mirror.

---

## Contributing
1. Fork the repository and create a feature branch: `git checkout -b feat/<feature-name>`
2. Follow the existing Java/Android code style (Android Studio default, Java 8).
3. Keep UI strings in `res/values/strings.xml` for localization.
4. Run lint/inspection before opening a pull request.
5. Submit a PR with a clear summary, screenshots for UI changes, and testing notes.

---

## License
Specify the license that applies to FireNotes (e.g., MIT, Apache 2.0, proprietary). Add or update the `LICENSE` file accordingly and reference it here.

---

## Contact & Support
- **Maintainer:** _Add name or team alias_
- **Email:** _contact@example.com_
- **Issues:** Use GitHub Issues (or your tracker of choice) for bugs, feature requests, and security reports.

Have feedback or ideas? Open an issue or start a discussion—FireNotes thrives on community input.
