# Quick Start Guide

## First-Time Setup (5-10 minutes)

### Step 1: Install Java JDK 17
1. Go to https://adoptium.net/temurin/releases/?version=17
2. Download the installer for your OS
3. Run the installer with default settings
4. Verify installation by opening terminal/command prompt:
   ```bash
   java -version
   ```
   Should show version 17.x.x

### Step 2: Install Android Studio
1. Go to https://developer.android.com/studio
2. Download Android Studio
3. Run the installer
4. Follow the setup wizard:
   - Choose "Standard" installation
   - Accept all licenses
   - Let it download Android SDK (takes ~5-10 minutes)

### Step 3: Open the Project
1. Launch Android Studio
2. Click "Open" (not "New Project")
3. Navigate to the `Flip7ScoreTracker` folder
4. Click "OK"
5. Wait for Gradle sync (progress bar at bottom)
   - First time may take 5-10 minutes to download dependencies
   - Be patient!

### Step 4: Run the App

#### Option A: Run on Emulator (Virtual Phone)
1. Click the device dropdown at the top (shows "No devices")
2. Click "Device Manager"
3. Click "Create Device"
4. Select "Pixel 6" > Next
5. Download a system image (recommend API 34, x86_64)
6. Click "Next" then "Finish"
7. Click the green "Run" button (▶️) at the top
8. App should launch in emulator!

#### Option B: Run on Your Android Phone
1. On your phone:
   - Go to Settings > About Phone
   - Tap "Build Number" 7 times (enables Developer Mode)
   - Go back to Settings > Developer Options
   - Enable "USB Debugging"
2. Connect phone to computer via USB
3. Accept the "Allow USB Debugging" prompt on your phone
4. In Android Studio, select your device from dropdown
5. Click the green "Run" button (▶️)
6. App should install and launch on your phone!

## Using the App

### Adding Players
1. Type player name in the text field
2. Click "Add Player"
3. Repeat for all players (minimum 2)
4. Click "Start Game"

### Playing a Game
1. For each round:
   - Enter each player's score in their row
   - Click "Submit Round"
2. Scores automatically update
3. Game announces winner when someone reaches 200+
4. Click "New Game" to reset or start over

## Building an APK to Share

Want to install on other phones without Android Studio?

```bash
cd Flip7ScoreTracker
./gradlew assembleDebug
```

The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

Copy this file to your phone and install it!

## Troubleshooting

### "Gradle sync failed"
- **Fix**: Click File > Invalidate Caches > Invalidate and Restart

### "SDK not found"
- **Fix**: File > Project Structure > SDK Location
- Set to where Android Studio installed the SDK (usually `~/Android/Sdk` or `C:\Users\YourName\AppData\Local\Android\Sdk`)

### Emulator won't start
- **Fix**: Tools > AVD Manager > Edit your device > Increase RAM to 2048 MB
- Or try creating a new emulator with different settings

### "Build failed - Could not resolve..."
- **Fix**: Make sure you have internet connection
- Gradle needs to download dependencies on first build

### App crashes on launch
- **Fix**: Check "Logcat" tab at bottom of Android Studio
- Look for red error messages
- Most common: missing SDK platform (install via SDK Manager)

## Getting Help

1. Check the [README.md](README.md) for detailed documentation
2. Review error messages in Android Studio's "Build" tab
3. Check "Logcat" for runtime errors
4. Search error messages online (Stack Overflow usually has answers)

## Next Steps

Once you have the app running:
- Try modifying the UI colors in `app/src/main/res/values/colors.xml`
- Change the app name in `app/src/main/res/values/strings.xml`
- Explore the Kotlin code to understand how it works

---

**That's it! You've built your first Android app!**
