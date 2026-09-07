# SCOL (gScol)

A phone app for **Android** and **iPhone**, built with Kotlin Multiplatform.

This guide walks you through running the project from scratch. You don't need to know any code — just follow the steps in order.

---

## The short version

If you already know your way around, here's the whole thing in 6 lines:

1. Install **Android Studio** → https://developer.android.com/studio
2. Download this project (green **Code** button → **Download ZIP**, or `git clone`)
3. Open the project folder in Android Studio, let it load
4. Create a file called `local.properties` and add the two URLs (ask the team)
5. Make a virtual phone (**Device Manager → Create Device**)
6. Press the green **▶** button

Everyone else — keep reading, we explain every step below. 👇

---

## Contents

- [Step 1 — Install Android Studio](#step-1--install-android-studio)
- [Step 2 — Download the project](#step-2--download-the-project)
- [Step 3 — Open it in Android Studio](#step-3--open-it-in-android-studio)
- [Step 4 — Add the settings file](#step-4--add-the-settings-file)
- [Step 5 — Make a virtual phone](#step-5--make-a-virtual-phone)
- [Step 6 — Press Play ▶](#step-6--press-play-)
- [If something goes wrong](#if-something-goes-wrong)
- [Extra: iPhone, terminal, project layout](#extra-stuff)

---

## What you need

Just **one** program: **Android Studio**. It's free, made by Google, and comes with everything else built in (you do **not** need to install Java, the Android SDK, or anything else separately).

That's it. Let's go.

---

## Step 1 — Install Android Studio

1. Open this link: **https://developer.android.com/studio**
2. Click the big **Download** button and accept the terms.
3. Install it, like any other app:
   - **On a Mac:** open the downloaded file, then drag the **Android Studio** icon into your **Applications** folder.
   - **On Windows:** open the downloaded file and keep clicking **Next** (the default choices are fine).
4. Open Android Studio. A **setup wizard** pops up the first time — choose **Standard** and keep clicking **Next**, saying **yes/agree** to everything.
5. It now downloads some extra pieces. **This part is slow — go get a coffee ☕.**

When it's done, you'll see a **"Welcome to Android Studio"** window. Leave it open.

---

## Step 2 — Download the project

**The easy way (no extra tools):**

1. Go to **https://github.com/Arittra101/gScol**
2. Click the green **Code** button, then **Download ZIP**.
3. Unzip the file onto your **Desktop** so it's easy to find. You now have a folder named something like `gScol-main`.

*(Know what Git is? You can instead run `git clone https://github.com/Arittra101/gScol.git` in a terminal. Same result.)*

---

## Step 3 — Open it in Android Studio

1. In the **"Welcome to Android Studio"** window, click **Open**.
2. Find the project folder you just unzipped, click it once, then click **Open**.
3. Android Studio now spends a few minutes **loading the project** and downloading the pieces it needs. You'll see a progress bar at the bottom. **Let it finish** (needs internet).

> ⚠️ **Heads up:** this first load usually ends with a **red error** about a "Base URL" or `local.properties`. **That's totally normal.** We fix it in the next step.

---

## Step 4 — Add the settings file

The app needs one small settings file that isn't included in the download. You'll make it by hand. It's quick.

**1.** Look at the panel on the **left** side of Android Studio. At the very top of it there's a small dropdown — set it to **Project**.

**2.** Find the top folder (named `gScol` or `gScol-main`). **Right-click it → New → File**.

**3.** Type this exact name and press Enter:

```
local.properties
```

**4.** A blank file opens. Paste this in:

```properties
# The two web addresses the app talks to.
# Ask a teammate for the real links and paste them below.
base.url=PASTE_THE_MAIN_URL_HERE
qa.base.url=PASTE_THE_TEST_URL_HERE
```

**5.** Replace the two `PASTE_...` bits with the real web addresses. **Don't have them? Ask the project owner or a teammate** — the app won't start without them.

**6.** Save the file (**Ctrl+S**, or **Cmd+S** on Mac). Then click the **elephant icon 🐘** near the top (its tooltip says *"Sync Project with Gradle Files"*). Wait for it to finish — this time the red error should be gone. ✅

> 🔒 Keep this file private. It can hold passwords, so never send it to anyone or upload it.
>
> *Optional extras:* if you ever get a message about `sdk.dir`, `google.maps.api.key`, or signing keys, those are only needed for advanced/publishing tasks — you can ignore them while just running the app.

---

## Step 5 — Make a virtual phone

Instead of a real phone, you'll use a **fake phone on your screen** (called an *emulator*).

1. In the top menu: **Tools → Device Manager**.
2. Click **Create Device** (or the **+** button).
3. Pick a phone from the list — **Pixel 8** is a good choice. Click **Next**.
4. Pick an Android version. Choose one near the top of the list (**API 34 or higher**). If it shows a **Download** link, click it, wait, then continue.
5. Click **Next**, then **Finish**.

Your virtual phone now appears in the list.

> 💻 On a newer Mac (M1/M2/M3/M4), pick an **arm64** version for the smoothest experience.

---

## Step 6 — Press Play ▶

1. Look at the toolbar near the top of Android Studio. You'll see a **dropdown** and a green **▶ (Play)** button.
2. Make sure the left dropdown shows **composeApp**.
3. In the device dropdown, pick the **virtual phone** you just made.
4. Press the green **▶** button.

Android Studio now builds the app (**the first time is the slowest** — later it's quick), opens the virtual phone, and installs the app on it.

In a minute or two, **the SCOL app opens on the virtual phone**. 🎉 **You did it!**

---

## If something goes wrong

Don't panic — these are the common ones and their fixes.

**❌ "Base URL is missing in local.properties"**
Your `base.url` is empty or the file is missing. Go back to [Step 4](#step-4--add-the-settings-file), add the real URL, and click the 🐘 sync button.

**❌ "SDK location not found"**
Open **File → Settings → Languages & Frameworks → Android SDK** (on Mac: **Android Studio → Settings**) and confirm the SDK is installed. Usually one click fixes it.

**❌ The load / build fails in a confusing way after you changed something**
Top menu: **File → Invalidate Caches → Invalidate and Restart**. Let it reload.

**❌ The virtual phone is super slow or won't start**
Make a new device with an `arm64` image (on Mac), or on Windows turn on "virtualization" in your PC's BIOS.

**❌ The very first build takes forever**
That's normal — it's downloading a lot the first time. Make sure your internet is stable. It's much faster next time.

**Still stuck?** Close and reopen Android Studio, then **File → Sync Project with Gradle Files**. If it still won't work, send a teammate a screenshot of the exact red error message.

---

## Extra stuff

*(Skip this unless you're curious — you don't need it to run the app.)*

**Run it from the terminal instead of pressing Play:**
```shell
# Mac / Linux
./gradlew :composeApp:assembleProdDebug

# Windows
.\gradlew.bat :composeApp:assembleProdDebug
```
This makes an installable `.apk` file inside `composeApp/build/outputs/apk/`.

**Run it on an iPhone (Mac only):**
You need a **Mac** with **Xcode** (free from the Mac App Store). Then:
```shell
open iosApp/iosApp.xcodeproj
```
Pick a simulator at the top of Xcode and press ▶. *(Windows can't build for iPhone.)*

**Choosing which version to run:**
There are 4 flavors: `prodDebug`, `qaDebug`, `prodRelease`, `qaRelease`. For everyday running, **`prodDebug`** or **`qaDebug`** is what you want. You change it under **View → Tool Windows → Build Variants**.

**What's inside the project:**
```
gScol/
├─ composeApp/       ← the app itself (most code lives in src/commonMain)
├─ iosApp/           ← the iPhone wrapper (open in Xcode)
├─ local.properties  ← YOUR private settings file (you made this in Step 4)
└─ README.md         ← this guide
```
Built with: Kotlin Multiplatform, Compose (UI), Koin, Ktor, Coil, DataStore. Works on Android 7.0 and newer.

For a deeper technical dive, see [`TECHNICAL_OVERVIEW.md`](./TECHNICAL_OVERVIEW.md).

---

Happy building! 🚀 If any step confused you, that's our fault — tell the team so we can make this guide clearer.
