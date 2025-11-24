# Flip 7 Score Tracker

An Android app for tracking scores during the card game Flip 7. Perfect for game nights with friends and family!

## Features

- **Player Setup**: Add any number of players before starting the game
- **Live Score Tracking**: Enter scores for each round and see totals update automatically
- **Points to Win Display**: Shows remaining points needed to reach 200 for each player
- **Automatic Win Detection**:
  - First player to reach 200+ points wins
  - If multiple players exceed 200 in the same round, the highest score wins
- **Dynamic Rounds**: Add as many rounds as needed
- **Game Management**: Reset current game or start a new game with different players

## Game Rules (Flip 7)

1. Any number of players can play
2. Each round, players receive a score
3. Scores accumulate across all rounds
4. First player to reach 200 or more points wins
5. Exception: If multiple players go over 200 in the same round, the player with the highest total wins

## Screenshots

### Setup Screen
Add player names before starting the game. Requires at least 2 players.

### Game Screen
- Current round number displayed at top
- Table showing: Player Name | Round Score Input | Total Score | Points to 200
- Submit button to record the round and advance
- New Game button to reset or start fresh

## Setup Instructions

### Prerequisites

1. **Install Java Development Kit (JDK) 17**
   - Download from [Adoptium](https://adoptium.net/temurin/releases/?version=17)
   - Install and verify: `java -version`

2. **Install Android Studio**
   - Download from [developer.android.com/studio](https://developer.android.com/studio)
   - Install with default settings
   - Launch Android Studio and complete the setup wizard

3. **Install Android SDK**
   - Android Studio will prompt you to install the Android SDK
   - Install SDK Platform for API Level 34 (Android 14)
   - Install Android SDK Build-Tools 34.0.0

### Running the App

#### Option 1: Using Android Studio (Recommended for Development)

1. **Clone or Download this repository**
   ```bash
   git clone <your-repo-url>
   cd Flip7ScoreTracker
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Click "Open" and select the `Flip7ScoreTracker` folder
   - Wait for Gradle sync to complete (may take a few minutes)

3. **Run on Emulator**
   - Click "Device Manager" in Android Studio
   - Create a new virtual device (e.g., Pixel 6, API 34)
   - Click the green "Run" button (or press Shift+F10)

4. **Run on Physical Device**
   - Enable Developer Options on your Android device:
     - Go to Settings > About Phone
     - Tap "Build Number" 7 times
   - Enable USB Debugging in Developer Options
   - Connect device via USB
   - Select your device from the device dropdown
   - Click the green "Run" button

#### Option 2: Using Command Line

1. **Build the APK**
   ```bash
   cd Flip7ScoreTracker
   ./gradlew assembleDebug
   ```

2. **Install on Connected Device**
   ```bash
   ./gradlew installDebug
   ```

3. **Find the APK**
   - Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
   - Release APK: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Building for Release

To create a release build:

```bash
./gradlew assembleRelease
```

The unsigned APK will be at: `app/build/outputs/apk/release/app-release-unsigned.apk`

Note: For production releases, you should sign your APK. See [Android's documentation](https://developer.android.com/studio/publish/app-signing) for details.

## GitHub Actions CI/CD

This project includes a GitHub Actions workflow that automatically:

1. **On every push to `main` or `develop`**:
   - Builds the app
   - Runs tests
   - Creates debug and release APKs
   - Uploads APKs as artifacts

2. **On push to `main` branch**:
   - Creates a GitHub release
   - Attaches the APK to the release
   - Tags the release with version number

### Setting Up GitHub Actions

1. **Push your code to GitHub**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin <your-github-repo-url>
   git push -u origin main
   ```

2. **Enable Actions**
   - Go to your repository on GitHub
   - Click the "Actions" tab
   - GitHub Actions will automatically detect the workflow

3. **Download Built APKs**
   - After each push, go to the "Actions" tab
   - Click on the latest workflow run
   - Scroll down to "Artifacts" section
   - Download `app-debug` or `app-release-unsigned`

## Project Structure

```
Flip7ScoreTracker/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/flip7/scoretracker/
│   │   │   │   ├── MainActivity.kt          # Player setup screen
│   │   │   │   ├── GameActivity.kt          # Game tracking screen
│   │   │   │   ├── Player.kt                # Player data model
│   │   │   │   ├── GameState.kt             # Game state management
│   │   │   │   ├── PlayerSetupAdapter.kt    # RecyclerView adapter for setup
│   │   │   │   └── GameScoreAdapter.kt      # RecyclerView adapter for game
│   │   │   ├── res/
│   │   │   │   ├── layout/                  # XML layouts
│   │   │   │   ├── values/                  # Strings, colors, themes
│   │   │   │   └── ...
│   │   │   └── AndroidManifest.xml
│   │   └── ...
│   └── build.gradle.kts
├── gradle/
│   └── wrapper/
├── .github/
│   └── workflows/
│       └── android-build.yml               # GitHub Actions workflow
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Technology Stack

- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **UI**: Material Design Components
- **Build System**: Gradle with Kotlin DSL
- **View Binding**: Enabled

## Development Notes

### Why Kotlin?

Kotlin was chosen for this project because:
- More concise and readable than Java
- Null safety built into the language
- Similar syntax to Python (easier learning curve)
- Official language for Android development
- Modern language features (data classes, lambda expressions)

### Key Concepts for Python Developers

- **Data Classes**: Similar to Python dataclasses, automatically generate equals(), hashCode(), toString()
- **Null Safety**: Variables must explicitly allow null (`var name: String?`)
- **Lambda Expressions**: `{ item -> doSomething(item) }` similar to Python lambdas
- **Type Inference**: `val name = "John"` (compiler infers String type)

## Troubleshooting

### Gradle Sync Failed
- **Solution**: File > Invalidate Caches > Invalidate and Restart

### SDK Not Found
- **Solution**: File > Project Structure > SDK Location > Set Android SDK location

### Build Failed - Missing Dependencies
- **Solution**: Tools > SDK Manager > Install missing SDK platforms/tools

### Emulator Won't Start
- **Solution**:
  - Ensure virtualization is enabled in BIOS
  - Increase emulator RAM in AVD settings
  - Try a different emulator image (x86_64 recommended)

## Future Enhancements

- [ ] Save game history to local storage
- [ ] Dark mode support
- [ ] Customizable win condition (not just 200 points)
- [ ] Undo last round
- [ ] Player statistics
- [ ] Export game results
- [ ] Multiple game profiles

## Contributing

Feel free to submit issues, fork the repository, and create pull requests for any improvements.

## License

This project is open source and available for personal and educational use.

## Support

For issues or questions:
1. Check the Troubleshooting section above
2. Review Android Studio logs (Logcat)
3. Create an issue on GitHub

---

**Enjoy tracking your Flip 7 games!**
