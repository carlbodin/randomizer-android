# AGENTS.md

Guide for coding agents working in this repository. Read [DESIGN.md](DESIGN.md) before changing code or UI.

## Project

- Repository/project name: `randomizer-android`. The user-facing app name is **Randomizer** (`app_name` in `strings.xml`); never show the `-android` suffix to users.
- Single-module Android app in Java, Views + XML layouts, no Kotlin, no Compose, no DI framework.
- `applicationId` and package are `carl.coding`. Do not change them: installed copies would stop receiving updates.
- minSdk 21, targetSdk 33, compileSdk 36. AGP 8.11, Gradle wrapper 8.14.3.

## Environment

- JDK 17–24 to run Gradle (CI uses Temurin 17). Gradle 8.14 does not run on JDK 25+. Point `JAVA_HOME` or `org.gradle.java.home` in `~/.gradle/gradle.properties` at a supported JDK.
- Android SDK with `platforms;android-36` and build-tools. Set `ANDROID_HOME`, or `sdk.dir` in `local.properties` (git-ignored).
- To verify on an emulator: `emulator` plus a system image, for example `system-images;android-34;google_apis;x86_64` with a `pixel_6` AVD.

## Commands

```sh
./gradlew :app:assembleDebug       # build app/build/outputs/apk/debug/randomizer-debug.apk
./gradlew :app:testDebugUnitTest   # JVM unit tests (app/src/test)
./gradlew :app:lintDebug           # must report 0 errors
adb install -r app/build/outputs/apk/debug/randomizer-debug.apk
adb shell am start -n carl.coding/.StartMenu
python3 scripts/generate_icon.py               # regenerate launcher icon, logo, README icon (needs Pillow)
scripts/capture_readme_screenshots.sh          # regenerate README screenshots (needs booted emulator + ImageMagick)
```

## Definition of done

1. `assembleDebug`, `testDebugUnitTest` and `lintDebug` pass with no lint errors.
2. Logic changes come with unit tests. Keep randomness and rules in plain Java classes that take a `java.util.Random` (see `RandomRange`, `Teams`), so they can be tested without Android.
3. UI changes are checked on an emulator in **both** light and dark mode (the moon/sun button on the start menu), and in landscape when the screen holds state.
4. If a feature's look changes, rerun `scripts/capture_readme_screenshots.sh`. If features are added or removed, update the README feature table and the script.
5. If you change launcher icon colors or shapes, edit `scripts/generate_icon.py` and rerun it. Never hand-edit its generated files (they say so in a header comment).

## Conventions

- All user-visible text lives in `res/values/strings.xml`. Format arguments use `%1$d` and counts use `plurals`.
- Reuse the styles in `res/values/styles.xml` and the color tokens in `res/values{,-night}/colors.xml`. Never hard-code colors in layouts or Java.
- Every screen extends `BaseActivity`: use its `handler` for delayed UI work (cleared in `onDestroy`), `vibrate(ms)` for haptics and `open(Class)` for navigation. Screen transitions come from the theme; don't call `overridePendingTransition`.
- New screens must be registered in `AndroidManifest.xml` with `android:exported="false"` and linked from the start menu.
- Persist user input through `PreferencesHelper` with a `KEY_*` constant in the owning activity. Keep transient results across rotation with `android:freezesText` or `onSaveInstanceState`.
- Intent extras are `EXTRA_*` constants on the receiving activity.

## Commits

- One concern per commit, with a concise imperative subject line.
- No AI or co-author attribution lines.
- Don't commit `local.properties`, build output or `__pycache__`.
- Pushing to `main` triggers `.github/workflows/android-debug-release.yml`, which publishes the debug APK to the `randomizer-android-debug-latest` prerelease.
