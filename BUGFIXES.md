# Bug Fixes

## Issues Fixed

### 1. ✅ Lint Error: Missing super.onBackPressed()

**Problem:**
```
Error: Overriding method should call super.onBackPressed [MissingSuperCall]
```

**Root Cause:**
The `onBackPressed()` method is deprecated in modern Android and either needs to call `super.onBackPressed()` or be replaced with the modern `OnBackPressedCallback` API.

**Solution:**
Replaced deprecated `onBackPressed()` with modern `OnBackPressedCallback`:

**Before:**
```kotlin
override fun onBackPressed() {
    showNewGameDialog()
}
```

**After:**
```kotlin
private fun setupBackPressHandler() {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showNewGameDialog()
        }
    })
}
```

**Files Changed:**
- `GameActivity.kt` - Added import, removed deprecated method, added modern handler

---

### 2. ✅ Dark Mode: Invisible Text on Round History

**Problem:**
In dark mode, the round history cards had white text on light gray background, making them unreadable.

**Root Cause:**
- Card backgrounds were hardcoded to `@color/light_gray` (light color)
- Text colors weren't using theme-aware colors
- No dark mode color variants defined

**Solution:**
1. Created dark mode color values
2. Added theme-aware text colors to layouts

**Files Created:**
- `values-night/colors.xml` - Dark mode color overrides
  - `light_gray`: Changed from `#FFF5F5F5` → `#FF2C2C2C` (dark gray)
  - `medium_gray`: Changed from `#FFE0E0E0` → `#FF404040` (darker gray)

- `values-night/themes.xml` - Dark mode theme

**Files Modified:**
- `item_round_history.xml` - Added `textColor="?android:attr/textColorPrimary"` to round number
- `item_player_round_score.xml` - Added `textColor="?android:attr/textColorPrimary"` to all text views

**How It Works:**
- Light mode: Uses `values/colors.xml` (light gray backgrounds, dark text)
- Dark mode: Uses `values-night/colors.xml` (dark gray backgrounds, light text)
- Text colors use `?android:attr/textColorPrimary` which automatically switches:
  - Light mode: Dark text (#000000)
  - Dark mode: Light text (#FFFFFF)

---

## Testing

### GitHub Actions Build
The build should now pass all lint checks:
```bash
./gradlew lintDebug
```

### Dark Mode Test
1. Run the app
2. Play a few rounds to create history
3. Go to phone Settings → Display → Dark theme (ON)
4. Switch back to app
5. Round history text should now be visible!

### Back Button Test
1. In game screen, press back button
2. "New Game" dialog should appear
3. Behavior unchanged, just using modern API

---

## Benefits

### Modern Back Handling
- ✅ No deprecation warnings
- ✅ Compatible with Android's predictive back gestures (Android 13+)
- ✅ More flexible (can enable/disable dynamically)
- ✅ Passes lint checks

### Dark Mode Support
- ✅ Text readable in both light and dark modes
- ✅ Follows system theme automatically
- ✅ Professional appearance
- ✅ Better battery life on OLED screens (dark mode)

---

## What Changed Per File

| File | Change | Reason |
|------|--------|--------|
| `GameActivity.kt` | Replaced `onBackPressed()` | Fix lint error, use modern API |
| `values-night/colors.xml` | **NEW** - Dark colors | Support dark mode |
| `values-night/themes.xml` | **NEW** - Dark theme | Material dark theme |
| `item_round_history.xml` | Added `textColor` attr | Make text visible in dark mode |
| `item_player_round_score.xml` | Added `textColor` attrs | Make text visible in dark mode |

---

## Future Improvements

These fixes lay the groundwork for:
- [ ] Fully themed dark mode (could customize all colors)
- [ ] User preference for theme (not just system default)
- [ ] Dynamic color support (Material You on Android 12+)
- [ ] Smooth theme transitions

---

## Deployment

Both fixes are now live in the code. When you:
1. **Push to GitHub** → Actions will build successfully
2. **Build locally** → No lint warnings
3. **Run app** → Back button works, dark mode works

Everything is ready! 🎉
