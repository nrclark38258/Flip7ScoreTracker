# Flip7ScoreTracker

Kotlin Android app for tracking scores in the card game Flip 7. No backend or database — all game state is in-memory and resets on app restart / new game.

## About the developer

The maintainer is knowledgeable in Python development but new to Kotlin and Android. When explaining Kotlin/Android-specific code or patterns, draw Python analogies where helpful (data class ~ `@dataclass`, nullable types ~ `Optional`, lambda syntax differences). Don't over-explain general programming concepts — only the Android/Kotlin-specific parts are likely unfamiliar.

## Stack

Kotlin, Gradle Kotlin DSL, Material Design Components, ViewBinding. minSdk 24 / targetSdk 34 / compileSdk 34.

Build locally (Windows): `./gradlew.bat assembleDebug --console=plain` (or `compileDebugKotlin` for a quick compile check).

## Source layout

`app/src/main/java/com/flip7/scoretracker/`:
- `MainActivity.kt` — player setup screen
- `GameActivity.kt` — main game screen (round input, history, win dialog)
- `Player.kt` — player model (name, totalScore, roundScores, `hasWon()` / `getPointsToWin()`)
- `GameState.kt` — owns players + rounds; `checkForWinner()`, `updateRound()` + `recalculateTotals()` (replays all rounds from scratch after an edit)
- `Round.kt` — per-round data keyed by player name: `scores`, `cardSelections`, `manualOverrides`
- `CardSelection.kt` — `ModifierCard` enum (+2, +4, +6, +8, +10, x2) and `CardSelection.computeTotal()`
- `CardKeypadBottomSheet.kt` — score entry sheet: card buttons plus a manual override field
- Adapters: `PlayerSetupAdapter`, `GameScoreAdapter`, `RoundHistoryAdapter`, `PlayerRoundScoreAdapter`, `EditScoreAdapter`

## Game rules as implemented

- **Round scoring:** sum of number cards, doubled if x2 is held, plus flat modifier bonuses, plus 15 for collecting 7 number cards.
- **Manual override** wins over card selections when both are present, but the card selection is kept so it can still be reviewed/edited. Both round-trip through round-history editing.
- **Win rule:** first player to reach 200+ total wins; if several cross 200 in the same round, highest total wins. The 200 threshold is hardcoded in both `Player.kt` and `GameState.kt`.

## Features

Implemented: player setup, card-based round scoring with manual override, automatic win detection, scrollable round history with running totals, editing any past round (full recalculation + winner re-check), dark mode (`values-night/`), `OnBackPressedCallback` back handling.

Ideas not yet implemented: persisted game history, undo last round, per-player stats, CSV export, multiple game profiles, configurable win threshold.

## CI/CD

`.github/workflows/android-build.yml` builds on push to `main`/`develop`; on `main` it also creates a GitHub Release. `versionName = "1.0.$GITHUB_RUN_NUMBER"` (set in `app/build.gradle.kts`), and APKs are named `Flip7ScoreTracker-{variant}-v{versionName}.apk`. The **debug** APK is what gets released, to avoid needing a release signing config.

### Known issue: release APKs can't update over each other

Installing a new release over an old one fails with "App not installed" (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`). Cause: no explicit `signingConfig` for the debug build type, and each CI run happens on a fresh VM, so Gradle generates a new random debug keystore every time — every release is signed with a different certificate.

Current workaround: uninstall, then reinstall. Fix (deferred): commit a stable debug keystore (or cache `~/.android/debug.keystore` in the workflow) and wire it into `app/build.gradle.kts` via an explicit `signingConfig`.

## Repo docs are stale

README.md, PROJECT_SUMMARY.md, BUGFIXES.md, CHANGES_SUMMARY.md, ROUND_HISTORY_FEATURE.md, DEVELOPMENT_GUIDE.md, QUICKSTART.md and ICONS_NOTE.md lag behind the code (e.g. README lists round history/editing as a future enhancement). Verify against source rather than trusting their feature lists.
