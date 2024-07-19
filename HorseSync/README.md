This is a Kotlin Multiplatform project targeting Android, iOS, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

Create all of the base screens for the KMP app that im creating for android only, currently. I am using the Kotlin Multiplatform NavHost library for the navigation between screens. For the dependency injection i am using koin. The app is for a horse riding school, where the users (riders) will create their accounts and have their available lessons, and will be able to schedule their monthly lessons and have the calendar for their lessons. They will also be able to check if they have already paid the lessons they have done. There will be an admin mode where the teachers will be able to check who paid and get their next lesson and the monthly calendar with all of the lessons scheduled. They will be the ones with the power to accept the students shceduling requests.