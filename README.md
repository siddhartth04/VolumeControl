# Volume Control for Redmi Note 8 Pro

A small Android app that replaces physical volume-button use with on-screen controls.

## Features
- Media volume slider
- Ring volume slider
- Alarm volume slider
- Media volume +/- buttons
- No root required
- No internet permission
- Compatible with Android 6.0+ (API 23+)

## Build with GitHub Actions

1. Create a GitHub repository.
2. Upload the **contents** of this project to the repository (not the ZIP itself).
3. Push/commit to the `main` branch.
4. Open the repository's **Actions** tab.
5. Open **Build Android APK**.
6. Wait for the workflow to finish successfully.
7. Open the completed workflow run.
8. Under **Artifacts**, download `VolumeControl-debug`.
9. Extract the downloaded artifact and install `app-debug.apk` on your Redmi Note 8 Pro.

You can also manually start the workflow from:
Actions -> Build Android APK -> Run workflow.

## GitHub Actions file

The workflow is located at:

`.github/workflows/build-apk.yml`

It installs Android SDK components, builds the debug APK, and uploads the APK as a GitHub Actions artifact.

## Important

Android does not allow ordinary apps to insert a custom slider directly inside Xiaomi's Quick Settings shade. A future version can add a floating overlay or notification control.
