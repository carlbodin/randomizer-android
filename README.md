<img src="docs/readme-assets/icon.png" alt="" width="96" align="right">

# Randomizer

Flip a coin, roll dice, pick from a list, split teams and more. A small Android app for everyday random decisions, with light and dark themes.

## Features

| Coin | Dice | Yatzy dice | Yes or no |
| :---: | :---: | :---: | :---: |
| <img src="docs/readme-assets/coin.png" alt="Coin flip" width="200"> | <img src="docs/readme-assets/dice.png" alt="Dice roll" width="200"> | <img src="docs/readme-assets/yatzy.png" alt="Yatzy dice with two held dice" width="200"> | <img src="docs/readme-assets/yes-no.png" alt="Yes or no answer" width="200"> |
| Flip heads or tails. | Roll a single die. | Roll five dice and tap dice to hold them between rolls. | Get a yes or no answer. |

| Categories | Team randomizer | Number generator |
| :---: | :---: | :---: |
| <img src="docs/readme-assets/categories.png" alt="Category pick" width="200"> | <img src="docs/readme-assets/teams.png" alt="Shuffled teams" width="200"> | <img src="docs/readme-assets/number.png" alt="Random number between 1 and 100" width="200"> |
| Pick one of your own options. Withdraw mode removes picks from the pool until it is reset. | Shuffle players into even teams. | Pick a whole number between a minimum and a maximum. |

Your categories, players and number range are remembered between sessions.

## Installation

Download `randomizer-debug.apk` from the latest [release](https://github.com/carlbodin/randomizer-android/releases) and open it on your Android device (Android 5.0 or newer). Allow "Install unknown apps" for the app you open the APK from. The APK can be deleted after installation.

## Building

Requires JDK 17 or newer and the Android SDK (platform 36).

```sh
./gradlew :app:assembleDebug        # app/build/outputs/apk/debug/randomizer-debug.apk
./gradlew :app:testDebugUnitTest    # unit tests
```

Every push to `main` builds the debug APK and updates the release above.
