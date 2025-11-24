# App Icons

The project includes a basic placeholder icon that displays a score grid/calculator symbol. This icon uses Android's adaptive icon system and will work on all modern Android devices.

## Current Icons

- **Adaptive Icons** (Android 8.0+): Located in `app/src/main/res/mipmap-anydpi-v26/`
  - Uses a purple background (#3700B3)
  - White grid/scorecard foreground icon
  - Automatically adapts to device icon shape (circle, square, rounded square, etc.)

## Customizing Icons (Optional)

If you want to create custom icons later:

### Option 1: Use Android Studio's Icon Generator (Easiest)
1. Right-click on `app` folder in Android Studio
2. Select `New > Image Asset`
3. Choose icon type: "Launcher Icons (Adaptive and Legacy)"
4. Upload your own image or use clipart
5. Adjust colors and padding
6. Click "Next" and "Finish"
7. Android Studio automatically generates all required sizes!

### Option 2: Online Icon Generator
1. Visit https://romannurik.github.io/AndroidAssetStudio/
2. Go to "Launcher Icon Generator"
3. Upload your image or use text
4. Download the generated icon pack
5. Extract and copy to `app/src/main/res/`

### Option 3: Manual Creation
Create PNG files for each density:
- mipmap-mdpi: 48x48 px
- mipmap-hdpi: 72x72 px
- mipmap-xhdpi: 96x96 px
- mipmap-xxhdpi: 144x144 px
- mipmap-xxxhdpi: 192x192 px

## Icon Design Tips

- **Keep it simple**: Icons are displayed small
- **High contrast**: Make it stand out
- **Recognizable**: Should represent score tracking/card games
- **Avoid text**: Text is hard to read at small sizes
- **Test shapes**: Try on different devices (some are circular, some square)

## Ideas for This App's Icon

- Playing cards (7 of hearts/spades/etc.)
- Scorecard with numbers
- Calculator with "200" (the winning score)
- Trophy with "7"
- Tally marks
- Numbers arranged in a flip pattern

For now, the included grid icon works well and clearly represents scorekeeping!
