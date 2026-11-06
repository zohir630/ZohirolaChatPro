# Zohirola

Zohirola is a messaging app for Android (Kotlin, MVVM, Hilt, Room, Firebase).

- Package: `com.zohirola.app`
- Supports Android 4.4 (API 19) and newer, on all phone and tablet CPU types
- Developer: [zohir630](https://github.com/zohir630)

## Features
- One-to-one chat and group chat
- Typing status, unread counts, message status (sent, delivered, seen)
- Text, voice, sticker and GIF messages, image attachments
- Notification actions for reply and mark as read
- Search users by name

## Setup (required for login and chat)
1. Create a Firebase project and add an Android app with package name `com.zohirola.app`.
2. Add the SHA-1 fingerprint of `app/zohirola.jks` to the Firebase app (needed for Phone Auth).
3. Replace `app/google-services.json` with the file from your Firebase console.
4. Enable Phone Auth, Cloud Firestore, Realtime Database and Storage in Firebase.
5. Set `SERVER_KEY` in `gradle.properties` for push notifications.

## Build
Use the **Build APK** GitHub Actions workflow, or run `./gradlew assembleDebug` (JDK 11).

## License
Based on the open-source LetsChat project, distributed under the MIT License. See [LICENSE](LICENSE); the license text and its copyright notice must be kept.
