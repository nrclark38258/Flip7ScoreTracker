# Development Guide

## For Developers Looking to Enhance the App

### Current Architecture

The app uses a simple MVVM-like pattern without a formal ViewModel:

```
MainActivity (Setup) ──> GameActivity (Play)
     ↓                         ↓
PlayerSetupAdapter       GameScoreAdapter
     ↓                         ↓
  Player names            GameState + Players
```

### Code Organization

```
Presentation Layer (Activities & Adapters):
├── MainActivity.kt           - Player setup UI
├── GameActivity.kt           - Game play UI
├── PlayerSetupAdapter.kt     - Setup list management
└── GameScoreAdapter.kt       - Score table management

Data Layer (Models):
├── Player.kt                 - Player entity
└── GameState.kt             - Game state management
```

## Enhancement Ideas with Implementation Guide

### 1. Add Data Persistence (Save Games)

**Goal**: Save game history to view later

**Implementation**:
1. Add Room dependency to `app/build.gradle.kts`:
   ```kotlin
   implementation("androidx.room:room-runtime:2.6.1")
   implementation("androidx.room:room-ktx:2.6.1")
   kapt("androidx.room:room-compiler:2.6.1")
   ```

2. Create database entities:
   ```kotlin
   @Entity
   data class GameRecord(
       @PrimaryKey(autoGenerate = true) val id: Int = 0,
       val date: Long,
       val winnerName: String,
       val winnerScore: Int
   )
   ```

3. Create DAO and Database classes
4. Save game on completion in `GameActivity.kt`
5. Create new Activity to view history

**Difficulty**: Moderate (1-2 hours)

### 2. Undo Last Round

**Goal**: Allow users to undo the last submitted round

**Implementation**:
1. In `GameState.kt`, add:
   ```kotlin
   private var lastRoundScores: Map<Player, Int>? = null

   fun undoLastRound() {
       if (currentRound > 1 && lastRoundScores != null) {
           lastRoundScores?.forEach { (player, score) ->
               player.totalScore -= score
               player.roundScores.removeLast()
           }
           currentRound--
           isGameOver = false
           winner = null
       }
   }
   ```

2. In `GameActivity.kt`:
   - Add "Undo" button to layout
   - Call `gameState.undoLastRound()`
   - Update UI

**Difficulty**: Easy (30 minutes)

### 3. Dark Mode Support

**Goal**: Add dark theme option

**Implementation**:
1. Create `res/values-night/` directory
2. Add `colors.xml` and `themes.xml` for dark mode
3. Use dark-friendly colors:
   ```xml
   <!-- values-night/colors.xml -->
   <color name="purple_200">#FFBB86FC</color>
   <color name="background">#FF121212</color>
   ```

4. Test by changing device theme settings

**Difficulty**: Easy (20 minutes)

### 4. Customizable Win Condition

**Goal**: Let users set winning score (not just 200)

**Implementation**:
1. Add to `MainActivity.kt`:
   ```kotlin
   private var winningScore = 200

   // Add EditText in layout for custom score
   binding.winningScoreInput.setText("200")
   ```

2. Pass to `GameActivity`:
   ```kotlin
   intent.putExtra("winning_score", winningScore)
   ```

3. Update `Player.kt` and `GameState.kt` to use dynamic value

**Difficulty**: Easy (1 hour)

### 5. Player Statistics

**Goal**: Track wins, games played, average score

**Implementation**:
1. Create new data class:
   ```kotlin
   data class PlayerStats(
       val name: String,
       var gamesPlayed: Int = 0,
       var wins: Int = 0,
       var totalPointsScored: Int = 0
   ) {
       fun averageScore() = if (gamesPlayed > 0)
           totalPointsScored / gamesPlayed else 0
   }
   ```

2. Store in SharedPreferences or Room database
3. Create new Activity to display statistics
4. Update stats after each game

**Difficulty**: Moderate (2-3 hours)

### 6. Score Animation

**Goal**: Animate score changes for better UX

**Implementation**:
1. In `GameScoreAdapter.kt`:
   ```kotlin
   fun animateScoreChange(textView: TextView, newScore: Int) {
       val animator = ValueAnimator.ofInt(oldScore, newScore)
       animator.duration = 500
       animator.addUpdateListener {
           textView.text = it.animatedValue.toString()
       }
       animator.start()
   }
   ```

2. Call when updating scores after round submission

**Difficulty**: Easy (30 minutes)

### 7. Export to CSV

**Goal**: Export game results to spreadsheet

**Implementation**:
1. Add write permission to `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
   ```

2. Create CSV generator:
   ```kotlin
   fun exportToCsv(gameState: GameState): String {
       val csv = StringBuilder()
       csv.append("Player,Round,Score,Total\n")
       gameState.players.forEach { player ->
           player.roundScores.forEachIndexed { index, score ->
               csv.append("${player.name},${index+1},$score,${player.totalScore}\n")
           }
       }
       return csv.toString()
   }
   ```

3. Save to Downloads folder or share via Intent

**Difficulty**: Moderate (1-2 hours)

### 8. Sound Effects

**Goal**: Play sounds on win, round submit, etc.

**Implementation**:
1. Add sound files to `res/raw/` (win.mp3, submit.mp3)
2. Create SoundManager:
   ```kotlin
   class SoundManager(context: Context) {
       private val mediaPlayer = MediaPlayer()

       fun playWin() {
           mediaPlayer.reset()
           mediaPlayer.setDataSource(context, R.raw.win)
           mediaPlayer.prepare()
           mediaPlayer.start()
       }
   }
   ```

3. Play sounds at appropriate times

**Difficulty**: Easy (1 hour)

### 9. Multiple Game Profiles

**Goal**: Save different player groups as profiles

**Implementation**:
1. Create Profile data class
2. Store profiles in SharedPreferences or Room
3. Add "Load Profile" button in MainActivity
4. Show dialog to select from saved profiles

**Difficulty**: Moderate (2-3 hours)

### 10. Portrait/Landscape Support

**Goal**: Support both orientations

**Implementation**:
1. Create `res/layout-land/` directory
2. Copy layouts and optimize for landscape
3. Save/restore state in Activities:
   ```kotlin
   override fun onSaveInstanceState(outState: Bundle) {
       super.onSaveInstanceState(outState)
       outState.putParcelable("game_state", gameState)
   }
   ```

**Difficulty**: Moderate (2 hours)

## Testing

### Adding Unit Tests

1. Open `app/src/test/java/com/flip7/scoretracker/`
2. Create test file:
   ```kotlin
   class PlayerTest {
       @Test
       fun testWinCondition() {
           val player = Player("Test")
           player.addRoundScore(150)
           assertFalse(player.hasWon())
           player.addRoundScore(60)
           assertTrue(player.hasWon())
       }
   }
   ```

3. Run: `./gradlew test`

### Adding UI Tests

1. Open `app/src/androidTest/java/com/flip7/scoretracker/`
2. Create UI test using Espresso
3. Run: `./gradlew connectedAndroidTest`

## Performance Optimization

### Current Performance
- RecyclerView is efficient for any number of players
- No database queries (all in-memory)
- Minimal background work

### Future Optimizations
1. **Use ViewModel**: Survive configuration changes
2. **Use LiveData**: Reactive UI updates
3. **Lazy loading**: For game history (if added)
4. **ProGuard**: Enable in release builds for smaller APK

## Code Quality

### Recommended Additions

1. **Lint**: Run `./gradlew lint` to find issues
2. **Ktlint**: Format code consistently
3. **Detekt**: Static code analysis
4. **Code comments**: Add KDoc for public APIs

### Best Practices

1. **Keep Activities lightweight**: Move logic to ViewModels
2. **Use constants**: For magic numbers (like 200)
3. **Resource IDs**: Use meaningful names
4. **Error handling**: Add try-catch for edge cases

## Debugging Tips

### Logcat Filtering
```
adb logcat | grep "Flip7"
```

### Common Issues

1. **RecyclerView not updating**
   - Call `adapter.notifyDataSetChanged()`
   - Or use specific notify methods

2. **State lost on rotation**
   - Implement `onSaveInstanceState`
   - Or use ViewModel

3. **Layout issues**
   - Check XML for `match_parent` vs `wrap_content`
   - Use ConstraintLayout debugger in Android Studio

## Release Checklist

Before releasing a new version:

- [ ] Update `versionCode` and `versionName` in `app/build.gradle.kts`
- [ ] Test on multiple devices/emulators
- [ ] Test both orientations
- [ ] Test edge cases (0 players, 100 players, etc.)
- [ ] Run lint: `./gradlew lint`
- [ ] Build release APK: `./gradlew assembleRelease`
- [ ] Sign APK (see Android docs)
- [ ] Update README.md with new features
- [ ] Create Git tag: `git tag v1.1.0`
- [ ] Push to GitHub: `git push --tags`

## Resources

### Official Docs
- Android Developer Guide: https://developer.android.com/guide
- Kotlin Documentation: https://kotlinlang.org/docs/home.html

### Libraries to Consider
- **Room**: SQLite database
- **ViewModel + LiveData**: MVVM architecture
- **Coroutines**: Async operations
- **Navigation Component**: Multi-screen navigation
- **Material Components**: Advanced UI components
- **Gson/Kotlinx Serialization**: JSON parsing

### Community
- r/androiddev: Reddit community
- Android Developers: YouTube channel
- Stack Overflow: Q&A

---

Happy coding! Start with the easy enhancements and work your way up. Each feature teaches new Android concepts!
