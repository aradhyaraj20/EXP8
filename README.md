# EXP8 - Web & Media Explorer 📱🌐🖼️

A modern Android application built with **Kotlin** and **Material Design 3**, demonstrating a multi-fragment architecture featuring an **In-App Web Browser** with offline HTML support and a **Multi-Source Image Gallery Grid** with local photo picker integration and card selection capabilities.

---

## 🌟 Key Features

### 1. 🌐 Web View Fragment (`WebViewFragment`)
- **Full-Screen Web Browsing**: In-app web rendering with JavaScript execution enabled.
- **Visual Progress Indicator**: Material 3 `LinearProgressIndicator` showing real-time loading progress (`0% - 100%`).
- **Pull-To-Refresh**: Integrated `SwipeRefreshLayout` allowing users to swipe down to reload any webpage.
- **Bookmarks & Fast Shortcuts**: Pre-configured menu options for Google, Wikipedia, YouTube, and GitHub.
- **Local Offline HTML Fallback**: Built-in responsive HTML page (`assets/sample.html`) that allows offline web view testing without requiring an active internet connection.

---

### 2. 🖼️ Multi-Source Image Grid Fragment (`ImageGridFragment`)
Displays a 2-column `RecyclerView` grid showcasing images loaded from **3 distinct channels**:
1. **Drawable Resources** (`1. DRAWABLE`): Vector graphics loaded directly from app resources (`R.drawable`).
2. **Local Device Storage** (`2. LOCAL`): PNG images stored in internal app storage (`context.filesDir`). Tapping "Pick Photo from Device" or "Edit" launches the native **Android Photo Picker** (`ActivityResultContracts.GetContent()`) to pick any photo from the device gallery.
3. **Network URLs / Web URIs** (`3. URL/URI`): Online web images rendered using **Glide** with automatic disk caching and fallback placeholders.

---

### 3. 🎯 Selection & Menu Capabilities

- **Card Selection ("Select All")**:
  - Clicking **"Select All"** (via the Toolbar OptionsMenu or Item PopupMenu) highlights all image cards in the grid simultaneously.
  - Selected cards feature a **thick green stroke border** (`#10B981`) and a **checkmark badge icon** (`ic_check_circle`).
  - Tapping individual cards toggles their selection state.

- **PopupMenu**:
  - Triggered on each grid card's 3-dots overflow button or via long press.
  - Actions: **Pick Photo from Device**, **Select All**, **Edit**, and **Share**.

- **Toolbar OptionsMenu**:
  - Contains **Back**, **Forward**, and **Refresh** quick action icons.
  - Nested **Bookmarks** sub-menu (Google, Wikipedia, YouTube, GitHub, Local HTML).
  - Main actions (**Select All**, **Edit**, **Share**) and **Emulator DNS Info** dialog.

- **Bottom Navigation**:
  - `BottomNavigationView` for seamless tab switching between **Web View** and **Image Grid**.

---

## 🛠️ Tech Stack & Dependencies

- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0) | **Target SDK**: 36 | **Compile SDK**: 37
- **Architecture**: Single Activity (`MainActivity`) with multi-fragment hosting (`WebViewFragment`, `ImageGridFragment`).
- **UI Framework**: Material Design 3, ConstraintLayout, CoordinatorLayout, RecyclerView, MaterialCardView, SwipeRefreshLayout.
- **Image Engine**: [Glide 4.16.0](https://github.com/bumptech/github/glide) for asynchronous image loading and caching.
- **Asset Management**: Android Assets (`src/main/assets/sample.html`) and Vector Drawables.

---

## 📁 Project Structure

```
EXP8/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── sample.html              # Local offline HTML webpage
│   │   │   ├── java/com/example/exp8/
│   │   │   │   ├── MainActivity.kt          # Host Activity & Bottom Navigation Controller
│   │   │   │   ├── WebViewFragment.kt       # Fragment handling WebView & browser logic
│   │   │   │   ├── ImageGridFragment.kt     # Fragment handling multi-source image gallery
│   │   │   │   ├── ImageAdapter.kt          # Grid RecyclerView Adapter & PopupMenu handler
│   │   │   │   └── ImageItem.kt             # Data model for image sources and selection state
│   │   │   └── res/
│   │   │       ├── drawable/                # Custom vector drawables & icons
│   │   │       ├── layout/                  # Activity, Fragment, and Grid item layouts
│   │   │       ├── menu/                    # OptionsMenu, PopupMenu & BottomNav menus
│   │   │       └── values/                  # Material 3 colors, themes & string resources
│   └── build.gradle.kts                     # Module build configurations & dependencies
└── README.md
```

---

## 🔧 Troubleshooting & Emulator DNS Setup

If external URLs fail to resolve in the Android Emulator (`net::ERR_NAME_NOT_RESOLVED`):
1. Open Emulator **Extended Controls** (`...` icon on emulator toolbar).
2. Go to **Settings** $\rightarrow$ **Network**.
3. Set **DNS Servers** to `8.8.8.8` or `8.8.4.4`.
4. Alternatively, use the **"Local HTML (Offline)"** option in the Bookmarks menu to test web rendering offline!

---

## 🚀 How to Run

1. Open the project in **Android Studio**.
2. Synchronize Gradle files (`File` $\rightarrow$ `Sync Project with Gradle Files`).
3. Select an Android Virtual Device (AVD) or connected hardware device.
4. Press **`Shift + F10`** or click the green **Run** button.

## images
![Screenshot 2026-09-24 130112.png](images/Screenshot%202026-09-24%20130112.png)
![Screenshot 2026-09-24 130123.png](images/Screenshot%202026-09-24%20130123.png)
![Screenshot 2026-09-24 130150.png](images/Screenshot%202026-09-24%20130150.png)
![Screenshot 2026-09-24 130219.png](images/Screenshot%202026-09-24%20130219.png)
![Screenshot 2026-09-24 130229.png](images/Screenshot%202026-09-24%20130229.png)
![Screenshot 2026-09-24 130238.png](images/Screenshot%202026-09-24%20130238.png)